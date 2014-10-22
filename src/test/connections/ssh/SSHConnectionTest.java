package test.connections.ssh;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import rockthenet.connections.ConnectionException;
import rockthenet.connections.WriteConnection;
import rockthenet.connections.ssh.SSHConnectionFactory;

/* TODO: not all tests matching yet! */

/**
 * Unit-tests for {@link SSHConnection}
 * 
 * @author Elias Frantar
 * @version 2014-10-22
 */
public class SSHConnectionTest {
	private static final String HOST = 		"10.0.100.10";
	private static final String USERNAME = 	"5ahit";
	private static final String PASSWORD = 	"Waeng7ohch8o";
	
	private static final String COMMAND = "ls ~";
	
	WriteConnection connection;
	
	@Before
	public void setup() throws ConnectionException {
		connection = SSHConnectionFactory.createSSHConnection(HOST, USERNAME, PASSWORD);
	}
	@After
	public void tearDown() {
		connection.close();
	}
	
	/* test-cases for successful operations */
	
	@Ignore
	@Test
	public void executeCommand() throws ConnectionException {
		connection.execute(COMMAND);
	}
	
	@Ignore
	@Test
	public void connectTwiceTest() throws ConnectionException { // should still work afterwards
		connection.establish();
		connection.establish();
		
		connection.execute(COMMAND);
	}
	@Ignore
	@Test
	public void reconnectTest() throws ConnectionException {
		connection.close();
		connection.establish();
		
		connection.execute(COMMAND);
	}
	
	@Ignore
	@Test (expected = ConnectionException.class)
	public void closeTest() throws ConnectionException {
		connection.close();
		connection.execute(COMMAND);
	}
	@Ignore
	@Test
	public void closeTwiceTest() { // should not throw any Exceptions
		connection.close();
		connection.close();
	}
	
	/* Exception tests */
	
	@Ignore
	@Test (expected = ConnectionException.class)
	public void invalidAddressTest() throws ConnectionException {
		connection.close();
		connection = SSHConnectionFactory.createSSHConnection("aaa", USERNAME, PASSWORD);
	}
	@Ignore
	@Test (expected = ConnectionException.class)
	public void notExistingAddressTest() throws ConnectionException {
		connection.close();
		connection = SSHConnectionFactory.createSSHConnection("10.10.10.10", USERNAME, PASSWORD);
	}
	
	@Ignore
	@Test (expected = ConnectionException.class)
	public void getWithClientDownTest() throws ConnectionException {
		connection.close();
		connection.execute(COMMAND);
	}
}
