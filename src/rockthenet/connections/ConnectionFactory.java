package rockthenet.connections;

/**
 * This factory produces established {@link Connection}s for various protocols.
 * <p> Every {@link Connection} returned by this class is ready for use. (has already been established)
 * 
 * @author Elias Frantar
 * @version 2014-10-05
 */
public class ConnectionFactory {
	
	/**
	 * Creates a new established {@link SNMPv2cConnection}
	 * 
	 * @param address the address of the SNMP-server (IP or URL)
	 * @param port the port of the SNMP-server
	 * @param communityName the name of the community to connect to
	 * @param securityName the security name of the community to connect to
	 * 
	 * @return a ready to used {@link SNMPv2cConnection} with the specified parameters
	 * @throws ConnectionException thrown if connecting failed (see Exception-message for more information)
	 */
	public static ReadConnection createSNMPv2cConnection(String address, int port, String communityName, String securityName ) throws ConnectionException {
		ReadConnection connection = new SNMPv2cConnection(address, port, communityName, securityName);
		connection.establish();
		return connection;
	}
}
