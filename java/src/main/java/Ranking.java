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
        client.flushAll();
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
}