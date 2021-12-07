package org.obiz.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.util.AttributeKey;

import java.util.concurrent.atomic.AtomicLong;

public class HexDumpProxyFrontendHandler extends ChannelInboundHandlerAdapter {

    public static final AttributeKey<Long> key = AttributeKey.newInstance("request num");
    private static AtomicLong counter = new AtomicLong();

    private final String remoteHost;
    private final int remotePort;

    private volatile Channel outboundChannel;
    private volatile EmbeddedChannel copyInboundChannel;

    public HexDumpProxyFrontendHandler(String remoteHost, int remotePort) {
        this.remoteHost = remoteHost;
        this.remotePort = remotePort;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {

        EmbeddedChannel channel = new EmbeddedChannel(
                new HttpRequestDecoder(), new CustomHttpDataHandler());
        DefaultChannelConfig channelConfig = (DefaultChannelConfig) channel.config();
        channelConfig.setConnectTimeoutMillis(500);
        channel.writeInbound();
        copyInboundChannel = channel;


        final Channel inboundChannel = ctx.channel();
        // Start the connection attempt.
        Bootstrap b = new Bootstrap();
        b.group(inboundChannel.eventLoop())
                .channel(ctx.channel().getClass())
                .handler(new HexDumpProxyBackendHandler(inboundChannel))
                .option(ChannelOption.AUTO_READ, false);
        ChannelFuture f = b.connect(remoteHost, remotePort);
        outboundChannel = f.channel();
        f.addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                // connection complete start to read first data
                inboundChannel.read();
            } else {
                // Close the connection if the connection attempt has failed.
                inboundChannel.close();
            }
        });
    }

    @Override
    public void channelRead(final ChannelHandlerContext ctx, Object msg) {
        if (outboundChannel.isActive()) {
            final Long id = counter.incrementAndGet();
            outboundChannel.attr(key).set(id);
            copyInboundChannel.attr(key).set(id);
            forwardAndDump(ctx, msg, outboundChannel, copyInboundChannel);
//            forwardAndDump(ctx, msg, outboundChannel);
        }
    }

    static void forwardAndDump(ChannelHandlerContext ctx, Object msg, Channel outboundChannel, EmbeddedChannel embeddedChannel) {
        ByteBuf dupBuf = ((ByteBuf) msg).copy();
        embeddedChannel.writeInbound(dupBuf);
        embeddedChannel.readInbound();

        forwardAndDump(ctx, msg, outboundChannel);
    }

    static void forwardAndDump(ChannelHandlerContext ctx, Object msg, Channel outboundChannel) {

        outboundChannel.writeAndFlush(msg).addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                // was able to flush out data, start to read the next chunk
                ctx.channel().read();
            } else {
                future.channel().close();
            }
        });
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        if (outboundChannel != null) {
            closeOnFlush(outboundChannel);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        closeOnFlush(ctx.channel());
    }

    /**
     * Closes the specified channel after all queued write requests are flushed.
     */
    static void closeOnFlush(Channel ch) {
        if (ch.isActive()) {
            ch.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
        }
    }
}
