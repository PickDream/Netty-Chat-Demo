package site.mao.chat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import site.mao.chat.protocol.codec.PacketCodecHandler;
import site.mao.chat.protocol.codec.Spliter;
import site.mao.chat.server.handler.*;
import site.mao.chat.server.handler.merged.MergedPacketHandler;

public class NettyServer {
    public static void main(String[] args) {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap
                .group(bossGroup,workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                        //nioSocketChannel.pipeline().addLast(new LoginServerHandler());
                        nioSocketChannel.pipeline()
                                .addLast(new IMIdleStateHandler())
                                /**
                                 * Spliter会从底层读数据校验是否已经是完整的一个协议数据包，
                                 * 如果不是的话会继续等数据，在这个过程中要保持某个channel的数据，
                                 * 所以是有状态的。
                                 * */
                                .addLast(new Spliter())//Spliter有状态
                                .addLast(PacketCodecHandler.INSTANCE)//编解码
                                .addLast(LoginRequestHandler.INSTANCE)
                                .addLast(HeartBeatRequestHandler.INSTANCE)
                                .addLast(AuthHandler.INSTANCE)
                                .addLast(MergedPacketHandler.INSTANCE);

                    }
                });

        bind(serverBootstrap,10000);
    }

    public static void bind(final ServerBootstrap sb,final int port){
        sb.bind(port).addListener(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> future) throws Exception {
                if (future.isSuccess()){
                    System.out.println("端口["+port+"]绑定成功!");
                }else {
                    System.err.println("端口[" + port + "]绑定失败!");
                    bind(sb,port+1);
                }
            }
        });
    }
}
