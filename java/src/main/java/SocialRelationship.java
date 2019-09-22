import redis.clients.jedis.Jedis;
import utils.JedisUtils;

import java.util.Set;

public class SocialRelationship {
    private Jedis client = JedisUtils.getClient();
    private String user;

    public SocialRelationship(String user) {
        this.user = user;
    }

    /**
     * 关注目标用户
     *
     * @param target 用户
     */
    public void follow(String target) {
        String followingKey = JedisUtils.getFollowingKey(user);
        String followersKey = JedisUtils.getFollowersKey(target);
        long now = System.currentTimeMillis();
        client.zadd(followingKey, now, target);
        client.zadd(followersKey, now, user);
    }

    /**
     * 取关目标用户
     *
     * @param target 用户
     */
    public void unfollow(String target) {
        String followingKey = JedisUtils.getFollowingKey(user);
        String followersKey = JedisUtils.getFollowersKey(target);
        client.zrem(followingKey, target);
        client.zrem(followersKey, user);
    }

    /**
     * 判断是否关注着目标用户
     *
     * @param target 用户
     * @return 是否关注
     */
    public boolean isFollowing(String target) {
        String followingKey = JedisUtils.getFollowingKey(user);
        return client.zrank(followingKey, target) != null;

    }

    /**
     * 获取当前用户关注的所有人，并按最近关注时间倒序排列
     *
     * @return 关注人集合
     */
    public Set<String> getAllFollowing() {
        String followingKey = JedisUtils.getFollowingKey(user);
        return client.zrevrange(followingKey, 0, -1);
    }

    /**
     * 获取当前用户的所有粉丝，并按最近关注时间倒序排列
     *
     * @return 粉丝集合
     */
    public Set<String> getAllFollowers() {
        String followersKey = JedisUtils.getFollowersKey(user);
        return client.zrevrange(followersKey, 0, -1);
    }

    /**
     * 获取当前用户关注的人数
     *
     * @return 人数
     */
    public Long countFollowing() {
        String followingKey = JedisUtils.getFollowingKey(user);
        return client.zcard(followingKey);
    }

    /**
     * 获取当前用户的粉丝数
     *
     * @return 人数
     */
    public Long countFollowers() {
        String followersKey = JedisUtils.getFollowersKey(user);
        return client.zcard(followersKey);
    }

    /**
     * 获取与某用户的共同关注
     *
     * @param one 用户
     * @return 共同关注的用户集合
     */
    public Set<String> getCommonFollowing(String one) {
        String commonKey = JedisUtils.getCommonFollowingKey(user, one);
        client.zinterstore(commonKey, JedisUtils.getFollowingKey(user), JedisUtils.getFollowingKey(one));
        return client.zrevrange(commonKey, 0, -1);
    }
}