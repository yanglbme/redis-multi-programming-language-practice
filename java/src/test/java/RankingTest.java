import org.junit.jupiter.api.Test;
import redis.clients.jedis.Tuple;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class RankingTest {

    @Test
    public void testRanking() {
        Ranking ranking = new Ranking();

        ranking.incr("bingo", 5);
        ranking.incr("iris", 3);
        ranking.incr("lily", 4);
        ranking.incr("helen", 6);

        Set<Tuple> expectResult = new HashSet<Tuple>() {{
            add(new Tuple("helen", 6.0));
            add(new Tuple("bingo", 5.0));
        }};
        assertEquals(expectResult, ranking.getTodayTopNWithScores(2));
        assertEquals(expectResult, ranking.getWeekTopNWithScores(2));

        expectResult.add(new Tuple("lily", 4.0));
        assertEquals(expectResult, ranking.getMonthTopNWithScores(3));
    }
}
