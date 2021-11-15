package org.obiz;

import io.quarkus.vertx.web.Body;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServerRequest;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;


@Path("/")
public class Listener {

    @Inject
    EventBus bus;

    @GET
    @Path("{var:.*}")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.WILDCARD)
    public String get(@Context HttpServerRequest request, @Body String body) {
        return processRequest(request, body);
    }

    @PUT
    @Path("{var:.*}")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.WILDCARD)
    public String put(@Context HttpServerRequest request, @Body String body) {
        return processRequest(request, body);
    }

    @POST
    @Path("{var:.*}")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.WILDCARD)
    public String post(@Context HttpServerRequest request, @Body String body) {
        return processRequest(request, body);
    }

    @DELETE
    @Path("{var:.*}")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.WILDCARD)
    public String delete(@Context HttpServerRequest request, @Body String body) {
        return processRequest(request, body);
    }

    private String processRequest(HttpServerRequest request, String body) {
        RequestInfo requestInfo = new RequestInfo(
                request.method().name(),
                request.path(),
                request.query(),
                request.getHeader(HttpHeaders.CONTENT_TYPE),
                body
        );

        String json = requestInfo.json();
        System.out.println("json to send = " + json);
        bus.send("new-request", requestInfo);
        return requestInfo.getMethod() + ":\n" + requestInfo.getPath() + "\n" + requestInfo.getQuery() + "\n" + requestInfo.getContentType();
    }
}