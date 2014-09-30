package rockthenet.connections;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

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
import org.snmp4j.util.DefaultPDUFactory;
import org.snmp4j.util.TreeEvent;
import org.snmp4j.util.TreeUtils;

public class SNMPv2cConnection implements ReadConnection {
	private static final int MAX_TREE_SIZE = 500;
	
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
	public VariableBinding get(String oid) throws ConnectionException {	
		return get(new String[]{oid})[0]; // return only one VariableBinding
	}

	@Override
	public VariableBinding[] get(String[] oids) throws ConnectionException { // TODO: verify behavior defined in documentation
		PDU pdu = new PDU();
		for (String oid : oids)
			pdu.add(new VariableBinding(new OID(oid)));
	    pdu.setType(PDU.GET);

	    try {
	    	ResponseEvent response = snmp.send(pdu, target);
		    return response.getResponse().getVariableBindings().toArray(new VariableBinding[oids.length]);
	    } catch (IOException e) {
			throw new ConnectionException();
		}
	}
	
	@Override
	public VariableBinding[] getTable(String rootOID) throws ConnectionException { /// TODO: throw the exception somehwere
		TreeUtils treeUtils = new TreeUtils(snmp, new DefaultPDUFactory());
		treeUtils.setMaxRepetitions(MAX_TREE_SIZE);
		return treeUtils.getSubtree(target, new OID(rootOID)).get(0).getVariableBindings(); // in a single-OID request, it's always 0
	}
}
