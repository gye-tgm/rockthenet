package test.validators;

import org.junit.Before;
import org.junit.Test;
import rockthenet.validators.IPValidator;

import static org.junit.Assert.*;

public class IPValidatorTest {
    private IPValidator ipValidator;
    @Before
    public void setUp(){
        ipValidator = new IPValidator();
    }
    @Test
    public void testValidateSuccess() throws Exception {
        assertTrue(ipValidator.validate("1.34.12.3"));
    }
    @Test
    public void testValidateFail(){
        assertFalse(ipValidator.validate("0.-52.12.3"));
    }

    @Test
    public void testValidateFail2(){
        assertFalse(ipValidator.validate("0.-52.12"));
    }

    @Test
    public void testValidateFailAffe(){
        assertFalse(ipValidator.validate("affe")); // random hex string 0xAFFE
    }
}