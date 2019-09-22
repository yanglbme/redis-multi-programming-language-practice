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
}