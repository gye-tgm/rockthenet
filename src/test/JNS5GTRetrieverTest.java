package test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import rockthenet.firewall.jns5gt.JNS5GTRetriever;
import rockthenet.firewall.jns5gt.JNS5GTPolicy;

import java.util.Map;

public class JNS5GTRetrieverTest {
    JNS5GTRetriever JNS5GTRetriever;
    @Before
    public void setUp() throws Exception {
         JNS5GTRetriever = new JNS5GTRetriever("10.0.100.10", 161, "5xHIT");
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testRetrievePolicies() throws Exception {
        for(JNS5GTPolicy policy: JNS5GTRetriever.retrievePolicies()){
            System.out.println(policy.toString() + "\n");
        }
    }

    @Test
    public void testGetReadConnection() throws Exception {

    }
}