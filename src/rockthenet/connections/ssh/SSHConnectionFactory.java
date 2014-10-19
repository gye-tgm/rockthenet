package rockthenet.connections.ssh;

import rockthenet.connections.ConnectionException;
import rockthenet.connections.WriteConnection;

/**
 * This factory produces established {@link rockthenet.connections.Connection}s for the SSH protocol.
 * <p> Every {@link rockthenet.connections.Connection} returned by this class is ready for use. (has already been established)
 * 
 * @author Elias Frantar
 * @version 2014-10-19
 */
public class SSHConnectionFactory {
	
	/**
	 * Creates a new established {@link rockthenet.connections.snmp.SSHConnection}
	 * 
	 * @param host the host address of the SSH-server (IP or URL)
	 * @param username the username to use for connecting
	 * @param password the corresponding password
	 * 
	 * @return a ready to use {@link rockthenet.connections.snmp.SSHConnection} with the specified parameters
	 * @throws ConnectionException thrown if connecting failed (see Exception-message for more information)
	 */
	public static WriteConnection createSSHConnection(String host, String username, String password) throws ConnectionException {
		WriteConnection connection = new SSHConnection(host, username, password);
		connection.establish();
		
		return connection;
	}
	
}
