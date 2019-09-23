# 利用 Redis Set 实现点赞点踩功能

## 代码实现
| [Python](#Python-版本) | [Java](#Java-版本) |
|---|---|

### Python 版本
```python
from redis import Redis


def get_like_key(entity_id):
    return 'like:{}'.format(entity_id)


def get_dislike_key(entity_id):
    return 'dislike:{}'.format(entity_id)


class Like:
    def __init__(self, client: Redis, entity_id: str):
        self.client = client
        self.entity_id = entity_id

    def like(self, user_id) -> int:
        """某用户点赞"""
        self.client.sadd(get_like_key(self.entity_id), user_id)
        self.client.srem(get_dislike_key(self.entity_id), user_id)
        return self.client.scard(get_like_key(self.entity_id))

    def dislike(self, user_id) -> int:
        """某用户点踩"""
        self.client.sadd(get_dislike_key(self.entity_id), user_id)
        self.client.srem(get_like_key(self.entity_id), user_id)
        return self.client.scard(get_like_key(self.entity_id))

    def get_like_status(self, user_id) -> int:
        """判断用户是否点赞或点踩了，点赞返回1，点踩返回-1，不赞不踩返回0"""
        if self.client.sismember(get_like_key(self.entity_id), user_id):
            return 1
        if self.client.sismember(get_dislike_key(self.entity_id), user_id):
            return -1
        return 0

    def get_like_count(self) -> int:
        """获取当前点赞数"""
        return self.client.scard(get_like_key(self.entity_id))


if __name__ == '__main__':
    redis = Redis(decode_responses=True)
    redis.flushall()
    like_entity = Like(redis, 'user1')
    print(like_entity.get_like_count())  # 0
    like_entity.like('user2')
    like_entity.like('user3')

    like_entity.dislike('user4')

    print(like_entity.get_like_count())  # 2
    print(like_entity.get_like_status('user2'))  # 1
    print(like_entity.get_like_status('user4'))  # -1


```

### Java 版本
```java
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
    
    public static void main(String[] args){
        LikeService likeService = new LikeService();
        
        String entityId = "user1";
        System.out.println(likeService.getLikeCount(entityId)); // 0

        likeService.like("user2", entityId);
        likeService.like("user3", entityId);

        likeService.dislike("user4", entityId);

        System.out.println(likeService.getLikeCount(entityId)); // 2
        System.out.println(likeService.getLikeStatus("user2", entityId)); // 1

        System.out.println(likeService.getLikeStatus("user4", entityId)); // -1
    }
}

```