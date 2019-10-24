import redis.clients.jedis.Jedis;
import utils.JedisUtils;

import java.util.Set;

public class AutoComplete {
    private Jedis client = JedisUtils.getClient();

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
}
