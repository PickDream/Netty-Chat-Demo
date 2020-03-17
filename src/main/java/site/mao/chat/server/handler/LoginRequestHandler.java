package site.mao.chat.server.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import site.mao.chat.protocol.packet.LoginRequestPacket;
import site.mao.chat.protocol.packet.LoginResponsePacket;
import site.mao.chat.session.Session;
import site.mao.chat.session.SessionMap;

import java.util.Date;
import java.util.UUID;


@ChannelHandler.Sharable
public class LoginRequestHandler extends SimpleChannelInboundHandler<LoginRequestPacket> {

    public static final LoginRequestHandler INSTANCE = new LoginRequestHandler();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRequestPacket msg) throws Exception {

        System.out.println(new Date() + ": 收到客户端登录请求……");

        LoginResponsePacket loginResponsePacket = new LoginResponsePacket();
        loginResponsePacket.setVersion(msg.getVersion());
        if (valid(msg)) {
            Session session = new Session();
            String userId = randomUserKey();
            session.setUserId(userId);
            session.setUserName(msg.getUserName());
            SessionMap.bindSession(session, ctx.channel());
            loginResponsePacket.setSuccess(true);
            loginResponsePacket.setUserId(userId);
            loginResponsePacket.setUserName(msg.getUserName());
            logUserInfo(session);

            System.out.println(new Date() + ": 登录成功!");
        } else {
            loginResponsePacket.setReason("账号密码校验失败");
            loginResponsePacket.setSuccess(false);
            System.out.println(new Date() + ": 登录失败!");
        }

        // 登录响应
        ctx.channel().writeAndFlush(loginResponsePacket);
    }

    private boolean valid(LoginRequestPacket requestPacket) {
        return true;
    }

    private String randomUserKey() {
        return UUID.randomUUID().toString()
                .replace("-", "");
    }

    private void logUserInfo(Session session){
        System.out.println("[ userid:"+session.getUserId()+" userName:"+session.getUserName()+" ]");
    }

    /**
     * 管理当连接关闭的情况
     * */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        SessionMap.unBindSession(ctx.channel());
    }
}
