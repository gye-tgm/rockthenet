package rockthenet.connections;

import java.io.IOException;

import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.Target;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.util.DefaultPDUFactory;
import org.snmp4j.util.RetrievalEvent;
import org.snmp4j.util.TreeEvent;
import org.snmp4j.util.TreeUtils;

import java.util.List;

public abstract class SNMPConnection implements ReadConnection {
	public static final String CONNECTION_PROTOCOL = "udp";
	public static final int MAX_TREE_SIZE = 500;
	
	protected Snmp snmp;
	protected Target target;
	
	protected Address parseAddress(String address, int port) throws ConnectionException {
		Address parsedAddress = GenericAddress.parse("udp:" + address + "/" + port);
		
		if (parsedAddress == null)
			throw new ConnectionException("invalid address");
		
		return parsedAddress;
	}
	
	@Override
	public void close() {
		try {
			snmp.close();
		} catch (IOException e) {} // if there is an Exception, the connection should have already been closed anyways
	}

	@Override
	public VariableBinding get(String oid) throws ConnectionException {	
		return get(new String[]{oid})[0]; // return only one VariableBinding
	}

	@Override
	public VariableBinding[] get(String[] oids) throws ConnectionException {
		PDU pdu = new PDU();
		for (String oid : oids)
			pdu.add(new VariableBinding(new OID(oid)));
	    pdu.setType(PDU.GET);

	    try {
	    	ResponseEvent response = snmp.send(pdu, target);
	    	
	    	if (response.getResponse() == null)
	    		throw new ConnectionException("no response from target");
	    	
		    return response.getResponse().getVariableBindings().toArray(new VariableBinding[oids.length]);
	    } catch (IOException e) {
			throw new ConnectionException("message could not be sent");
		}
	}
	
	@Override
	public VariableBinding[] getTable(String rootOID) throws ConnectionException {
		TreeUtils treeUtils = new TreeUtils(snmp, new DefaultPDUFactory());
		treeUtils.setMaxRepetitions(MAX_TREE_SIZE);
		
		List<TreeEvent> result = treeUtils.getSubtree(target, new OID(rootOID));
		
		if (result.get(0).getStatus() != RetrievalEvent.STATUS_OK)
			throw new ConnectionException("request failed");
		
		return result.get(0).getVariableBindings(); // in a single-OID request, it's always 0
	}
}
