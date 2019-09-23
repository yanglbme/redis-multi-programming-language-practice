import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LikeServiceTest {

    @Test
    public void testLikeService() {
        LikeService likeService = new LikeService();
        String entityId = "user1";
        assertEquals(0, likeService.getLikeCount(entityId));

        likeService.like("user2", entityId);
        likeService.like("user3", entityId);

        likeService.dislike("user4", entityId);

        assertEquals(2, likeService.getLikeCount(entityId));
        assertEquals(1, likeService.getLikeStatus("user2", entityId));

        assertEquals(-1, likeService.getLikeStatus("user4", entityId));
    }
}
