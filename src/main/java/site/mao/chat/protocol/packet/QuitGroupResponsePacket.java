package site.mao.chat.protocol.packet;

import lombok.Data;
import site.mao.chat.protocol.Command;
import site.mao.chat.protocol.Packet;

@Data
public class QuitGroupResponsePacket extends Packet {

    private boolean success;

    private String groupId;
    @Override
    public Byte getCommandType() {
        return Command.QUIT_GROUP_RESPONSE;
    }
}
