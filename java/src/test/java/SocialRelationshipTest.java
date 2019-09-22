import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class SocialRelationshipTest {

    @Test
    public void testSNS() {
        SocialRelationship bingo = new SocialRelationship("Bingo");
        SocialRelationship iris = new SocialRelationship("Iris");
        bingo.follow("Iris");
        bingo.follow("GitHub");
        bingo.follow("Apple");
        iris.follow("Bingo");
        iris.follow("GitHub");

        assertTrue(bingo.isFollowing("Iris"));
        assertEquals(new HashSet<String>() {{ add("Apple"); add("GitHub"); add("Iris"); }},
                bingo.getAllFollowing());

        assertTrue(iris.isFollowing("Bingo"));

        assertEquals(3, bingo.countFollowing());
        assertEquals(new HashSet<String>() {{ add("GitHub"); }},
                bingo.getCommonFollowing("Iris"));

    }
}
