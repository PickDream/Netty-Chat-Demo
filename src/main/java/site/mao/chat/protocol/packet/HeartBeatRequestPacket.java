package site.mao.chat.protocol.packet;

import site.mao.chat.protocol.Command;
import site.mao.chat.protocol.Packet;

public class HeartBeatRequestPacket extends Packet {

    @Override
    public Byte getCommandType() {
        return Command.HEART_BEAT;
    }
}
