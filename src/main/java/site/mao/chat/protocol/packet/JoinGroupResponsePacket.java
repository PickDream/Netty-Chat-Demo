package site.mao.chat.protocol.packet;

import lombok.Data;
import site.mao.chat.protocol.Command;
import site.mao.chat.protocol.Packet;

@Data
public class JoinGroupResponsePacket extends Packet {

    boolean success;

    String groupId;

    @Override
    public Byte getCommandType() {
        return Command.JOIN_GROUP_RESPONSE;
    }
}
