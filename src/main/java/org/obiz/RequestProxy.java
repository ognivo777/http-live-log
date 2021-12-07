package org.obiz;

import io.netty.handler.codec.DecoderResult;
import io.vertx.codegen.annotations.CacheReturn;
import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.GenIgnore;
import io.vertx.codegen.annotations.Nullable;
import io.vertx.core.*;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.*;
import io.vertx.core.http.impl.HttpServerRequestInternal;
import io.vertx.core.net.NetSocket;
import io.vertx.core.net.SocketAddress;
import io.vertx.core.streams.Pipe;
import io.vertx.core.streams.WriteStream;

import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.security.cert.X509Certificate;
import java.util.Map;

public class RequestProxy implements HttpServerRequestInternal {
    private HttpServerRequestInternal rawRequest;
    private String newUri;

    public RequestProxy(HttpServerRequest rawRequest, String newUri) {
        this.rawRequest = (HttpServerRequestInternal)rawRequest;
        this.newUri = newUri;
    }

    @Override
    public HttpServerRequest exceptionHandler(Handler<Throwable> handler) {
        return rawRequest.exceptionHandler(handler);
    }

    @Override
    public HttpServerRequest handler(Handler<Buffer> handler) {
        return rawRequest.handler(handler);
    }

    @Override
    public HttpServerRequest pause() {
        return rawRequest.pause();
    }

    @Override
    public HttpServerRequest resume() {
        return rawRequest.resume();
    }

    @Override
    public HttpServerRequest fetch(long amount) {
        return rawRequest.fetch(amount);
    }

    @Override
    public HttpServerRequest endHandler(Handler<Void> endHandler) {
        return rawRequest.endHandler(endHandler);
    }

    @Override
    public HttpVersion version() {
        return rawRequest.version();
    }

    @Override
    public HttpMethod method() {
        return rawRequest.method();
    }

    @Override
    public boolean isSSL() {
        return rawRequest.isSSL();
    }

    @Override
    @Nullable
    public String scheme() {
        return rawRequest.scheme();
    }

    @Override
    public String uri() {
        return newUri;
    }

    @Override
    @Nullable
    public String path() {
        return rawRequest.path();
    }

    @Override
    @Nullable
    public String query() {
        return rawRequest.query();
    }

    @Override
    @Nullable
    public String host() {
        return rawRequest.host();
    }

    @Override
    public long bytesRead() {
        return rawRequest.bytesRead();
    }

    @Override
    @CacheReturn
    public HttpServerResponse response() {
        return rawRequest.response();
    }

    @Override
    @CacheReturn
    public MultiMap headers() {
        return rawRequest.headers();
    }

    @Override
    @Nullable
    public String getHeader(String headerName) {
        return rawRequest.getHeader(headerName);
    }

    @Override
    @GenIgnore({"permitted-type"})
    public String getHeader(CharSequence headerName) {
        return rawRequest.getHeader(headerName);
    }

    @Override
    @CacheReturn
    public MultiMap params() {
        return rawRequest.params();
    }

    @Override
    @Nullable
    public String getParam(String paramName) {
        return rawRequest.getParam(paramName);
    }

    @Override
    @CacheReturn
    public SocketAddress remoteAddress() {
        return rawRequest.remoteAddress();
    }

    @Override
    @CacheReturn
    public SocketAddress localAddress() {
        return rawRequest.localAddress();
    }

    @Override
    @GenIgnore({"permitted-type"})
    public SSLSession sslSession() {
        return rawRequest.sslSession();
    }

    @Override
    @GenIgnore
    public X509Certificate[] peerCertificateChain() throws SSLPeerUnverifiedException {
        return rawRequest.peerCertificateChain();
    }

    @Override
    public String absoluteURI() {
        return rawRequest.absoluteURI();
    }

    @Override
    @Fluent
    public HttpServerRequest bodyHandler(@Nullable Handler<Buffer> bodyHandler) {
        return rawRequest.bodyHandler(bodyHandler);
    }

    @Override
    public HttpServerRequest body(Handler<AsyncResult<Buffer>> handler) {
        return rawRequest.body(handler);
    }

    @Override
    public Future<Buffer> body() {
        return rawRequest.body();
    }

    @Override
    public void end(Handler<AsyncResult<Void>> handler) {
        rawRequest.end(handler);
    }

    @Override
    public Future<Void> end() {
        return rawRequest.end();
    }

    @Override
    public void toNetSocket(Handler<AsyncResult<NetSocket>> handler) {
        rawRequest.toNetSocket(handler);
    }

    @Override
    public Future<NetSocket> toNetSocket() {
        return rawRequest.toNetSocket();
    }

    @Override
    @Fluent
    public HttpServerRequest setExpectMultipart(boolean expect) {
        return rawRequest.setExpectMultipart(expect);
    }

    @Override
    public boolean isExpectMultipart() {
        return rawRequest.isExpectMultipart();
    }

    @Override
    @Fluent
    public HttpServerRequest uploadHandler(@Nullable Handler<HttpServerFileUpload> uploadHandler) {
        return rawRequest.uploadHandler(uploadHandler);
    }

    @Override
    @CacheReturn
    public MultiMap formAttributes() {
        return rawRequest.formAttributes();
    }

    @Override
    @Nullable
    public String getFormAttribute(String attributeName) {
        return rawRequest.getFormAttribute(attributeName);
    }

    @Override
    @CacheReturn
    public int streamId() {
        return rawRequest.streamId();
    }

    @Override
    public void toWebSocket(Handler<AsyncResult<ServerWebSocket>> handler) {
        rawRequest.toWebSocket(handler);
    }

    @Override
    public Future<ServerWebSocket> toWebSocket() {
        return rawRequest.toWebSocket();
    }

    @Override
    public boolean isEnded() {
        return rawRequest.isEnded();
    }

    @Override
    @Fluent
    public HttpServerRequest customFrameHandler(Handler<HttpFrame> handler) {
        return rawRequest.customFrameHandler(handler);
    }

    @Override
    @CacheReturn
    public HttpConnection connection() {
        return rawRequest.connection();
    }

    @Override
    public StreamPriority streamPriority() {
        return rawRequest.streamPriority();
    }

    @Override
    @Fluent
    public HttpServerRequest streamPriorityHandler(Handler<StreamPriority> handler) {
        return rawRequest.streamPriorityHandler(handler);
    }

    @Override
    @GenIgnore
    public DecoderResult decoderResult() {
        return rawRequest.decoderResult();
    }

    @Override
    public @Nullable Cookie getCookie(String name) {
        return rawRequest.getCookie(name);
    }

    @Override
    public int cookieCount() {
        return rawRequest.cookieCount();
    }

    @Override
    public Map<String, Cookie> cookieMap() {
        return rawRequest.cookieMap();
    }

    @Override
    @Fluent
    public HttpServerRequest routed(String route) {
        return rawRequest.routed(route);
    }

    @Override
    public Pipe<Buffer> pipe() {
        return rawRequest.pipe();
    }

    @Override
    public Future<Void> pipeTo(WriteStream<Buffer> dst) {
        return rawRequest.pipeTo(dst);
    }

    @Override
    public void pipeTo(WriteStream<Buffer> dst, Handler<AsyncResult<Void>> handler) {
        rawRequest.pipeTo(dst, handler);
    }

    @Override
    public Context context() {
        return rawRequest.context();
    }

    @Override
    public Object metric() {
        return rawRequest.metric();
    }
}
