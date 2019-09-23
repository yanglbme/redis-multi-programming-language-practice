import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoginSessionTest {

    @Test
    public void testLoginSession() {
        LoginSession session = new LoginSession("bingo");

        String token = session.create();

        String res = session.validate("this is a wrong token");
        assertEquals("SESSION_TOKEN_INCORRECT", res);

        res = session.validate(token);
        assertEquals("SESSION_TOKEN_CORRECT", res);

        session.destroy();
        res = session.validate(token);
        assertEquals("SESSION_NOT_LOGIN", res);
    }
}