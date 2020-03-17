package site.mao.chat.protocol.packet;

import lombok.Data;
import site.mao.chat.protocol.Command;
import site.mao.chat.protocol.Packet;

@Data
public class LoginRequestPacket extends Packet {

    private Integer userId;

    private String userName;

    private String password;

    @Override
    public Byte getCommandType() {
        return Command.LOGIN_REQUEST;
    }

}
