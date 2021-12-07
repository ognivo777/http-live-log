package org.obiz;

import io.vertx.codegen.annotations.Nullable;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.streams.WriteStream;


public class WriteStreamWrapper implements WriteStream {
    private Buffer buffer;

    public WriteStreamWrapper() {
        buffer = Buffer.buffer();
    }

    @Override
    public WriteStream exceptionHandler(Handler handler) {
        return null;
    }

    @Override
    public Future<Void> write(Object data) {
//        return buffer.appendBuffer();
        return null;
    }

    @Override
    public Future<Void> end() {
        return WriteStream.super.end();
    }

    @Override
    public Future<Void> end(Object data) {
        return WriteStream.super.end(data);
    }

    @Override
    public void end(Object data, Handler handler) {
        WriteStream.super.end(data, handler);
    }

    @Override
    public WriteStream setWriteQueueMaxSize(int maxSize) {
        return null;
    }

    @Override
    public boolean writeQueueFull() {
        return false;
    }

    @Override
    public WriteStream drainHandler(@Nullable Handler handler) {
        return null;
    }

    @Override
    public void end(Handler handler) {

    }

    @Override
    public void write(Object data, Handler handler) {

    }
}
