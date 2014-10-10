package rockthenet.connections.ssh;

import rockthenet.connections.ConnectionException;
import rockthenet.connections.WriteConnection;

public class SSHConnectionFactory {
	
	public static WriteConnection createSSHConnection(String host, String username, String password) throws ConnectionException {
		WriteConnection connection = new SSHConnection(host, username, password);
		connection.establish();
		
		return connection;
	}
	
}
