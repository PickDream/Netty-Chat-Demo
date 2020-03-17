package site.mao.chat.server.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import site.mao.chat.protocol.packet.MessageRequestPacket;
import site.mao.chat.protocol.packet.MessageResponsePacket;
import site.mao.chat.session.Session;
import site.mao.chat.session.SessionMap;

import java.util.Date;
import java.util.Objects;


@ChannelHandler.Sharable
public class MessageRequestHandler extends SimpleChannelInboundHandler<MessageRequestPacket> {

    public static final MessageRequestHandler INSTANCE = new MessageRequestHandler();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageRequestPacket msg) throws Exception {

        //1. 解析发送者信息
        Session session = SessionMap.getSession(ctx.channel());
        //2.封装MessageResponsePacket
        MessageResponsePacket messageResponsePacket = new MessageResponsePacket();
        messageResponsePacket.setFromUserId(session.getUserId());
        messageResponsePacket.setMessage(msg.getMessage());
        messageResponsePacket.setFromUserName(session.getUserName());
        //3. 拿到消息接受方的channel
        Channel toChannel = SessionMap.getChannel(msg.getToUserId());

        if (Objects.nonNull(toChannel)&&SessionMap.hasLogin(toChannel)){
            toChannel.writeAndFlush(messageResponsePacket);
        }else {
            System.err.println("["+msg.getToUserId()+"没有上线"+"]");
        }


    }
}
