# 利用 Redis Sorted Set 实现日/周/月排行榜

## 代码实现
| [Python](#Python-版本) | [Java](#Java-版本) |
|---|---|

### Python 版本
```python
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


if __name__ == '__main__':
    redis = Redis(decode_responses=True)
    ranking = Ranking(redis)

    ranking.incr('bingo', 5)
    ranking.incr('iris', 3)
    ranking.incr('lily', 4)
    ranking.incr('helen', 6)

    print(ranking.get_today_top_n(n=2, with_scores=True))  # [('helen', 6.0), ('bingo', 5.0)]
    print(ranking.get_week_top_n(n=2, with_scores=True))  # [('helen', 6.0), ('bingo', 5.0)]
    print(ranking.get_month_top_n(n=3, with_scores=True))  # [('helen', 6.0), ('bingo', 5.0), ('lily', 4.0)]
```

### Java 版本
```java
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.Calendar;
import java.util.Set;

/**
 * @author bingoyang
 * @date 2019/9/7
 */
public class Ranking {
    private Jedis client = new Jedis();
    private Calendar calendar = Calendar.getInstance();

    public Ranking() {
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
    }

    private String getDayRankKey() {
        return String.format("rank:day:%s%s%s", calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    }

    private String getWeekRankKey() {
        return String.format("rank:week:%s%s", calendar.get(Calendar.YEAR), calendar.get(Calendar.WEEK_OF_YEAR));
    }

    private String getMonthRankKey() {
        return String.format("rank:month:%s%s", calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH));
    }

    public void incr(String user, double score) {
        client.zincrby(getDayRankKey(), score, user);
        client.zincrby(getWeekRankKey(), score, user);
        client.zincrby(getMonthRankKey(), score, user);
    }

    /**
     * 获取日榜单topN
     *
     * @param n 前n位
     * @return 结果集合
     */
    public Set<Tuple> getTodayTopNWithScores(long n) {
        return client.zrevrangeWithScores(getDayRankKey(), 0, n - 1);
    }

    /**
     * 获取周榜单topN
     *
     * @param n 前n位
     * @return 结果集合
     */
    public Set<Tuple> getWeekTopNWithScores(long n) {
        return client.zrevrangeWithScores(getWeekRankKey(), 0, n - 1);
    }

    /**
     * 获取月榜单topN
     *
     * @param n 前n位
     * @return 结果集合
     */
    public Set<Tuple> getMonthTopNWithScores(long n) {
        return client.zrevrangeWithScores(getMonthRankKey(), 0, n - 1);
    }

    public static void main(String[] args) {
        Ranking ranking = new Ranking();

        ranking.incr("bingo", 5);
        ranking.incr("iris", 3);
        ranking.incr("lily", 4);
        ranking.incr("helen", 6);

        System.out.println(ranking.getTodayTopNWithScores(2)); // [[helen,6.0], [bingo,5.0]]
        System.out.println(ranking.getWeekTopNWithScores(2)); // [[helen,6.0], [bingo,5.0]]
        System.out.println(ranking.getMonthTopNWithScores(3)); // [[helen,6.0], [bingo,5.0], [lily,4.0]]

    }
}
```