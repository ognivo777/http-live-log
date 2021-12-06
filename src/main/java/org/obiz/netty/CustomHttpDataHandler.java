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

        if (msg instanceof HttpRequest) {
            HttpRequest request = (HttpRequest) msg;
            responseData.append("----=====REQUEST====----");
            responseData.append(request.method());
            responseData.append(" ").append(request.uri());
            charset = HttpUtil.getCharset(request);
            request.headers().forEach(entry -> {
                responseData.append("H: ").append(entry.getKey()).append("=").append(entry.getValue());
            });
        }

        if (msg instanceof HttpResponse) {
            HttpResponse response = (HttpResponse) msg;
            responseData.append("----=====RESPONSE====----");
            charset = HttpUtil.getCharset(response);
            response.headers().forEach(entry -> {
                responseData.append("H: ").append(entry.getKey()).append("=").append(entry.getValue());
            });
        }

        if (msg instanceof HttpContent) {
            HttpContent httpContent = (HttpContent) msg;
            ByteBuf content = httpContent.content();
            if (content.isReadable()) {
                responseData.append(content.toString(charset));
            }
        }

        if (msg instanceof LastHttpContent) {
//            LastHttpContent trailer = (LastHttpContent) msg;
            System.out.println("=====================\nDATA:\n" + responseData);
            responseData.setLength(0);
        }


//        if (responseData.length()!=0) {
//            System.out.println("DATA:\n" + responseData);
//            responseData.setLength(0);
//        }

    }
}
