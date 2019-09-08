import random
import string
import time

from redis import Redis


def generate_random_value():
    return ''.join(random.sample(string.ascii_letters + string.digits, 32))


# 设定默认过期时长为 10s
DEFAULT_TIMEOUT = 10


class DistributedLock:
    def __init__(self, client: Redis, key: str):
        self.client = client
        self.key = key

    def acquire(self, timeout=DEFAULT_TIMEOUT) -> bool:
        """尝试获取锁"""
        res = self.client.set(self.key, generate_random_value(), ex=timeout, nx=True)  # ex 表示过期时长秒数，nx 表示 key 不存在时才设置
        return res is True

    def release(self) -> bool:
        """尝试释放锁"""
        return self.client.delete(self.key) == 1
