package rockthenet.connections.snmp;

import java.io.IOException;

import org.snmp4j.Snmp;
import org.snmp4j.UserTarget;
import org.snmp4j.mp.MPv3;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.security.AuthSHA;
import org.snmp4j.security.PrivDES;
import org.snmp4j.security.SecurityLevel;
import org.snmp4j.security.SecurityModels;
import org.snmp4j.security.SecurityProtocols;
import org.snmp4j.security.USM;
import org.snmp4j.security.UsmUser;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import rockthenet.connections.ConnectionException;

/**
 * This class implements a connection via the <i>SNMPv3</i> protocol.
 * 
 * @author Elias Frantar
 * @version 2014-10-06
 */
public class SNMPv3Connection extends SNMPConnection {
	/**
	 * The protocol/algorithm used for authentication (SHA)
	 */
	public static OID AUTH_PROTOCOL = 	AuthSHA.ID;
	/**
	 * The protocol/algorithm used for privacy (DES)
	 */
	public static OID PRIV_PROTOCOL = 	PrivDES.ID;
	
	private UsmUser user;
	
	/**
	 * Equivalent to {@code SNMPv3Connection(address, port, username, authPassword, privPassword, AUTH_PROTOCOL, PRIV_PROTOCOL)
	 * 
	 * @see #SNMPv3Connection(String, int, String, String, String, OID, OID)
	 */
	protected SNMPv3Connection(String address, int port, String username, String authPassword, String privPassword) throws ConnectionException {
		this(address, port, username, authPassword, privPassword, AUTH_PROTOCOL, PRIV_PROTOCOL);
	}
	/**
	 * Creates a new connection
	 * 
	 * <p><i>Note:</i> {@link #establish()} must be called before usage!
	 * 
	 * @param address the address of the SNMP-server (IP or URL)
	 * @param port the port of the SNMP-server
	 * @param username the name of the user
	 * @param authPassword the authentication password
	 * @param privPassword the privacy password
	 * @param authProtocol the authentication protocol (algorithm)
	 * @param privProtocol the privacy protocol (algorithm)
	 * 
	 * @throws ConnectionException thrown if invalid address-parameters where passed
	 */
	protected SNMPv3Connection(String address, int port, String username, String authPassword, String privPassword, OID authProtocol, OID privProtocol) throws ConnectionException {
		Address targetAddress = parseAddress(address, port);
		
		UserTarget target = new UserTarget();
		target.setVersion(SnmpConstants.version3);
		target.setAddress(targetAddress);
		target.setSecurityLevel(SecurityLevel.AUTH_PRIV);
		target.setSecurityName(new OctetString("SHADES")); // SHA and DES
		this.target = target;
		
		this.user = new UsmUser(new OctetString(username), authProtocol, new OctetString(authPassword), privProtocol, new OctetString(privPassword));
	}
	
	@Override
	public void establish() throws ConnectionException {
		if (snmp != null) // in case we are already up and want to reconnect
			close();
		
		try {
			snmp = new Snmp(new DefaultUdpTransportMapping());
			SecurityModels.getInstance().addSecurityModel(new USM(SecurityProtocols.getInstance(), new OctetString(MPv3.createLocalEngineID()), 0));
			snmp.listen();
			snmp.getUSM().addUser(user);
		} catch (IOException e) {
			throw new ConnectionException();
		}
		
		get("1.0"); // just a SNMP-ping to verify that the requested SNMP-host is reachable
	}
}
