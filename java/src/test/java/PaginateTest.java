import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PaginateTest {

    @Test
    public void testPaginate() {
        Paginate topics = new Paginate("user-topics");
        for (int i = 0; i < 24; ++i) {
            topics.add(i + "");
        }

        // 取总数
        assertEquals(24, topics.getTotalCount());

        // 以每页5条数据的方式取出第1页数据
        assertEquals(Arrays.asList("23", "22", "21", "20", "19"),
                topics.getPage(1, 5));

        // 以每页10条数据的方式取出第1页数据
        assertEquals(Arrays.asList("23", "22", "21", "20", "19", "18", "17", "16", "15", "14"),
                topics.getPage(1, 10));

        // 以每页5条数据的方式取出第5页数据
        assertEquals(Arrays.asList("3", "2", "1", "0"),
                topics.getPage(5, 5));

    }
}
