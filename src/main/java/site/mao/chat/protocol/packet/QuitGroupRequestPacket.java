package site.mao.chat.protocol.packet;

import lombok.Data;
import site.mao.chat.protocol.Command;
import site.mao.chat.protocol.Packet;

@Data
public class QuitGroupRequestPacket extends Packet {
    String groupId;
    @Override
    public Byte getCommandType() {
        return Command.QUIT_GROUP_REQUEST;
    }
}
