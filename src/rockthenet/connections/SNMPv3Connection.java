package rockthenet.connections;

import java.io.IOException;

import org.snmp4j.Snmp;
import org.snmp4j.UserTarget;
import org.snmp4j.mp.MPv3;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.security.AuthMD5;
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

/* TODO: test functionality */

public class SNMPv3Connection extends SNMPConnection {
	public static OID AUTH_PROTOCOL = AuthMD5.ID;
	public static OID PRIVACY_PROTOCOL = PrivDES.ID;
	
	private UsmUser user;
	
	protected SNMPv3Connection(String address, int port, String username, String authPassword, String privPassword) throws ConnectionException {
		this(address, port, username, authPassword, privPassword, AUTH_PROTOCOL, PRIVACY_PROTOCOL);
	}
	
	protected SNMPv3Connection(String address, int port, String username, String authPassword, String privPassword, OID authProtocol, OID privProtocol) throws ConnectionException {
		Address targetAddress = parseAddress(address, port);
		
		UserTarget target = new UserTarget();
		target.setVersion(SnmpConstants.version3);
		target.setAddress(targetAddress);
		target.setSecurityLevel(SecurityLevel.AUTH_PRIV);
		target.setSecurityName(new OctetString("MD5DES"));
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
