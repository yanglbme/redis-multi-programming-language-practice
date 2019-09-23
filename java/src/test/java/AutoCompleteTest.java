import org.junit.jupiter.api.Test;

import java.util.LinkedHashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AutoCompleteTest {

    @Test
    public void testHint() {
        AutoComplete ac = new AutoComplete();
        ac.feed("张艺兴", 5000);
        ac.feed("张艺谋", 3000);
        ac.feed("张三", 500);
        assertEquals(ac.hint("张", 10),
                new LinkedHashSet<String>() {{
                    add("张艺兴");
                    add("张艺谋");
                    add("张三");
                }});

        assertEquals(ac.hint("张艺", 10),
                new LinkedHashSet<String>() {{
                    add("张艺兴");
                    add("张艺谋");
                }});
    }
}