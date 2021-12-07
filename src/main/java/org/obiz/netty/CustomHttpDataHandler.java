package org.obiz.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;

import java.nio.charset.Charset;

public class CustomHttpDataHandler extends SimpleChannelInboundHandler {
    StringBuilder responseData = new StringBuilder();
    private Charset charset;

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        final Transit t = ctx.channel().attr(HexDumpProxyFrontendHandler.key).get();

        if (msg instanceof HttpRequest) {
            System.out.println("REQUEST id = " + t.getId());
            HttpRequest request = (HttpRequest) msg;
//            records.put(id, new Transit(id, request));
            t.addRequest(request);

            responseData.append("----=====REQUEST====----");
            responseData.append(request.method());
            responseData.append(" ").append(request.uri());
            charset = HttpUtil.getCharset(request);
            request.headers().forEach(entry -> {
                responseData.append("H: ").append(entry.getKey()).append("=").append(entry.getValue());
            });
        }

        if (msg instanceof HttpResponse) {
            System.out.println("RESPONSE id = " + t.getId());
            HttpResponse response = (HttpResponse) msg;
//            records.get(id).addResponse(response);
            t.addResponse(response);
            responseData.append("----=====RESPONSE====----");
            charset = HttpUtil.getCharset(response);
            response.headers().forEach(entry -> {
                responseData.append("H: ").append(entry.getKey()).append("=").append(entry.getValue());
            });
        }

        if (msg instanceof HttpContent) {
            System.out.println("CONTENT id = " + t.getId());
            HttpContent httpContent = (HttpContent) msg;
            t.addContent(httpContent);
//            records.get(id).addContent(httpContent);
            ByteBuf content = httpContent.content();
//            if (content.isReadable()) {
                responseData.append("\n").append(content.toString(charset));
//            }
        }

        if (msg instanceof LastHttpContent) {
//            LastHttpContent trailer = (LastHttpContent) msg;
//            responseData.append("\nBODY:").append(trailer.content().toString(charset));
            System.out.println("=====================");
//            final Transit transit = records.get(id);
            if(t.isFinished()) {
                System.out.println("response time: " + t.responseTime());
            }
            System.out.println("DATA:\n" + responseData);
            responseData.setLength(0);
        }

    }
}
