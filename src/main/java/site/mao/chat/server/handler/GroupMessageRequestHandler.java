package site.mao.chat.server.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import site.mao.chat.protocol.packet.GroupMessageRequest;
import site.mao.chat.protocol.packet.GroupMessageResponse;
import site.mao.chat.session.Session;
import site.mao.chat.session.SessionMap;

@ChannelHandler.Sharable
public class GroupMessageRequestHandler extends SimpleChannelInboundHandler<GroupMessageRequest> {

    public static final GroupMessageRequestHandler INSTANCE = new GroupMessageRequestHandler();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupMessageRequest messageRequest) throws Exception {

        Session sender = SessionMap.getSession(ctx.channel());

        String msg = messageRequest.getMessage();

        String groupId = messageRequest.getGroupId();

        ChannelGroup channelGroup = SessionMap.getChannelGroup(groupId);

        GroupMessageResponse messageResponse = new GroupMessageResponse();

        messageResponse.setSenderSession(sender);

        messageResponse.setGroupId(groupId);

        messageResponse.setMessage(msg);

        channelGroup.writeAndFlush(messageResponse);

    }
}
