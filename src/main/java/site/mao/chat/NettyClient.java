package site.mao.chat;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import site.mao.chat.client.handler.*;
import site.mao.chat.console.ConsoleCommandManager;
import site.mao.chat.protocol.codec.PackageEncoder;
import site.mao.chat.protocol.codec.PacketCodecHandler;
import site.mao.chat.protocol.packet.LoginRequestPacket;
import site.mao.chat.server.handler.IMIdleStateHandler;
import site.mao.chat.session.SessionMap;

import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class NettyClient {

    private static Lock lock = new ReentrantLock();

    private static Condition waitCondition = lock.newCondition();

    private static AtomicBoolean hasLogin = new AtomicBoolean(false);

    private static int MAX_RETRY_TIMES = 5;

    private static ConsoleCommandManager consoleCommandManager;

    public static void main(String[] args) {

        consoleCommandManager = new ConsoleCommandManager();

        NioEventLoopGroup group = new NioEventLoopGroup();


        Bootstrap bootstrap = new Bootstrap();

        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline()
                                .addLast(new HeartBeatTimeHandler())
                                .addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 7, 4))
                                .addLast(new PacketCodecHandler())
                                .addLast(new LoginChannelHandler(hasLogin))
                                .addLast(new MessageResponseHandler())
                                .addLast(new PackageEncoder());


                    }
                });

        connect(bootstrap,"localhost",10001,5);
    }

    private static void connect(Bootstrap bootstrap,String host,int port,int retryTimes){
        bootstrap.connect(host,port).addListener(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> future) throws Exception {
                if (future.isSuccess()){
                    System.out.println("连接成功！");
                    //获取Channel
                    Channel channel = ((ChannelFuture)future).channel();
                    startConsoleThread(channel);
                }else{
                    int order = (MAX_RETRY_TIMES - retryTimes)+1;
                    int delay = 1<<order;
                    System.err.println(new Date()+":连接失败，第"+order+"次重连...");
                    bootstrap.config().group()
                            .schedule(()->connect(bootstrap, host, port, retryTimes-1),delay, TimeUnit.SECONDS);
                }
            }
        });
    }

    /**
     * 用户输入线程
     * */
    private static void startConsoleThread(Channel channel){

        new Thread(()->{
            Scanner sc = new Scanner(System.in);

            while (!Thread.interrupted()) {
                if (SessionMap.hasLogin(channel)) {
                    consoleCommandManager.exec(sc,channel);
                }else {
                    LoginRequestPacket loginRequestPacket = new LoginRequestPacket();
                    System.out.print("输入用户名登录: ");
                    String username = sc.nextLine();
                    loginRequestPacket.setUserName(username);

                    // 密码使用默认的
                    loginRequestPacket.setPassword("pwd");

                    // 发送登录数据包
                    channel.writeAndFlush(loginRequestPacket);

                    //等待登陆成功响应
                    waitForLoginResponse();
                }
            }
        }).start();
    }

    /**
     * 等候最多5s
     * */
    private static void waitForLoginResponse() {

        int waitTimes = 10;
        lock.lock();
        while (!hasLogin.get()&&waitTimes>0){
            try {
                waitCondition.await(500,TimeUnit.MICROSECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                lock.unlock();
            }
            waitTimes--;
        }

        if (!hasLogin.get()){
            System.err.println("登陆超时");
        }

    }

}
