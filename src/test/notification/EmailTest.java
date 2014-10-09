package test.notification;

/**
 * Created by Samuel on 09.10.2014.
 */

import org.junit.Before;
import org.junit.Test;
import rockthenet.view.Email;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EmailTest {
    private Email email;

    @Before
    public void setUp() {
        email = new Email();
    }

    @Test
    public void testValidateSuccess() throws InterruptedException {
        email.sendMail("rockthenet.gg@gmail.com", "F9 liftoff", "F9 in orbit");
        assertTrue(email.checkMail("F9 liftoff"));
    }

    @Test
    public void testValidateFail() throws InterruptedException {
        email.sendMail("rockthenet.gg@gmail.com", "F9 liftoff", "F9 in orbit");
        assertFalse(email.checkMail("F9 crashed"));
    }
}