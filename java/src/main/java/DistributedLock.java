import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;
import utils.JedisUtils;

import java.util.UUID;

public class DistributedLock {

    private Jedis client = JedisUtils.getClient();
    private String key;

    public DistributedLock(String key) {
        this.key = key;
    }

    /**
     * 获取随机字符串值
     *
     * @return 随机字符串
     */
    private String generateRandomValue() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 尝试获取锁
     *
     * @param timeout 过期时长
     * @return 是否获取成功
     */
    public boolean acquire(int timeout) {
        SetParams params = new SetParams().ex(timeout).nx();
        return client.set(key, generateRandomValue(), params) != null;
    }

    public boolean acquire() {
        int timeout = 10;
        return acquire(timeout);
    }

    /**
     * 尝试释放锁
     *
     * @return 是否成功释放锁
     */
    public boolean release() {
        return client.del(key) == 1;
    }
}