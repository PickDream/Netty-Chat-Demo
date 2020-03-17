package site.mao.chat.console;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import site.mao.chat.protocol.codec.PacketCodeC;
import site.mao.chat.protocol.packet.MessageRequestPacket;

import java.util.Scanner;

public class SendToUserConsoleCommand implements ConsoleCommand{

    @Override
    public void exec(Scanner scanner, Channel channel) {

        System.out.println("输入用户Id");
        String userId = scanner.nextLine();
        System.out.println("输入消息发送至服务端: ");
        String msg = scanner.nextLine();

        MessageRequestPacket packet = new MessageRequestPacket();
        packet.setToUserId(userId);
        packet.setMessage(msg);

        channel.writeAndFlush(packet);
    }

    @Override
    public String getCommandName() {
        return "s2u";
    }
}
