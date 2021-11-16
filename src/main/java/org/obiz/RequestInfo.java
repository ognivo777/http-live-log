package org.obiz;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.runtime.annotations.RegisterForReflection;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServerRequest;

@RegisterForReflection
public class RequestInfo {
    private long time;
    private String method;
    private String path;
    private String query;
    private String contentType;
    private String payload;
    private String formattedPayload;

    public RequestInfo(String method, String path, String query, String contentType, String payload) {
        this.time = System.currentTimeMillis();
        this.method = method;
        this.path = path;
        this.query = query;
        this.contentType = contentType;
        this.payload = payload;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getFormattedPayload() {
        return formattedPayload;
    }

    public void setFormattedPayload(String formattedPayload) {
        this.formattedPayload = formattedPayload;
    }

    public String json() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "{\"error\":\"" + e.getMessage() + "\"}";
        }
    }
}
