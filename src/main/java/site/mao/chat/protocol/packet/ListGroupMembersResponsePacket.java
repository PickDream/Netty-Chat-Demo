package site.mao.chat.protocol.packet;

import lombok.Data;
import site.mao.chat.protocol.Command;
import site.mao.chat.protocol.Packet;
import site.mao.chat.session.Session;

import java.util.List;

@Data
public class ListGroupMembersResponsePacket extends Packet {
    private String groupId;
    private List<Session> sessionList;
    @Override
    public Byte getCommandType() {
        return Command.GROUP_MEMBER_RESPONSE;
    }
}
