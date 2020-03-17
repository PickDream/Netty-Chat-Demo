package site.mao.chat.attributes;

import io.netty.util.AttributeKey;
import site.mao.chat.session.Session;

public interface Attributes {
    AttributeKey<Session> SESSION = AttributeKey.newInstance("session");
}