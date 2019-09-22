import org.junit.jupiter.api.Test;
import redis.clients.jedis.Jedis;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class URLShortenTest {

    @Test
    public void testURLShorten() {
        Jedis client = new Jedis();
        client.flushAll();
        URLShorten urlShorten = new URLShorten(client);

        String shortId = urlShorten.shorten("https://github.com/yanglbme");

        assertEquals("100000", shortId);
        String sourceUrl = urlShorten.restore(shortId);
        assertEquals("https://github.com/yanglbme", sourceUrl);

        shortId = urlShorten.shorten("https://doocs.github.io");
        assertEquals("100001", shortId);
    }
}
