from unittest import TestCase

from redis import Redis

from main.paginate import Paginate


def test_paginate():
    test_case = TestCase()
    redis = Redis(decode_responses=True)
    redis.flushall()
    topics = Paginate(redis, 'user-topics')

    for i in range(24):
        topics.add(i)

    # 取总数
    assert topics.get_size() == 24

    # 以每页5条数据的方式取出第1页数据
    test_case.assertListEqual(['23', '22', '21', '20', '19'], topics.get_page(1, 5))

    # 以每页10条数据的方式取出第1页数据
    test_case.assertListEqual(['23', '22', '21', '20', '19', '18', '17', '16', '15', '14'], topics.get_page(1, 10))

    # 以每页5条数据的方式取出第5页数据
    test_case.assertListEqual(['3', '2', '1', '0'], topics.get_page(5, 5))
