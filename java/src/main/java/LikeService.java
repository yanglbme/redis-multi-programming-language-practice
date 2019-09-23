import redis.clients.jedis.Jedis;
import utils.JedisUtils;

public class LikeService {
    private Jedis client = JedisUtils.getClient();

    public LikeService() {

    }

    private String getLikeKey(String entityId) {
        return "like:" + entityId;
    }

    private String getDislikeKey(String entityId) {
        return "dislike:" + entityId;
    }

    /**
     * 用户点赞了某个实体
     *
     * @param userId 用户id
     * @param entityId 实体id
     * @return 实体的点赞人数
     */
    public Long like(String userId, String entityId) {
        client.sadd(getLikeKey(entityId), userId);
        client.srem(getDislikeKey(entityId), userId);
        return client.scard(getLikeKey(entityId));
    }

    /**
     * 用户点踩了某个实体
     *
     * @param userId 用户id
     * @param entityId 实体id
     * @return 实体的点赞人数
     */
    public Long dislike(String userId, String entityId) {
        client.sadd(getDislikeKey(entityId), userId);
        client.srem(getLikeKey(entityId), userId);
        return client.scard(getLikeKey(entityId));
    }

    /**
     * 用户对某个实体的赞踩状态，点赞1，点踩-1，不赞不踩0
     *
     * @param userId 用户id
     * @param entityId 实体id
     * @return 赞踩状态
     */
    public int getLikeStatus(String userId, String entityId) {
        if (client.sismember(getLikeKey(entityId), userId)) {
            return 1;
        }
        if (client.sismember(getDislikeKey(entityId), userId)) {
            return -1;
        }
        return 0;
    }

    /**
     * 获取某实体的点赞人数
     *
     * @param entityId 实体id
     * @return 点赞人数
     */
    public Long getLikeCount(String entityId) {
        return client.scard(getLikeKey(entityId));
    }
}
