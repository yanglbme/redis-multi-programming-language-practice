import redis.clients.jedis.Jedis;

public class Redis {
    public static void main(String[] args) {
        Jedis client = new Jedis();
        client.set("author", "yanglbme");
        client.set("wechat_id", "YLB0109");
        client.set("link", "https://github.com/yanglbme");
        client.set("email", "contact@yanglibin.info");
        System.out.println("Hello, Redis");
    }
}