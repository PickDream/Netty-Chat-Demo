package site.mao.chat.utils;

import java.util.UUID;

public final class IDUtil {
    public static String randomId(){
        return UUID.randomUUID().toString().split("-")[0];
    }
}
