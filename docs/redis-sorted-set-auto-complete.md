# 利用 Redis Sorted Set 实现自动补全

## 代码实现
| [Python](#Python-版本) | [Java](#Java-版本) |
|---|---|

### Python 版本
```python
from redis import Redis


class AutoComplete:
    def __init__(self, client: Redis):
        self.client = client

    def feed(self, keyword: str, weight):
        for i in range(1, len(keyword)):
            key = 'auto_complete:' + keyword[:i]
            self.client.zincrby(key, weight, keyword)

    def hint(self, prefix: str, count=10):
        key = 'auto_complete:' + prefix
        return self.client.zrevrange(key, 0, count - 1)


if __name__ == '__main__':
    redis = Redis(decode_responses=True)
    redis.flushall()
    ac = AutoComplete(redis)

    ac.feed('张艺兴', 5000)
    ac.feed('张艺谋', 3000)
    ac.feed('张三', 500)

    print(ac.hint('张'))  # ['张艺兴', '张艺谋', '张三']
    print(ac.hint('张艺'))  # ['张艺兴', '张艺谋']
```

### Java 版本
```java
import redis.clients.jedis.Jedis;

import java.util.Set;

public class AutoComplete {
    private Jedis client = new Jedis();

    public AutoComplete() {

    }

    /**
     * 根据关键词设置分词权重
     *
     * @param keyword 关键词
     * @param weight  权重
     */
    public void feed(String keyword, double weight) {
        int len = keyword.length();
        for (int i = 1; i < len; ++i) {
            String key = "auto_complete:" + keyword.substring(0, i);
            client.zincrby(key, weight, keyword);
        }
    }

    /**
     * 根据前缀获取自动补全的结果
     *
     * @param prefix 前缀
     * @param count  数量
     * @return 结果集合
     */
    public Set<String> hint(String prefix, long count) {
        String key = "auto_complete:" + prefix;
        return client.zrevrange(key, 0, count - 1);
    }

    public static void main(String[] args) {
        AutoComplete ac = new AutoComplete();
        ac.feed("张艺兴", 5000);
        ac.feed("张艺谋", 3000);
        ac.feed("张三", 500);

        System.out.println(ac.hint("张", 10)); // [张艺兴, 张艺谋, 张三]
        System.out.println(ac.hint("张艺", 10)); // [张艺兴, 张艺谋]
    }
}
```