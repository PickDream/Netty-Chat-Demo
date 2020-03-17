package site.mao.chat.protocol.packet;

import lombok.Data;
import site.mao.chat.protocol.Command;
import site.mao.chat.protocol.Packet;

@Data
public class MessageRequestPacket extends Packet {

    //发送给的对象
    private String toUserId;

    //消息内容
    private String message;

    @Override
    public Byte getCommandType() {
        return Command.MESSAGE_REQUEST;
    }


}
