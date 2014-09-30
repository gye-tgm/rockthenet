package test.connections;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Vector;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.snmp4j.PDU;
import org.snmp4j.agent.mo.MOAccessImpl;
import org.snmp4j.agent.mo.MOScalar;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;

import org.snmp4j.smi.VariableBinding;
import rockthenet.MibHelper;
import rockthenet.connections.ConnectionException;
import rockthenet.connections.ConnectionFactory;
import rockthenet.connections.ReadConnection;
import rockthenet.firewall.junipernetscreen5gt.JuniperNetscreen5GTRetriever;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class SNMPv2cConnectionTest {
	private static final String ADDRESS = "127.0.0.1";
	private static final int 	PORT = 1025;
	private static final String COMMUNITY = "test";
	
	private static final String OID_1 = "1.3.1.1.1.1.1.1.1.1";
	private static final String OID_2 = "1.3.1.1.4.5";
	private static final String OID_1_TEXT = "This is the first test!";
	private static final String OID_2_TEXT = "This is the second test!";
	
	private ReadConnection connection;

	private SNMPv2Agent snmpAgent;

	@Before
	public void setup() throws ConnectionException, IOException {
		snmpAgent = new SNMPv2Agent(ADDRESS, PORT, COMMUNITY);
		snmpAgent.start();
		
		snmpAgent.unregisterManagedObject(snmpAgent.getSnmpv2MIB());
		snmpAgent.registerManagedObject(new MOScalar(new OID(OID_1), new MOAccessImpl(1), new OctetString(OID_1_TEXT)));
		snmpAgent.registerManagedObject(new MOScalar(new OID(OID_2), new MOAccessImpl(1), new OctetString(OID_2_TEXT)));	
		
		connection = ConnectionFactory.createSNMPv2cConnection(ADDRESS, PORT, COMMUNITY);
	}
	
	@After
	public void tearDown() {
		connection.close();
		snmpAgent.stop();
	}
	
	/* test-cases for successful operations */
	
	@Test
	public void singleGetTest() throws ConnectionException {
		String result = connection.get(OID_1).getVariable(new OID(OID_1)).toString();
		assertEquals(OID_1_TEXT, result);
	}
	@Test
	public void multipleGetTest() throws ConnectionException {
		PDU result = connection.get(new String[]{OID_1, OID_2});
		
		assertEquals(OID_1_TEXT, result.getVariable(new OID(OID_1)).toString());
		assertEquals(OID_2_TEXT, result.getVariable(new OID(OID_2)).toString());
	}
	
	/* test-cases for failing operations */
	
	@Test
	public void getWrongOIDTest() throws ConnectionException {
		String result = connection.get("2.3.4.5").getVariable(new OID("2.3.4.5")).toString();
		assertEquals("noSuchObject", result);
	}
	
	@Test
	public void testGetFirewall() throws ConnectionException {
		connection = ConnectionFactory.createSNMPv2cConnection("10.0.100.10", 161, "5xHIT");
		System.out.println(connection.get(".1.3.6.1.2.1.1.4.0").get(0));
	}
}
