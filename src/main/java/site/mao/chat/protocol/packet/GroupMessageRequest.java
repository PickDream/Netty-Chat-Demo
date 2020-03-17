package site.mao.chat.protocol.packet;

import lombok.Data;
import site.mao.chat.protocol.Command;
import site.mao.chat.protocol.Packet;

@Data
public class GroupMessageRequest extends Packet {
    private String message;

    private String groupId;

    @Override
    public Byte getCommandType() {
        return Command.GROUP_MESSAGE_REQUEST;
    }
}
