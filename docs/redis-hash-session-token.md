# 利用 Redis 哈希实现登录会话


## 代码实现
| [Python](#Python-版本) | [Java](#Java-版本) |
|---|---|

### Python 版本

```python
import os
from _sha256 import sha256
from time import time

from redis import Redis

# 会话默认过期时间，一个月
DEFAULT_TIMEOUT = 30 * 24 * 3600

# 会话 token 及过期时间的 key
SESSION_TOKEN_KEY = 'SESSION:TOKEN'
SESSION_EXPIRE_TS_KEY = 'SESSION:EXPIRE'

# 会话状态
SESSION_NOT_LOGIN = 'SESSION_NOT_LOGIN'
SESSION_EXPIRE = 'SESSION_EXPIRE'
SESSION_TOKEN_CORRECT = 'SESSION_TOKEN_CORRECT'
SESSION_TOKEN_INCORRECT = 'SESSION_TOKEN_INCORRECT'


def generate_token():
    """生成一个随机的会话令牌"""
    return sha256(os.urandom(256)).hexdigest()


class LoginSession:
    def __init__(self, client: Redis, user_id: str):
        self.client = client
        self.user_id = user_id

    def create(self, timeout=DEFAULT_TIMEOUT) -> str:
        """创建新的会话，并返回会话token"""
        session_token = generate_token()

        # 设置过期时间
        expire_time = time() + timeout
        self.client.hset(SESSION_TOKEN_KEY, self.user_id, session_token)
        self.client.hset(SESSION_EXPIRE_TS_KEY, self.user_id, expire_time)
        return session_token

    def validate(self, token) -> str:
        """校验token"""
        session_token = self.client.hget(SESSION_TOKEN_KEY, self.user_id)
        expire_time = self.client.hget(SESSION_EXPIRE_TS_KEY, self.user_id)

        if (session_token is None) or (expire_time is None):
            return SESSION_NOT_LOGIN

        # 将字符串类型的时间转换为浮点数类型
        if time() > float(expire_time):
            return SESSION_EXPIRE

        if session_token == token:
            return SESSION_TOKEN_CORRECT

        return SESSION_TOKEN_INCORRECT

    def destroy(self):
        """销毁会话"""
        self.client.hdel(SESSION_TOKEN_KEY, self.user_id)
        self.client.hdel(SESSION_EXPIRE_TS_KEY, self.user_id)


if __name__ == '__main__':
    redis = Redis(decode_responses=True)
    session = LoginSession(redis, 'bingo')

    user_token = session.create()
    print(user_token)  # 22d33cb3155533bd71240d4de2285fbf27916253828be4c3784cb5577ae7ec05

    res = session.validate('this is a wrong token')
    print(res)  # SESSION_TOKEN_INCORRECT

    res = session.validate(user_token)
    print(res)  # SESSION_TOKEN_CORRECT

    session.destroy()
    res = session.validate(user_token)
    print(res)  # SESSION_NOT_LOGIN
```

### Java 版本

```java
import org.apache.commons.codec.binary.Hex;
import redis.clients.jedis.Jedis;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Random;


public class LoginSession {

    private final String SESSION_TOKEN_KEY = "SESSION:TOKEN";
    private final String SESSION_EXPIRE_TS_KEY = "SESSION:EXPIRE";

    private final String SESSION_NOT_LOGIN = "SESSION_NOT_LOGIN";
    private final String SESSION_EXPIRE = "SESSION_EXPIRE";
    private final String SESSION_TOKEN_CORRECT = "SESSION_TOKEN_CORRECT";
    private final String SESSION_TOKEN_INCORRECT = "SESSION_TOKEN_INCORRECT";

    private Jedis client;
    private String userId;

    public LoginSession(Jedis client, String userId) {
        this.client = client;
        this.userId = userId;
    }

    /**
     * 生成随机token
     *
     * @return token
     */
    private String generateToken() {
        byte[] b = new byte[256];
        new Random().nextBytes(b);

        MessageDigest messageDigest;

        String sessionToken = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hash = messageDigest.digest(b);
            sessionToken = Hex.encodeHexString(hash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return sessionToken;
    }

    /**
     * 创建会话，并返回token
     *
     * @param timeout 过期时长
     * @return token
     */
    public String create(int timeout) {
        String token = generateToken();
        long expireTime = Instant.now().getEpochSecond() + timeout;
        client.hset(SESSION_TOKEN_KEY, userId, token);
        client.hset(SESSION_EXPIRE_TS_KEY, userId, String.valueOf(expireTime));
        return token;
    }

    public String create() {
        // 设置默认过期时长
        int defaultTimeout = 30 * 24 * 3600;
        return create(defaultTimeout);
    }

    /**
     * 校验token
     *
     * @param token 输入的token
     * @return 校验结果
     */
    public String validate(String token) {
        String sessionToken = client.hget(SESSION_TOKEN_KEY, userId);
        String expireTimeStr = client.hget(SESSION_EXPIRE_TS_KEY, userId);

        if (sessionToken == null || expireTimeStr == null) {
            return SESSION_NOT_LOGIN;
        }

        Long expireTime = Long.parseLong(expireTimeStr);
        if (Instant.now().getEpochSecond() > expireTime) {
            return SESSION_EXPIRE;
        }

        if (sessionToken.equals(token)) {
            return SESSION_TOKEN_CORRECT;
        }
        return SESSION_TOKEN_INCORRECT;
    }

    /**
     * 销毁会话
     */
    public void destroy() {
        client.hdel(SESSION_TOKEN_KEY, userId);
        client.hdel(SESSION_EXPIRE_TS_KEY, userId);
    }


    public static void main(String[] args) {
        Jedis client = new Jedis();
        LoginSession session = new LoginSession(client, "bingo");

        String token = session.create();
        // b0bc7274a7af7a8b9aba7e25f08a80782654e9fa8b23cd4b9f417200a412c9a6
        System.out.println(token);

        String res = session.validate("this is a wrong token");
        // SESSION_TOKEN_INCORRECT
        System.out.println(res);

        res = session.validate(token);
        // SESSION_TOKEN_CORRECT
        System.out.println(res);

        session.destroy();
        res = session.validate(token);
        // SESSION_NOT_LOGIN
        System.out.println(res);
    }
}
```