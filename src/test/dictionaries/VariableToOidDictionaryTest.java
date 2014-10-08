package test.dictionaries;

import net.percederberg.mibble.MibLoaderException;
import org.junit.Before;
import org.junit.Test;
import rockthenet.dictionaries.VariableToOidDictionary;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Tests the variable to oid dictionary by testing with the Juniper Netscreen mibs.
 */
public class VariableToOidDictionaryTest {
    private VariableToOidDictionary dictionary;

    @Before
    public void setUp() throws IOException, MibLoaderException {
        dictionary = new VariableToOidDictionary();
        dictionary.load(new File("res/asn1-3224-mibs/NETSCREEN-POLICY-MIB.mib"));
    }

    @Test
    public void testLoadNsPlyId() throws Exception {
        assertEquals(dictionary.getVariableToOid("nsPlyId"), "1.3.6.1.4.1.3224.10.1.1.1");
    }

    @Test
    public void testLoadOidOfNsPlyId() {
        assertEquals(dictionary.getOidToVariable("1.3.6.1.4.1.3224.10.1.1.1"), "nsPlyId");
    }

    @Test
    public void testLoadOidToVariableNull() {
        assertEquals(dictionary.getOidToVariable("1"), null);
    }

    @Test
    public void testLoadVariableToOidNull() {
        assertEquals(dictionary.getVariableToOid("invalid abc"), null);
    }
}