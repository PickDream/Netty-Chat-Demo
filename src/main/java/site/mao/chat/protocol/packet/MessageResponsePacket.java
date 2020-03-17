package site.mao.chat.protocol.packet;

import lombok.Data;
import site.mao.chat.protocol.Command;
import site.mao.chat.protocol.Packet;

@Data
public class MessageResponsePacket extends Packet {
    String fromUserId;
    String fromUserName;
    String message;
    @Override
    public Byte getCommandType() {
        return Command.MESSAGE_RESPONSE;
    }
}
