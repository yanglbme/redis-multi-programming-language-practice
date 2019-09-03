# 利用 Redis Sorted Set 实现粉丝关注功能

为什么选用 Sorted Set？其点有三：

- Sorted Set 有序，系统可以根据用户关注实体的时间倒序排列，获取最新的关注列表；
- Sorted Set 去重，用户不能重复关注同一个实体；
- Sorted Set 可以获取两用户之间的共同关注。

## 代码实现
1. Python 版本

```python
from time import time

from redis import Redis


def following_key(user):
    return 'following:' + user


def followers_key(user):
    return 'followers:' + user


def common_following_key(user1, user2):
    return 'common:following:{}:{}'.format(user1, user2)


class SocialRelationship:
    def __init__(self, client: Redis, user):
        self.client = client
        self.user = user

    def follow(self, target):
        """关注目标用户"""
        self.client.zadd(following_key(self.user), {target: time()})
        self.client.zadd(followers_key(target), {self.user: time()})

    def unfollow(self, target):
        """取关目标用户"""
        self.client.zrem(following_key(self.user), target)
        self.client.zrem(followers_key(target), self.user)

    def is_following(self, target):
        """判断是否关注着目标用户"""
        return self.client.zrank(following_key(self.user), target) is not None

    def get_all_following(self):
        """获取当前用户关注的所有人，并按最近关注时间倒序排列"""
        return self.client.zrevrange(following_key(self.user), 0, -1)

    def get_all_followers(self):
        """获取当前用户的所有粉丝，并按最近关注时间倒序排列"""
        return self.client.zrevrange(followers_key(self.user), 0, -1)

    def count_following(self):
        """获取当前用户关注的人数"""
        return self.client.zcard(following_key(self.user))

    def count_followers(self):
        """获取当前用户的粉丝数"""
        return self.client.zcard(followers_key(self.user))

    def get_common_following(self, one):
        """获取与某用户的共同关注"""
        common_key = common_following_key(self.user, one)
        self.client.zinterstore(common_key, (following_key(self.user), following_key(one)))
        return self.client.zrevrange(common_key, 0, -1)


if __name__ == '__main__':
    redis = Redis(decode_responses=True)
    bingo = SocialRelationship(redis, 'Bingo')
    iris = SocialRelationship(redis, 'Iris')
    bingo.follow('Iris')
    bingo.follow('GitHub')
    bingo.follow('Apple')
    iris.follow('Bingo')
    iris.follow('GitHub')

    print(bingo.is_following('Iris'))  # True
    print(bingo.get_all_following())  # ['Apple', 'GitHub', 'Iris']
    print(iris.is_following('Bingo'))  # True
    print(bingo.count_following())  # 3

    print(bingo.get_common_following('Iris'))  # ['GitHub']
```

2. Java 版本
- JedisUtils.java

```java
import redis.clients.jedis.Jedis;

public class JedisUtils {

    public static Jedis getClient() {
        return new Jedis();
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
```

- SocialRelationship.java

```java
import redis.clients.jedis.Jedis;

import java.util.Set;

public class SocialRelationship {
    private Jedis client;
    private String user;

    public SocialRelationship(Jedis client, String user) {
        this.client = client;
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

    public static void main(String[] args) {
        Jedis client = JedisUtils.getClient();
        SocialRelationship bingo = new SocialRelationship(client, "Bingo");
        SocialRelationship iris = new SocialRelationship(client, "Iris");
        bingo.follow("Iris");
        bingo.follow("GitHub");
        bingo.follow("Apple");
        iris.follow("Bingo");
        iris.follow("GitHub");

        System.out.println(bingo.isFollowing("Iris")); // true
        System.out.println(bingo.getAllFollowing());  // [Apple, GitHub, Iris]
        System.out.println(iris.isFollowing("Bingo")); // true
        System.out.println(bingo.countFollowing()); // 3

        System.out.println(bingo.getCommonFollowing("Iris")); // [GitHub]
    }
}
```