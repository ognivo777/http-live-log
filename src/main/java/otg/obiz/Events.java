package otg.obiz;

import io.quarkus.vertx.ConsumeEvent;
import io.vertx.core.impl.ConcurrentHashSet;

import javax.enterprise.context.ApplicationScoped;
import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.Set;

@ServerEndpoint("/events")
@ApplicationScoped
public class Events {
    Set<Session> sessions = new ConcurrentHashSet<>();

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("NEW CLIENT!");
        sessions.add(session);
    }

    @OnClose
    public void onClose(Session session) {
        System.out.println("CLIENT LEFT!");
        sessions.remove(session);
    }

    @ConsumeEvent("new-request")
    public void consume(RequestInfo requestInfo) {
        System.out.println("ON EVENT!");
        System.out.println("requestInfo.json() = " + requestInfo.json());
        sessions.forEach(s -> {
            s.getAsyncRemote().sendObject(System.currentTimeMillis() + " : " + requestInfo.json(), result ->  {
                if (result.getException() != null) {
                    System.out.println("Unable to send message: " + result.getException());
                }
            });
        });
    }
}
