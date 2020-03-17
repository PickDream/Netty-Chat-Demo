package site.mao.chat.protocol.packet;

import lombok.Data;
import site.mao.chat.protocol.Command;
import site.mao.chat.protocol.Packet;

import java.util.List;

@Data
public class CreateGroupResponsePacket extends Packet {

    boolean success;

    String groupId;

    List<String> userNameList;

    @Override
    public Byte getCommandType() {
        return Command.CREATE_GROUP_RESPONSE;
    }
}
