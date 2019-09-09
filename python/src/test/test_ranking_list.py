from unittest import TestCase

from redis import Redis

from main.ranking_list import Ranking


def test_ranking():
    test_case = TestCase()
    redis = Redis(decode_responses=True)
    redis.flushall()
    ranking = Ranking(redis)

    ranking.incr('bingo', 5)
    ranking.incr('iris', 3)
    ranking.incr('lily', 4)
    ranking.incr('helen', 6)

    test_case.assertListEqual([('helen', 6.0), ('bingo', 5.0)], ranking.get_today_top_n(n=2, with_scores=True))
    test_case.assertListEqual([('helen', 6.0), ('bingo', 5.0)], ranking.get_week_top_n(n=2, with_scores=True))
    test_case.assertListEqual([('helen', 6.0), ('bingo', 5.0), ('lily', 4.0)], ranking.get_month_top_n(n=3, with_scores=True))
