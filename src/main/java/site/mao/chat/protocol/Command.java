package site.mao.chat.protocol;

public interface Command {

    Byte LOGIN_REQUEST = 1;

    Byte LOGIN_RESPONSE = 2;

    Byte MESSAGE_REQUEST = 3;

    Byte MESSAGE_RESPONSE = 4;

    Byte CREATE_GROUP_REQUEST = 5;

    Byte CREATE_GROUP_RESPONSE = 6;

    Byte JOIN_GROUP_REQUEST = 7;

    Byte JOIN_GROUP_RESPONSE = 8;

    Byte QUIT_GROUP_REQUEST = 9;

    Byte QUIT_GROUP_RESPONSE = 10;

    Byte GROUP_MEMBER_REQUEST = 11;

    Byte GROUP_MEMBER_RESPONSE = 12;

    Byte GROUP_MESSAGE_REQUEST = 13;

    Byte GROUP_MESSAGE_RESPONSE = 14;

    Byte LOGOUT_REQUEST = 15;

    Byte LOGOUT_RESPONSE = 16;

    Byte HEART_BEAT = 17;


}
