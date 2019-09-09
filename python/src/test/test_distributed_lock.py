import time

from redis import Redis

from main.distributed_lock import DistributedLock


def test_distributed_lock():
    redis = Redis(decode_responses=True)
    redis.flushall()
    lock = DistributedLock(redis, 'bingo_distributed_lock')

    assert lock.acquire(10) is True
    assert lock.acquire(10) is False

    time.sleep(10)

    assert lock.acquire() is True
    assert lock.release() is True

    assert lock.acquire() is True
