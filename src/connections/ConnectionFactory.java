package connections;

public class ConnectionFactory {
	public static ReadConnection createSNMPv2cConnection(String address, int port, String community) throws ConnectionException {
		ReadConnection connection = new SNMPv2cConnection(address, port, community);
		connection.establish();
		return connection;
	}
}
