package test.connections.snmp;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.snmp4j.agent.mo.MOAccessImpl;
import org.snmp4j.agent.mo.MOScalar;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;

import rockthenet.connections.ConnectionException;
import rockthenet.connections.snmp.SNMPConnectionFactory;
import rockthenet.connections.ReadConnection;
import rockthenet.connections.snmp.SNMPv2cConnection;

/* TODO: add a real MOTable for testing */

/**
 * Unit-tests for {@link SNMPv2cConnection}
 * 
 * @author Elias Frantar
 * @version 2014-10-05
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class SNMPv2cConnectionTest {
	private static final String ADDRESS = "127.0.0.1";
	private static final int 	PORT = 1025;
	private static final String COMMUNITY_NAME = "test";
	private static final String SECURITY_NAME = "test";
	
	private static final String OID_1 = "1.3.1.1.1.1.1.1.1.1";
	private static final String OID_2 = "1.3.2.1.1.1.1.1.1.1";
	private static final String OID_1_TEXT = "This is the first test!";
	private static final String OID_2_TEXT = "This is the second test!";
	
	private ReadConnection connection;
	private SNMPv2Agent snmpAgent = null;

	/* execute before and after every test-case */
	@Before
	public void setup() throws ConnectionException, IOException {
        if(snmpAgent == null)
		    snmpAgent = new SNMPv2Agent(ADDRESS, PORT, COMMUNITY_NAME);
		snmpAgent.start();
		
		snmpAgent.unregisterManagedObject(snmpAgent.getSnmpv2MIB());
		snmpAgent.registerManagedObject(new MOScalar(new OID(OID_1), new MOAccessImpl(1), new OctetString(OID_1_TEXT)));
		snmpAgent.registerManagedObject(new MOScalar(new OID(OID_2), new MOAccessImpl(1), new OctetString(OID_2_TEXT)));	

		connection = SNMPConnectionFactory.createSNMPv2cConnection(ADDRESS, PORT, COMMUNITY_NAME, SECURITY_NAME);
	}
	@After
	public void tearDown() {
		connection.close();
		snmpAgent.stop();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
	
	/* test-cases for successful operations */
	
	@Test
	public void singleGetTest() throws ConnectionException {
		String result = connection.get(OID_1).toValueString();
		assertEquals(OID_1_TEXT, result);
	}
	@Test
	public void multipleGetTest() throws ConnectionException {
		VariableBinding[] result = connection.get(new String[]{OID_1, OID_2});
		
		assertEquals(OID_1_TEXT, result[0].toValueString());
		assertEquals(OID_2_TEXT, result[1].toValueString());
	}
	@Test
	public void getTableTest() throws ConnectionException {
		VariableBinding[] result = connection.getTable(OID_1.substring(0, OID_1.length() - 2));
		
		assertEquals(OID_1_TEXT, result[0].toValueString());
	}
	
	@Test
	public void connectTwiceTest() throws ConnectionException { // should still work afterwards
		connection.establish();
		connection.establish();
		
		String result = connection.get(OID_1).toValueString();
		assertEquals(OID_1_TEXT, result);
	}
	@Test
	public void reconnectTest() throws ConnectionException {
		connection.close();
		connection.establish();
		
		String result = connection.get(OID_1).toValueString();
		assertEquals(OID_1_TEXT, result);
	}
	
	@Test (expected = ConnectionException.class)
	public void closeTest() throws ConnectionException {
		connection.close();
		connection.get(OID_1);
	}
	@Test
	public void closeTwiceTest() { // should not throw any Exceptions
		connection.close();
		connection.close();
	}
	
	/* test-cases for failing operations */
	
	@Test
	public void getWrongOIDTest() throws ConnectionException {
		String result = connection.get("2.3.4.5").toValueString();
		assertEquals("noSuchObject", result);
	}
	
	/* Exception tests */
	
	@Test (expected = ConnectionException.class)
	public void invalidAddressTest() throws ConnectionException {
		connection.close();
		connection = SNMPConnectionFactory.createSNMPv2cConnection("aaa", PORT, COMMUNITY_NAME, SECURITY_NAME);
	}
	@Test (expected = ConnectionException.class)
	public void notExistingAddressTest() throws ConnectionException {
		connection.close();
		connection = SNMPConnectionFactory.createSNMPv2cConnection("10.10.10.10", PORT, COMMUNITY_NAME, SECURITY_NAME);
	}
	
	@Test (expected = ConnectionException.class)
	public void getWithClientDownTest() throws ConnectionException {
		connection.close();
		connection.get(OID_1);
	}
	@Test (expected = ConnectionException.class)
	public void getWithServerDownTest() throws ConnectionException {
		snmpAgent.stop();
		connection.get(OID_1);
	}
	
	@Test (expected = ConnectionException.class)
	public void getTableWithClientDownTest() throws ConnectionException {
		connection.close();
		connection.getTable(OID_1);
	}
	@Test (expected = ConnectionException.class)
	public void getTableWithServerDownTest() throws ConnectionException {
		snmpAgent.stop();
		connection.getTable(OID_1);
	}
}
