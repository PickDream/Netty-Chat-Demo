package site.mao.chat.client.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import site.mao.chat.protocol.packet.LoginRequestPacket;
import site.mao.chat.protocol.packet.LoginResponsePacket;
import site.mao.chat.session.Session;
import site.mao.chat.session.SessionMap;

import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

public class LoginChannelHandler extends SimpleChannelInboundHandler<LoginResponsePacket> {

    private AtomicBoolean isLoginRef;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginResponsePacket loginResponsePacket) throws Exception {
        if (loginResponsePacket.isSuccess()) {
            isLoginRef.compareAndSet(false,true);
            String userId = loginResponsePacket.getUserId();
            String userName = loginResponsePacket.getUserName();
            System.out.println(new Date() + ": 客户端登录成功");
            SessionMap.bindSession(new Session(userId,userName),ctx.channel());
        } else {
            System.out.println(new Date() + ": 客户端登录失败，原因：" + loginResponsePacket.getReason());
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端连接被关闭");
    }

    public LoginChannelHandler(AtomicBoolean isLoginRef){
        super();
        this.isLoginRef = isLoginRef;
    }

}
