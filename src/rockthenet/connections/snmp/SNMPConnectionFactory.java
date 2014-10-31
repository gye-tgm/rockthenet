package rockthenet.connections.snmp;

import rockthenet.connections.ConnectionException;
import rockthenet.connections.ReadConnection;

/**
 * This factory produces established {@link rockthenet.connections.Connection}s for the SNMP protocol.
 * <p> Every {@link rockthenet.connections.Connection} returned by this class is ready for use. (has already been
 * established)
 *
 * @author Elias Frantar
 * @version 2014-10-19
 */
public class SNMPConnectionFactory {

    /**
     * Creates a new established {@link rockthenet.connections.snmp.SNMPv2cConnection}
     *
     * @param address       the address of the SNMP-server (IP or URL)
     * @param port          the port of the SNMP-server
     * @param communityName the name of the community to connect to
     * @param securityName  the security name of the community to connect to
     * @return a ready to use {@link rockthenet.connections.snmp.SNMPv2cConnection} with the specified parameters
     * @throws ConnectionException thrown if connecting failed (see Exception-message for more information)
     */
    public static ReadConnection createSNMPv2cConnection(String address, int port, String communityName,
                                                         String securityName) throws ConnectionException {
        ReadConnection connection = new SNMPv2cConnection(address, port, communityName, securityName);
        connection.establish();
        return connection;
    }

    /**
     * Creates a new established {@link rockthenet.connections.snmp.SNMPv3Connection}
     *
     * @param address      the address of the SNMP-server (IP or URL)
     * @param port         the port of the SNMP-server
     * @param username     the name of the user
     * @param authPassword the authentication password
     * @param privPassword the privacy password
     * @return a ready to use {@link SNMPv2cConnection} with the specified parameters
     * @throws ConnectionException thrown if connecting failed (see Exception-message for more information)
     */
    public static ReadConnection createSNMPv3Connection(String address, int port, String username,
                                                        String authPassword,
                                                        String privPassword) throws ConnectionException {
        ReadConnection connection = new SNMPv3Connection(address, port, username, authPassword, privPassword);
        connection.establish();
        return connection;
    }

}
