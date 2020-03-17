package site.mao.chat.protocol.packet;

import lombok.Data;
import site.mao.chat.protocol.Command;
import site.mao.chat.protocol.Packet;

@Data
public class LoginResponsePacket extends Packet {

    private boolean success;

    private String reason;

    private String userId;

    String userName;


    @Override
    public Byte getCommandType() {
        return Command.LOGIN_RESPONSE;
    }
}
