from unittest import TestCase

from redis import Redis

from main.auto_complete import AutoComplete


def test_auto_complete():
    redis = Redis(decode_responses=True)
    redis.flushall()
    test_case = TestCase()

    ac = AutoComplete(redis)
    ac.feed('张艺兴', 5000)
    ac.feed('张艺谋', 3000)
    ac.feed('张三', 500)

    test_case.assertListEqual(['张艺兴', '张艺谋', '张三'], ac.hint('张'))
    test_case.assertListEqual(['张艺兴', '张艺谋'], ac.hint('张艺'))
