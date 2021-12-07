package org.obiz;

import io.quarkus.runtime.Startup;
import io.quarkus.vertx.web.Route;
import io.quarkus.vertx.web.RoutingExchange;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.httpproxy.HttpProxy;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import static org.obiz.Listener.processRequest;

@ApplicationScoped
@Startup
public class Proxy {

    public static final String HTTPS = "https://";
    public static final String HTTP = "http://";
    public static final int HTTP_PORT = 80;
    public static final int HTTPS_PORT = 443;

    private static Logger log = Logger.getLogger(Proxy.class);

    @ConfigProperty(name = "app.default.redirect.host", defaultValue = "localhost")
    String defaultHost;
    @ConfigProperty(name = "app.default.redirect.port", defaultValue = "80")
    Integer defaultPort;

    private final Vertx vertx;
    EventBus bus;

    private HttpProxy reverseProxy;
    private HttpClient proxyClient;

    @Inject
    public Proxy(Vertx vertx, EventBus bus) {
        this.vertx = vertx;
        this.bus = bus;
    }

    @PostConstruct
    void init() {
        proxyClient = vertx.createHttpClient();
        reverseProxy = HttpProxy.reverseProxy(proxyClient);
        reverseProxy.origin(defaultPort, defaultHost);
    }

    @Route(regex = "/proxy/.*", methods = Route.HttpMethod.GET)
    void proxy(RoutingExchange ex) {
        log.info("===PROXY===");
        HttpServerRequest request = ex.request();



        //ToDo перехватить поток BODY в запросе
        //ToDo перехватить ответ сервиса и показать на UI
        //мб всё это спустить на уровень ниже - до vertx или даже netty

        RequestInfo requestInfo = processRequest(request, "-=NOT IMPLEMENTED YET=-");
        bus.send("new-request", requestInfo);

        log.info("request.uri() = " + request.uri());
        log.info("request.path() = " + request.path());
        String extraPath = request.path().substring(7);
        log.info("extraPath = " + extraPath);
        String proto = "";
        int port = HTTP_PORT;
        if(extraPath.startsWith(HTTPS)) {
            proto = HTTPS;
            port = HTTPS_PORT;
        } else if (extraPath.startsWith(HTTP)) {
            proto = HTTP;
        }
        if(proto.length()>0) {
            String extraPathNoProto = extraPath.substring(proto.length());
            log.info("extraPathNoProto = " + extraPathNoProto);
            int hostPortEnd = extraPathNoProto.indexOf('/');
            if (hostPortEnd > 0) {
                String hostPort = extraPathNoProto.substring(0, hostPortEnd);
                log.info("hostPort = " + hostPort);
                String host = hostPort;
                String stringPort = "";
                int portSplitPos = hostPort.indexOf(':');
                if (portSplitPos > 0) {
                    stringPort = hostPort.substring(portSplitPos + 1);
                    host = hostPort.substring(0, portSplitPos);
                }
                log.info("redirect to host: " + host);

                try {
                    if(!stringPort.isEmpty()){
                        port = Integer.parseInt(stringPort);
                    }
                    log.info("redirect to port: " + port);
                    String uri = request.uri();
                    log.info("Original uri: " + uri);
                    String partToBeRemove = "proxy/" + proto + hostPort + "/";
                    log.info("partToBeRemove = " + partToBeRemove);
                    String newUri = uri.replace(partToBeRemove, "");
                    log.info("newUri = " + newUri);
                    HttpServerRequest wrappedRequest = new RequestProxy(request, newUri);
                    HttpProxy httpProxy = HttpProxy.reverseProxy(proxyClient);
                    httpProxy.origin(port, host);
                    httpProxy.handle(wrappedRequest);
                    return;
                } catch (NumberFormatException e) {
                    log.info("Can't parse port!");
                }
            }
        } else {
            log.info("Proto not found! Use default redirect.");
        }
        reverseProxy.handle(request);
    }

}
