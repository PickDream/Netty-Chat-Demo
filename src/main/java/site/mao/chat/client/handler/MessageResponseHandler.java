package site.mao.chat.client.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import site.mao.chat.protocol.packet.MessageResponsePacket;

import java.util.Date;

public class MessageResponseHandler extends SimpleChannelInboundHandler<MessageResponsePacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageResponsePacket messageResponsePacket) throws Exception {
       String fromUserId = messageResponsePacket.getFromUserId();
       String fromUserName = messageResponsePacket.getFromUserName();
       System.out.println(fromUserId + ":" + fromUserName + " -> " + messageResponsePacket .getMessage());
    }
}
