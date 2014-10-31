package rockthenet.connections.snmp;

import org.snmp4j.CommunityTarget;
import org.snmp4j.Snmp;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.OctetString;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import rockthenet.connections.ConnectionException;

import java.io.IOException;

/**
 * This class implements a connection via the <i>SNMPv2c</i> protocol.
 *
 * @author Elias Frantar
 * @version 2014-10-05
 */
public class SNMPv2cConnection extends SNMPConnection {

    /**
     * Creates a new connection
     * <p>
     * <p><i>Note:</i> {@link #establish()} must be called before usage!
     *
     * @param address       the address of the SNMP-server (IP or URL)
     * @param port          the port of the SNMP-server
     * @param communityName the name of the community to connect to
     * @param securityName  the security name of the community to connect to
     * @throws ConnectionException thrown if invalid address-parameters where passed
     */
    protected SNMPv2cConnection(String address, int port, String communityName,
                                String securityName) throws ConnectionException {
        Address targetAddress = parseAddress(address, port);

        CommunityTarget target = new CommunityTarget();
        target.setVersion(SnmpConstants.version2c);
        target.setAddress(targetAddress);
        target.setCommunity(new OctetString(communityName));
        target.setSecurityName(new OctetString(securityName));
        this.target = target;
    }

    @Override
    public void establish() throws ConnectionException {
        if (snmp != null) // in case we are already up and want to reconnect
            close();

        try {
            snmp = new Snmp(new DefaultUdpTransportMapping());
            snmp.listen();
        } catch (IOException e) {
            throw new ConnectionException();
        }

        get("1.0"); // just a SNMP-ping to verify that the requested SNMP-host is reachable
    }
}
