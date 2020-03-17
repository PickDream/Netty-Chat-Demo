package site.mao.chat.session;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import site.mao.chat.attributes.Attributes;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public final class SessionMap {

    private static final Map<String, Channel> userIdChennelMap = new ConcurrentHashMap<>();

    private static final Map<String, ChannelGroup> groupIdChannelGroupMap = new ConcurrentHashMap<>();

    public static void bindSession(Session session,Channel channel){
        userIdChennelMap.put(session.getUserId(),channel);
        channel.attr(Attributes.SESSION).set(session);
    }

    public static void unBindSession(Channel channel) {
        if (hasLogin(channel)) {
            userIdChennelMap.remove(getSession(channel).getUserId());
            channel.attr(Attributes.SESSION).set(null);
        }
    }

    public static boolean hasLogin(Channel channel) {

        return channel.hasAttr(Attributes.SESSION);
    }

    public static Session getSession(Channel channel) {

        return channel.attr(Attributes.SESSION).get();
    }

    public static Channel getChannel(String userId) {

        return userIdChennelMap.get(userId);
    }

    public static void bindChannelGroup(String groupId, ChannelGroup channelGroup) {
        groupIdChannelGroupMap.put(groupId, channelGroup);
    }

    public static ChannelGroup getChannelGroup(String groupId) {
        return groupIdChannelGroupMap.get(groupId);
    }

    /**
     * TODO 逻辑依然可以优化
     * */
    public static void removeChannel(Channel channel){
        //删除Group中的Channel
        List<String> emptyChannelGroupKeys = groupIdChannelGroupMap.entrySet().stream()
                .map(entry-> {
                    entry.getValue().remove(channel);
                    return entry;
                }).filter(entry->entry.getValue().isEmpty())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        for (String emptyKey:emptyChannelGroupKeys){
            groupIdChannelGroupMap.remove(emptyKey);
        }

        String userId = getSession(channel).getUserId();

        userIdChennelMap.remove(userId);

    }
}
