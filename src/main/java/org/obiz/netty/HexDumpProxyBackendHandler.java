package org.obiz.netty;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.DefaultChannelConfig;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.http.HttpResponseDecoder;

public class HexDumpProxyBackendHandler extends ChannelInboundHandlerAdapter {

    private final Channel inboundChannel;
    private EmbeddedChannel copyOutboundChannel;

    public HexDumpProxyBackendHandler(Channel inboundChannel) {
        this.inboundChannel = inboundChannel;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        EmbeddedChannel channel = new EmbeddedChannel(
                new HttpResponseDecoder(), new CustomHttpDataHandler());
        DefaultChannelConfig channelConfig = (DefaultChannelConfig) channel.config();
        channelConfig.setConnectTimeoutMillis(500);
        channel.writeInbound();
        copyOutboundChannel = channel;

        ctx.read();
        ctx.write(Unpooled.EMPTY_BUFFER);
    }

    @Override
    public void channelRead(final ChannelHandlerContext ctx, Object msg) {
        HexDumpProxyFrontendHandler.forwardAndDump(ctx, msg, inboundChannel, copyOutboundChannel);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        HexDumpProxyFrontendHandler.closeOnFlush(inboundChannel);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        HexDumpProxyFrontendHandler.closeOnFlush(ctx.channel());
    }
}
