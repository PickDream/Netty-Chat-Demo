package site.mao.chat.protocol.packet;

import lombok.Data;
import site.mao.chat.protocol.Command;
import site.mao.chat.protocol.Packet;

@Data
public class LogoutResponsePacket extends Packet {
    private boolean success;
    @Override
    public Byte getCommandType() {
        return Command.LOGOUT_RESPONSE;
    }
}
