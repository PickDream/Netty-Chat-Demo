package site.mao.chat.server.handler;

import com.sun.jdi.ThreadGroupReference;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import site.mao.chat.protocol.packet.CreateGroupRequestPacket;
import site.mao.chat.protocol.packet.CreateGroupResponsePacket;
import site.mao.chat.session.Session;
import site.mao.chat.session.SessionMap;
import site.mao.chat.utils.IDUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@ChannelHandler.Sharable
public class CreateGroupRequestHandler extends SimpleChannelInboundHandler<CreateGroupRequestPacket> {

    public static final CreateGroupRequestHandler INSTANCE = new CreateGroupRequestHandler();
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CreateGroupRequestPacket createGroupRequestPacket) throws Exception {

        List<String> userIdList = createGroupRequestPacket.getUserIdList();

        List<String> userNameList = new ArrayList<>();

        ChannelGroup channelGroup = new DefaultChannelGroup(ctx.executor());

        for (String userId:userIdList){
            Channel channel = SessionMap.getChannel(userId);
            if (Objects.nonNull(channel)){
                channelGroup.add(channel);
                userNameList.add(SessionMap.getSession(channel).getUserName());
            }
        }

        CreateGroupResponsePacket createGroupResponsePacket = new CreateGroupResponsePacket();
        createGroupResponsePacket.setSuccess(true);
        createGroupResponsePacket.setGroupId(IDUtil.randomId());
        createGroupResponsePacket.setUserNameList(userNameList);

        //存入管理器
        SessionMap.bindChannelGroup(createGroupResponsePacket.getGroupId(),
                channelGroup);

        channelGroup.writeAndFlush(createGroupResponsePacket);

        System.out.print("群创建成功，id 为[" + createGroupResponsePacket.getGroupId() + "], ");
        System.out.println("群里面有：" + createGroupResponsePacket.getUserNameList());
    }
}
