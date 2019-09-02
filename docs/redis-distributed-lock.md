# 使用 Redis String 实现分布式锁


## 代码实现
1. Python 版本

```python
import random
import string
import time

from redis import Redis


def generate_random_value():
    return ''.join(random.sample(string.ascii_letters + string.digits, 32))


# 设定默认过期时长为 10s
DEFAULT_TIMEOUT = 10


class DistributedLock:
    def __init__(self, client: Redis, key: str):
        self.client = client
        self.key = key

    def acquire(self, timeout=DEFAULT_TIMEOUT) -> bool:
        """尝试获取锁"""
        res = self.client.set(self.key, generate_random_value(), ex=timeout, nx=True)  # ex 表示过期时长秒数，nx 表示 key 不存在时才设置
        return res is True

    def release(self) -> bool:
        """尝试释放锁"""
        return self.client.delete(self.key) == 1


if __name__ == '__main__':
    redis = Redis(decode_responses=True)
    lock = DistributedLock(redis, 'bingo_distributed_lock')

    print(lock.acquire(10))  # True
    print(lock.acquire(10))  # False

    time.sleep(10)
    print(lock.acquire())  # True
    print(lock.release())  # True

    print(lock.acquire())  # True
```

2. Java 版本

```java
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

import java.util.UUID;
import java.util.concurrent.TimeUnit;


public class DistributedLock {

    private Jedis client;
    private String key;

    public DistributedLock(Jedis client, String key) {
        this.client = client;
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

    public static void main(String[] args) throws InterruptedException {
        Jedis jedis = new Jedis();
        DistributedLock lock = new DistributedLock(jedis, "bingo_distributed_lock");

        System.out.println(lock.acquire(10)); // true
        System.out.println(lock.acquire(10)); // false

        TimeUnit.SECONDS.sleep(10);
        System.out.println(lock.acquire()); // true
        System.out.println(lock.release()); // true

        System.out.println(lock.acquire()); // true
    }
}
```