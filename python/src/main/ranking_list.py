import datetime
import time

from redis import Redis


def get_day_rank_key():
    return 'rank:day:{}'.format(datetime.date.today().strftime('%Y%m%d'))


def get_week_rank_key():
    return 'rank:week:{}{}'.format(datetime.date.today().strftime('%Y'), time.strftime("%W"))


def get_month_rank_key():
    today = datetime.date.today()
    return 'rank:month:{}{}'.format(today.strftime('%Y'), today.strftime('%m'))


class Ranking:
    def __init__(self, client: Redis):
        self.client = client

    def incr(self, user, score=1):
        self.client.zincrby(get_day_rank_key(), score, user)
        self.client.zincrby(get_week_rank_key(), score, user)
        self.client.zincrby(get_month_rank_key(), score, user)

    def get_today_top_n(self, n, with_scores=False):
        """获取日榜单topN"""
        return self.client.zrevrange(get_day_rank_key(), 0, n - 1, withscores=with_scores)

    def get_week_top_n(self, n, with_scores=False):
        """获取周榜单topN"""
        return self.client.zrevrange(get_week_rank_key(), 0, n - 1, withscores=with_scores)

    def get_month_top_n(self, n, with_scores=False):
        """获取月榜单topN"""
        return self.client.zrevrange(get_month_rank_key(), 0, n - 1, withscores=with_scores)
