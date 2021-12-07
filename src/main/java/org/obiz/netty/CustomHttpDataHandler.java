package org.obiz.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;

import java.nio.charset.Charset;
import java.util.concurrent.ConcurrentHashMap;

public class CustomHttpDataHandler extends SimpleChannelInboundHandler {

//    private final ConcurrentHashMap<Long, Transit> records = new ConcurrentHashMap<>();

    StringBuilder responseData = new StringBuilder();
    private Charset charset;

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {

        final Long id = ctx.channel().attr(HexDumpProxyFrontendHandler.key).get();

        if (msg instanceof HttpRequest) {
            System.out.println("REQUEST id = " + id);

            HttpRequest request = (HttpRequest) msg;
//            records.put(id, new Transit(id, request));

            responseData.append("----=====REQUEST====----");
            responseData.append(request.method());
            responseData.append(" ").append(request.uri());
            charset = HttpUtil.getCharset(request);
            request.headers().forEach(entry -> {
                responseData.append("H: ").append(entry.getKey()).append("=").append(entry.getValue());
            });
        }

        if (msg instanceof HttpResponse) {
            System.out.println("RESPONSE id = " + id);
            HttpResponse response = (HttpResponse) msg;
//            records.get(id).addResponse(response);
            responseData.append("----=====RESPONSE====----");
            charset = HttpUtil.getCharset(response);
            response.headers().forEach(entry -> {
                responseData.append("H: ").append(entry.getKey()).append("=").append(entry.getValue());
            });
        }

        if (msg instanceof HttpContent) {
            System.out.println("CONTENT id = " + id);
            HttpContent httpContent = (HttpContent) msg;
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
//            if(transit.isFinished()) {
//                System.out.println("response time: " + transit.responseTime());
//            }
            System.out.println("DATA:\n" + responseData);
            responseData.setLength(0);
        }

    }
}
