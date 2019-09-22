package utils;

import redis.clients.jedis.Jedis;

public class JedisUtils {

    public static Jedis getClient() {
        Jedis client = new Jedis();
        client.flushAll();
        return client;
    }

    public static String getFollowingKey(String user) {
        return "following:" + user;
    }

    public static String getFollowersKey(String user) {
        return "followers:" + user;
    }

    public static String getCommonFollowingKey(String user1, String user2) {
        return String.format("common:following:%s:%s", user1, user2);
    }
}
