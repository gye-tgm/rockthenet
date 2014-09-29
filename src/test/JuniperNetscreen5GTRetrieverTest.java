package test;

import net.percederberg.mibble.MibValue;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import rockthenet.firewall.junipernetscreen5gt.JuniperNetscreen5GTRetriever;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class JuniperNetscreen5GTRetrieverTest {
    JuniperNetscreen5GTRetriever juniperNetscreen5GTRetriever;
    @Before
    public void setUp() throws Exception {
         juniperNetscreen5GTRetriever = new JuniperNetscreen5GTRetriever("10.0.100.10", 161, "5xHIT");
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testRetrievePolicies() throws Exception {

    }

    @Test
    public void testGetReadConnection() throws Exception {

    }

    @Test
    public void testGetOidDictionary() throws Exception {
        HashMap<String, MibValue> hashMap = juniperNetscreen5GTRetriever.getOidDictionary();
        for(Map.Entry<String, MibValue> e: hashMap.entrySet()){
            System.out.println(e.getKey() + " "  + e.getValue());
        }

    }
}