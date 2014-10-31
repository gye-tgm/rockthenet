package test.validators;

import org.junit.Before;
import org.junit.Test;

import rockthenet.validation.EmailValidator;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EmailValidatorTest {
    private EmailValidator emailValidator;
    @Before
    public void setUp(){
        emailValidator = new EmailValidator();
    }
    @Test
    public void testValidateSuccess() throws Exception {
        assertTrue(emailValidator.validate("ohayou@gmail.com"));
    }
    @Test
    public void testValidateFail(){
        assertFalse(emailValidator.validate("sensee@"));
    }

    @Test
    public void testValidateFail2(){
        assertFalse(emailValidator.validate("@soudesu.com"));
    }

    @Test
    public void testValidateFailAffe(){
        assertFalse(emailValidator.validate("affe")); // random hex string 0xAFFE
    }
}