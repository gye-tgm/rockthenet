package test.connections;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.snmp4j.agent.mo.MOAccessImpl;
import org.snmp4j.agent.mo.MOScalar;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;

import rockthenet.connections.ConnectionException;
import rockthenet.connections.ConnectionFactory;
import rockthenet.connections.ReadConnection;

public class SNMPv2cConnectionTest {
	private static final String ADDRESS = "127.0.0.1";
	private static final int 	PORT = 161;
	private static final String COMMUNITY = "test";
	
	private static final String OID_1 = "1.3.1.1.1.1.1.1.1.1";
	private static final String OID_2 = "1.3.1.1.4.5";
	private static final String OID_1_TEXT = "This is the first test!";
	private static final String OID_2_TEXT = "This is the second test!";
	
	private ReadConnection connection;

	private static SNMPv2Agent snmpAgent;
	
	@BeforeClass
	public static void startUp() throws IOException {
		snmpAgent = new SNMPv2Agent(ADDRESS, PORT, COMMUNITY);
		snmpAgent.start();
		
		snmpAgent.unregisterManagedObject(snmpAgent.getSnmpv2MIB());
		snmpAgent.registerManagedObject(new MOScalar(new OID(OID_1), new MOAccessImpl(1), new OctetString(OID_1_TEXT)));
		snmpAgent.registerManagedObject(new MOScalar(new OID(OID_2), new MOAccessImpl(1), new OctetString(OID_2_TEXT)));	
	}
	@AfterClass
	public static void closeDown() {
		snmpAgent.stop();
	}
	
	@Before
	public void setup() throws ConnectionException {
		connection = ConnectionFactory.createSNMPv2cConnection(ADDRESS, PORT, COMMUNITY);
	}
	@After
	public void tearDown() {
		connection.close();
	}
	
	@Test
	public void singleGetTest() throws ConnectionException {
		String result = connection.get(OID_1).get(0).toValueString();
		assertEquals(OID_1_TEXT, result);
	}
}
