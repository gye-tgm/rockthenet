package rockthenet.connections;

import java.io.IOException;

import org.snmp4j.CommunityTarget;
import org.snmp4j.Snmp;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.OctetString;
import org.snmp4j.transport.DefaultUdpTransportMapping;

public class SNMPv2cConnection extends SNMPConnection {

	protected SNMPv2cConnection(String address, int port, String community) throws ConnectionException {
		Address targetAddress = parseAddress(address, port);
			
		CommunityTarget target = new CommunityTarget();
		target.setVersion(SnmpConstants.version2c);
		target.setAddress(targetAddress);
		target.setCommunity(new OctetString(community));
		this.target = target;
	}

	@Override
	public void establish() throws ConnectionException { // TODO: verify behavior defined in documentation
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
