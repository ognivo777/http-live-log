package org.obiz;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.runtime.annotations.RegisterForReflection;
import org.jboss.logging.Logger;

import java.util.List;
import java.util.Map;


@RegisterForReflection
public class RequestInfo {
    private static Logger log = Logger.getLogger(RequestInfo.class);

    private long time;
    private String method;
    private String host;
    private String path;
    private String query;
    private String contentType;
    private String payload;
    private String formattedPayload;
    private List<Map.Entry<String, String>> headers;

    public RequestInfo(String host, String method, String path, String query, String contentType, List<Map.Entry<String, String>> headers, String payload) {
        this.host = host;
        this.time = System.currentTimeMillis();
        this.method = method;
        this.path = path;
        this.query = query;
        this.contentType = contentType;
        this.headers = headers;
        this.payload = payload;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public List<Map.Entry<String, String>> getHeaders() {
        return headers;
    }

    public void setHeaders(List<Map.Entry<String, String>> headers) {
        this.headers = headers;
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
            log.error("Cant create JSON for RequestInfo!", e);
            return "{\"error\":\"" + e.getMessage() + "\"}";
        }
    }
}
