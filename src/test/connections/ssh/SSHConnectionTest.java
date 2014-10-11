package test.connections.ssh;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import rockthenet.connections.ConnectionException;
import rockthenet.connections.WriteConnection;
import rockthenet.connections.ssh.SSHConnectionFactory;

public class SSHConnectionTest {
	private static final String HOST = 		"10.0.100.10";
	private static final String USERNAME = 	"5ahit";
	private static final String PASSWORD = 	"Waeng7ohch8o";
	
	WriteConnection sshConnection;
	
	@Before
	public void setup() throws ConnectionException {
		sshConnection = SSHConnectionFactory.createSSHConnection(HOST, USERNAME, PASSWORD);
	}
	@After
	public void tearDown() {
		sshConnection.close();
	}
	
	@Test
	public void testExec() throws ConnectionException {
		sshConnection.execute("unset policy id 4\n");
	}
}