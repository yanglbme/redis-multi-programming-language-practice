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
}