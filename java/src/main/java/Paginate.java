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
}