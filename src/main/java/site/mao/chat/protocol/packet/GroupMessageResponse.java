package site.mao.chat.protocol.packet;

import lombok.Data;
import site.mao.chat.protocol.Packet;
import site.mao.chat.session.Session;

@Data
public class GroupMessageResponse extends Packet{
    private Session senderSession;

    private String groupId;

    private String message;
    @Override
    public Byte getCommandType() {
        return null;
    }
}
