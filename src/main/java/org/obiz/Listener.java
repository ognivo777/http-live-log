package org.obiz;

import io.quarkus.vertx.web.Body;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServerRequest;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Path("/")
public class Listener {

    private static Logger log = Logger.getLogger(Listener.class);
    @Inject
    EventBus bus;

    @GET
    @PUT
    @POST
    @DELETE
    @Path("{var:.*}")
    @Produces(MediaType.WILDCARD)
    @Consumes(MediaType.WILDCARD)
    public String delete(@Context HttpServerRequest request, @Body String body) {
        return processRequest(request, body);
    }


    private String processRequest(HttpServerRequest request, String body) {
        List<Map.Entry<String, String>> headers = new ArrayList<>();
        request.headers().forEach(stringStringEntry -> {
            log.info("Header: " + stringStringEntry.getKey() + " - " + stringStringEntry.getValue());
            headers.add(stringStringEntry);
        });
        RequestInfo requestInfo = new RequestInfo(
                request.remoteAddress().host(),
                request.method().name(),
                request.path(),
                request.query(),
                request.getHeader(HttpHeaders.CONTENT_TYPE),
                headers,
                body
        );

        String json = requestInfo.json();
        System.out.println("json to send = " + json);
        bus.send("new-request", requestInfo);
        return requestInfo.getMethod() + ":\n" + requestInfo.getPath() + "\n" + requestInfo.getQuery() + "\n" + requestInfo.getContentType();
    }
}