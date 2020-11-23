# 利用 Redis 列表实现页面数据分页

## 代码实现

| [Python](#Python-版本) | [Java](#Java-版本) |
| ---------------------- | ------------------ |

### Python 版本

```python
from redis import Redis


class Paginate:
    def __init__(self, client: Redis, key: str):
        self.client = client
        self.key = key

    def add(self, item):
        """添加元素到分页列表中"""
        self.client.lpush(self.key, item)

    def get_page(self, page: int, per_page: int):
        """根据页码取出指定数量的元素"""
        start = (page - 1) * per_page
        end = page * per_page - 1
        return self.client.lrange(self.key, start, end)

    def get_size(self):
        """获取列表的元素数量"""
        return self.client.llen(self.key)


if __name__ == '__main__':
    redis = Redis(decode_responses=True)
    topics = Paginate(redis, 'user-topics')

    for i in range(24):
        topics.add(i)

    # 取总数
    print(topics.get_size())  # 24

    # 以每页5条数据的方式取出第1页数据
    print(topics.get_page(1, 5))  # ['23', '22', '21', '20', '19']

    # 以每页10条数据的方式取出第1页数据
    print(topics.get_page(1, 10))  # ['23', '22', '21', '20', '19', '18', '17', '16', '15', '14']

    # 以每页5条数据的方式取出第5页数据
    print(topics.get_page(5, 5))  # ['3', '2', '1', '0']

```

### Java 版本

```java
import redis.clients.jedis.Jedis;

import java.util.List;

public class Paginate {

    private Jedis client;
    private String key;

    public Paginate(Jedis client, String key) {
        this.client = client;
        this.key = key;
    }

    /**
     * 添加元素到分页列表中
     *
     * @param item 元素
     */
    public void add(String item) {
        client.lpush(key, item);
    }

    /**
     * 根据页码取出指定数量的元素
     *
     * @param page     页码
     * @param pageSize 数量
     * @return 元素列表
     */
    public List<String> getPage(int page, int pageSize) {
        long start = (page - 1) * pageSize;
        long end = page * pageSize - 1;
        return client.lrange(key, start, end);
    }

    /**
     * 获取列表的元素数量
     *
     * @return 总数
     */
    public Long getTotalCount() {
        return client.llen(key);
    }

    public static void main(String[] args) {
        Jedis client = new Jedis();
        Paginate topics = new Paginate(client, "user-topics");
        for (int i = 0; i < 24; ++i) {
            topics.add(i + "");
        }

        // 取总数
        System.out.println(topics.getTotalCount()); // 24

        // 以每页5条数据的方式取出第1页数据
        System.out.println(topics.getPage(1, 5)); // [23, 22, 21, 20, 19]

        // 以每页10条数据的方式取出第1页数据
        System.out.println(topics.getPage(1, 10)); // [23, 22, 21, 20, 19, 18, 17, 16, 15, 14]

        // 以每页5条数据的方式取出第5页数据
        System.out.println(topics.getPage(5, 5)); // [3, 2, 1, 0]
    }
}
```
