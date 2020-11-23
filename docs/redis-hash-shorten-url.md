# 利用 Redis 哈希实现短网址

## 代码实现

| [Python](#Python-版本) | [Java](#Java-版本) |
| ---------------------- | ------------------ |

### Python 版本

```python
from redis import Redis


# 10进制数转换为36进制字符串
def base10_to_base36(number: int) -> str:
    alphabets = '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ'
    result = ''
    while number != 0:
        number, i = divmod(number, 36)
        result = alphabets[i] + result

    return result or alphabets[0]


URL_HASH_SHORT_SOURCE_KEY = 'url_hash:short_source'
ID_COUNTER = 'short_url:id_counter'


class URLShorten:

    def __init__(self, client: Redis):
        self.client = client
        # 设置初始ID值，保留1-5位的短码，从6位的短码开始生成
        self.client.set(ID_COUNTER, 36 ** 5 - 1)

    def shorten(self, source_url: str) -> str:
        """对源网址进行缩短，返回短网址ID"""
        new_id = self.client.incr(ID_COUNTER)
        short_id = base10_to_base36(new_id)
        self.client.hset(URL_HASH_SHORT_SOURCE_KEY, short_id, source_url)
        return short_id

    def restore(self, short_id: str) -> str:
        """根据短网址ID，返回对应的源网址"""
        return self.client.hget(URL_HASH_SHORT_SOURCE_KEY, short_id)


if __name__ == '__main__':
    redis = Redis(decode_responses=True)
    url_shorten = URLShorten(redis)

    short_id = url_shorten.shorten('https://github.com/yanglbme')
    print(short_id)  # 100000
    source_url = url_shorten.restore(short_id)
    print(source_url)  # https://github.com/yanglbme

    print(url_shorten.shorten('https://doocs.github.io'))  # 100001
```

### Java 版本

```java
import redis.clients.jedis.Jedis;

public class URLShorten {
    private Jedis client;

    private final String URL_HASH_SHORT_SOURCE_KEY = "url_hash:short_source";
    private final String ID_COUNTER = "short_url:id_counter";

    /**
     * 将10进制数转换为36进制字符串
     *
     * @param number 10进制数
     * @return 36进制字符串
     */
    private String base10ToBase36(long number) {
        String alphabets = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        char[] chars = alphabets.toCharArray();
        StringBuilder result = new StringBuilder();
        while (number != 0) {
            int i = (int) number % 36;
            number /= 36;
            result.insert(0, chars[i]);
        }
        return result.toString();
    }

    public URLShorten(Jedis client) {
        this.client = client;
        // 设置初始ID值，保留1-5位的短码，从6位的短码开始生成
        this.client.set(ID_COUNTER, String.valueOf((long) Math.pow(36, 5) - 1));
    }

    /**
     * 对源网址进行缩短，返回短网址ID
     *
     * @param sourceUrl 源网址
     * @return 短网址ID
     */
    public String shorten(String sourceUrl) {
        long newId = client.incr(ID_COUNTER);
        String shortId = base10ToBase36(newId);
        client.hset(URL_HASH_SHORT_SOURCE_KEY, shortId, sourceUrl);
        return shortId;
    }

    /**
     * 根据短网址ID，返回对应的源网址
     *
     * @param shortId 短网址ID
     * @return 源网址
     */
    public String restore(String shortId) {
        return client.hget(URL_HASH_SHORT_SOURCE_KEY, shortId);
    }

    public static void main(String[] args) {
        Jedis client = new Jedis();
        URLShorten urlShorten = new URLShorten(client);

        String shortId = urlShorten.shorten("https://github.com/yanglbme");
        System.out.println(shortId); // 100000
        String sourceUrl = urlShorten.restore(shortId);
        System.out.println(sourceUrl); // https://github.com/yanglbme

        System.out.println(urlShorten.shorten("https://doocs.github.io")); // 100001

    }
}
```
