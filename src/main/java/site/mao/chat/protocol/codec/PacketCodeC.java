package site.mao.chat.protocol.codec;

import io.netty.buffer.ByteBuf;
import site.mao.chat.protocol.packet.*;
import site.mao.chat.protocol.Command;
import site.mao.chat.protocol.Packet;
import site.mao.chat.protocol.serializer.Serializer;
import site.mao.chat.protocol.serializer.SerializerAlgorithm;
import site.mao.chat.protocol.serializer.impl.JSONSerializer;

import java.util.HashMap;
import java.util.Map;

public class PacketCodeC {

    public static final PacketCodeC INSTANCE = new PacketCodeC();

    public static final int MAGIC_NUMBER = 0x12345678;

    private static final Map<Byte,Class<? extends Packet>> packetTypeMap;

    private static final Map<Byte, Serializer> serializerMap;

    static {

        packetTypeMap = new HashMap<>();

        //TODO 需要优化逻辑
        packetTypeMap.put(Command.LOGIN_REQUEST,LoginRequestPacket.class);
        packetTypeMap.put(Command.LOGIN_RESPONSE, LoginResponsePacket.class);
        packetTypeMap.put(Command.MESSAGE_REQUEST, MessageRequestPacket.class);
        packetTypeMap.put(Command.MESSAGE_RESPONSE, MessageResponsePacket.class);
        packetTypeMap.put(Command.CREATE_GROUP_REQUEST, CreateGroupRequestPacket.class);
        packetTypeMap.put(Command.CREATE_GROUP_RESPONSE, CreateGroupResponsePacket.class);
        packetTypeMap.put(Command.JOIN_GROUP_REQUEST,JoinGroupRequestPacket.class);
        packetTypeMap.put(Command.JOIN_GROUP_RESPONSE,JoinGroupResponsePacket.class);
        packetTypeMap.put(Command.QUIT_GROUP_REQUEST,QuitGroupRequestPacket.class);
        packetTypeMap.put(Command.QUIT_GROUP_RESPONSE,QuitGroupResponsePacket.class);
        packetTypeMap.put(Command.GROUP_MESSAGE_REQUEST,GroupMessageRequest.class);
        packetTypeMap.put(Command.GROUP_MESSAGE_RESPONSE,GroupMessageResponse.class);
        packetTypeMap.put(Command.LOGOUT_REQUEST,LogoutRequestPacket.class);
        packetTypeMap.put(Command.LOGOUT_RESPONSE,LogoutResponsePacket.class);
        packetTypeMap.put(Command.HEART_BEAT,HeartBeatRequestPacket.class);

        serializerMap = new HashMap<>();
        serializerMap.put(SerializerAlgorithm.JSON, new JSONSerializer());
    }

    public ByteBuf encode(ByteBuf byteBuf,Packet packet){

        byte[] bytes = Serializer.DEFAULT.serialize(packet);

        byteBuf.writeInt(MAGIC_NUMBER);

        byteBuf.writeByte(packet.getVersion());

        byteBuf.writeByte(Serializer.DEFAULT.getSerializerAlgorithm());

        byteBuf.writeByte(packet.getCommandType());

        byteBuf.writeInt(bytes.length);

        byteBuf.writeBytes(bytes);

        return byteBuf;

    }

    public Packet decode(ByteBuf byteBuf) {

        // 跳过 magic number
        byteBuf.skipBytes(4);

        // 跳过版本号
        byteBuf.skipBytes(1);

        // 序列化算法
        byte serializeAlgorithm = byteBuf.readByte();

        // 指令
        byte command = byteBuf.readByte();

        // 数据包长度
        int length = byteBuf.readInt();

        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);

        Class<? extends Packet> requestType = getRequestType(command);
        Serializer serializer = getSerializer(serializeAlgorithm);

        if (requestType != null && serializer != null) {
            return serializer.deserialize(requestType, bytes);
        }

        return null;
    }

    private Serializer getSerializer(byte serializeAlgorithm){
        return serializerMap.get(serializeAlgorithm);
    }

    private Class<? extends Packet> getRequestType(byte command){
        return packetTypeMap.get(command);
    }
}
