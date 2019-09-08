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
