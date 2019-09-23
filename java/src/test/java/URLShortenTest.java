import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class URLShortenTest {

    @Test
    public void testURLShorten() {
        URLShorten urlShorten = new URLShorten();

        String shortId = urlShorten.shorten("https://github.com/yanglbme");

        assertEquals("100000", shortId);
        String sourceUrl = urlShorten.restore(shortId);
        assertEquals("https://github.com/yanglbme", sourceUrl);

        shortId = urlShorten.shorten("https://doocs.github.io");
        assertEquals("100001", shortId);
    }
}