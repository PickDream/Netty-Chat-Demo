package site.mao.chat.server.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import site.mao.chat.protocol.packet.LogoutRequestPacket;
import site.mao.chat.protocol.packet.LogoutResponsePacket;
import site.mao.chat.session.Session;
import site.mao.chat.session.SessionMap;

import java.util.List;

@ChannelHandler.Sharable
public class LogoutRequestHandler extends SimpleChannelInboundHandler<LogoutRequestPacket> {

    public static final LogoutRequestHandler INSTANCE = new LogoutRequestHandler();
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LogoutRequestPacket requestPacket) throws Exception {
        SessionMap.removeChannel(ctx.channel());
        LogoutResponsePacket logoutResponsePacket = new LogoutResponsePacket();
        logoutResponsePacket.setSuccess(true);
        ctx.writeAndFlush(logoutResponsePacket);
    }
}
