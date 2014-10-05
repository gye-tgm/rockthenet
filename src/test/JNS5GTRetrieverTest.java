package test;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.snmp4j.smi.*;
import rockthenet.MibHelper;
import rockthenet.connections.ConnectionException;
import rockthenet.connections.ReadConnection;
import rockthenet.firewall.jns5gt.JNS5GTRetriever;
import rockthenet.firewall.jns5gt.JNS5GTPolicy;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


public class JNS5GTRetrieverTest {
    @Before
    public void setUp() {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGetPolicies() throws Exception {
        ReadConnection readConnection = mock(ReadConnection.class);
        MibHelper mibHelper = new MibHelper("res/asn1-3224-mibs/NETSCREEN-POLICY-MIB.mib");

        List<JNS5GTPolicy> expectedPolicies = new ArrayList<>();

        JNS5GTPolicy policy1 = new JNS5GTPolicy();
        policy1.setId(1);
        policy1.setName("policy-test-1");
        policy1.setThruPut(100);
        policy1.setSrcAddress("0.0.0.0");
        policy1.setDstAddress("127.0.0.1");
        policy1.setSrcZone("Trust");
        policy1.setDstZone("Untrust");
        policy1.setService(0);
        policy1.setAction(1);
        policy1.setActiveStatus(2);

        expectedPolicies.add(policy1);

        VariableBinding[] variableBindings = new VariableBinding[]{
                new VariableBinding(new OID(mibHelper.getOID("nsPlyId") + ".1.0"), new Integer32(1)),
                new VariableBinding(new OID(mibHelper.getOID("nsPlySrcAddr") + ".1.0"), new OctetString("0.0.0.0")),
                new VariableBinding(new OID(mibHelper.getOID("nsPlyDstAddr") + ".1.0"), new OctetString("127.0.0.1")),
                new VariableBinding(new OID(mibHelper.getOID("nsPlyMonBytePerSec") + ".1.0"), new Integer32(100)),
                new VariableBinding(new OID(mibHelper.getOID("nsPlyName") + ".1.0"), new OctetString("policy-test-1")),
                new VariableBinding(new OID(mibHelper.getOID("nsPlySrcZone") + ".1.0"), new OctetString("Trust")),
                new VariableBinding(new OID(mibHelper.getOID("nsPlyDstZone") + ".1.0"), new OctetString("Untrust")),
                new VariableBinding(new OID(mibHelper.getOID("nsPlyService") + ".1.0"), new Integer32(0)),
                new VariableBinding(new OID(mibHelper.getOID("nsPlyAction") + ".1.0"), new Integer32(1)),
                new VariableBinding(new OID(mibHelper.getOID("nsPlyActiveStatus") + ".1.0"), new Integer32(2)),
        };

        when(readConnection.getTable(mibHelper.getOID("netscreenPolicyMibModule"))).thenReturn(variableBindings);

        JNS5GTRetriever retriever = new JNS5GTRetriever(readConnection);
        List<JNS5GTPolicy> actualPolicies = (ArrayList<JNS5GTPolicy>) retriever.get("policies");
        assertEquals(expectedPolicies, actualPolicies);
    }

    @Test(expected = ConnectionException.class)
    public void testRetrievePoliciesWithConnection() throws ConnectionException {
        // TODO: this needs to be fixed
        JNS5GTRetriever retriever = new JNS5GTRetriever("NOSUCHADDRESSSAVAILABLE", 161, "5xHIT");
        for(JNS5GTPolicy policy: retriever.retrievePolicies()){
            System.out.println(policy.toString() + "\n");
        }
    }

    @Test
    public void testGetNothing() throws Exception {
        ReadConnection readConnection = mock(ReadConnection.class);
        JNS5GTRetriever retriever = new JNS5GTRetriever(readConnection);
        assertNull(retriever.get("Huhu"));
    }
}