package site.mao.chat.protocol.packet;

import lombok.Data;
import site.mao.chat.protocol.Command;
import site.mao.chat.protocol.Packet;

import java.util.List;

@Data
public class LogoutRequestPacket extends Packet {

    private List<String> joinedGroup;

    @Override
    public Byte getCommandType() {
        return Command.LOGOUT_REQUEST;
    }
}
