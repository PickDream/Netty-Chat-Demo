package site.mao.chat.protocol.packet;

import lombok.Data;
import site.mao.chat.protocol.Command;
import site.mao.chat.protocol.Packet;

@Data
public class JoinGroupRequestPacket extends Packet {

    String groupId;

    @Override
    public Byte getCommandType() {
        return Command.JOIN_GROUP_REQUEST;
    }
}
