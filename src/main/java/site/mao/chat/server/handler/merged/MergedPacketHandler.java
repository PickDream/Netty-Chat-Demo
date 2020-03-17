package site.mao.chat.server.handler.merged;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import site.mao.chat.protocol.Command;
import site.mao.chat.protocol.Packet;
import site.mao.chat.server.handler.*;

import java.util.HashMap;
import java.util.Map;


@ChannelHandler.Sharable
public class MergedPacketHandler extends SimpleChannelInboundHandler<Packet> {

    public static final MergedPacketHandler INSTANCE = new MergedPacketHandler();

    private Map<Byte,SimpleChannelInboundHandler<? extends Packet>> handlerMap;

    private MergedPacketHandler(){
        handlerMap = new HashMap<>();

        handlerMap.put(Command.MESSAGE_REQUEST, MessageRequestHandler.INSTANCE);
        handlerMap.put(Command.CREATE_GROUP_REQUEST, CreateGroupRequestHandler.INSTANCE);
        handlerMap.put(Command.JOIN_GROUP_REQUEST, JoinGroupRequestHandler.INSTANCE);
        handlerMap.put(Command.QUIT_GROUP_REQUEST, QuitGroupRequestHandler.INSTANCE);
        handlerMap.put(Command.GROUP_MEMBER_REQUEST, ListGroupMembersRequestHandler.INSTANCE);
        handlerMap.put(Command.GROUP_MESSAGE_REQUEST, GroupMessageRequestHandler.INSTANCE);
        handlerMap.put(Command.LOGOUT_REQUEST, LogoutRequestHandler.INSTANCE);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Packet packet) throws Exception {
        handlerMap.get(packet.getCommandType()).channelRead(ctx, packet);
    }
}
