package org.obiz;

import io.quarkus.vertx.ConsumeEvent;
import io.vertx.core.impl.ConcurrentHashSet;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.Set;

@ServerEndpoint("/events")
@ApplicationScoped
public class Events {
    private static Logger log = Logger.getLogger(Events.class);
    Set<Session> sessions = new ConcurrentHashSet<>();

    @OnOpen
    public void onOpen(Session session) {
        log.info("NEW CLIENT!");
        sessions.add(session);
    }

    @OnClose
    public void onClose(Session session) {
        log.info("CLIENT LEFT!");
        sessions.remove(session);
    }

    @ConsumeEvent("new-request")
    public void consume(RequestInfo requestInfo) {
        log.info("ON EVENT!");
        sessions.forEach(s -> s.getAsyncRemote().sendObject(requestInfo.json(), result ->  {
            if (result.getException() != null) {
                log.error("Unable to send message to web-socket client!", result.getException());
            }
        }));
    }
}
