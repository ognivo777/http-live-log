package org.obiz.netty;

import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;

public class Transit {
    private long id;
    private long startTime;
    private long endTime;
    private HttpRequest request;
    private HttpContent requestContent;
    private HttpResponse response;
    private HttpContent responseContent;

    public Transit(long id, HttpRequest request) {
        this.id = id;
        this.request = request;
        this.startTime = System.currentTimeMillis();
    }

    public void addResponse(HttpResponse response) {
        this.response = response;
    }

    public void addContent(HttpContent httpContent) {
        if(response==null) {
            requestContent = httpContent;
        } else {
            responseContent = httpContent;
        }
    }

    public boolean isFinished() {
        return response!=null;
    }

    public long responseTime() {
        return endTime - startTime;
    }
}
