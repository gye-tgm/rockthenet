package test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import rockthenet.MibHelper;

/**
 * @author Gary Ye
 */
public class MibHelperTest {
    private MibHelper mibHelper;

    @Before
    public void setUp() throws Exception {
        mibHelper = new MibHelper("res/asn1-3224-mibs/NETSCREEN-POLICY-MIB.mib");
    }

    @After
    public void tearDown() throws Exception {
        mibHelper = null;
    }

    @Test
    public void testGetOID(){
        String[] names = {"nsPlyId", "nsPlySrcZone", "nsPlyDstZone", "nsPlySrcAddr", "nsPlyDstAddr", "nsPlyService", "nsPlyAction", "nsPlyActiveStatus", "nsPlyName", "netscreenPolicy"};
        for(int i = 0; i < names.length; i++) {
            System.out.println(names[i] + " " + mibHelper.getOID(names[i]));
        }
    }
}
