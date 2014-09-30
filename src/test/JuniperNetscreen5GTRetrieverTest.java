package test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import rockthenet.firewall.Policy;
import rockthenet.firewall.junipernetscreen5gt.JuniperNetscreen5GTRetriever;

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
    public void test() {
    	System.out.println(new Policy(1, "test", "test", "test", "test", 1, 1, 1, "test"));
    }
}