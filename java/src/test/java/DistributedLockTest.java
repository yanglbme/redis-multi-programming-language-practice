import org.junit.jupiter.api.Test;
import redis.clients.jedis.Jedis;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DistributedLockTest {

    @Test
    public void testDistributedLock() throws InterruptedException {
        Jedis jedis = new Jedis();
        jedis.flushAll();

        DistributedLock lock = new DistributedLock(jedis, "bingo_distributed_lock");
        assertTrue(lock.acquire(10));
        assertFalse(lock.acquire(10));

        TimeUnit.SECONDS.sleep(10);

        assertTrue(lock.acquire());
        assertTrue(lock.release());

        assertTrue(lock.acquire());
    }
}
