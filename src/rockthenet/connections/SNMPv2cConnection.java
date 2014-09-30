package rockthenet.connections;

import java.io.IOException;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.Target;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

public class SNMPv2cConnection implements ReadConnection {
	private Snmp snmp;
	private Target target;
	
	protected SNMPv2cConnection(String address, int port, String community) {
		Address targetAddress = GenericAddress.parse("udp:" + address + "/" + port);
		
		CommunityTarget target = new CommunityTarget();
		target.setVersion(SnmpConstants.version2c);
		target.setAddress(targetAddress);
		target.setCommunity(new OctetString(community));
		this.target = target;
	}

	@Override
	public void establish() throws ConnectionException { // TODO: verify behavior defined in documentation
		try {
			snmp = new Snmp(new DefaultUdpTransportMapping());
			snmp.listen();
		} catch (IOException e) {
			throw new ConnectionException();
		}
	}

	@Override
	public void close() { // TODO: verify behavior defined in documentation
		try {
			snmp.close();
		} catch (IOException e) {} // if there is an Exception, the connection should have already been closed anyways
	}

	@Override
	public PDU get(String oid) throws ConnectionException {	
		return get(new String[]{oid});
	}

	@Override
	public PDU get(String[] oids) throws ConnectionException { // TODO: verify behavior defined in documentation
		PDU pdu = new PDU();
		for (String oid : oids)
			pdu.add(new VariableBinding(new OID(oid)));
	    pdu.setType(PDU.GET);

	    try {
	    	ResponseEvent response = snmp.send(pdu, target);
		    return response.getResponse();
	    } catch (IOException e) {
			throw new ConnectionException();
		}
	}
}
