package rockthenet.connections;

import java.io.IOException;

import org.snmp4j.PDU;
import org.snmp4j.ScopedPDU;
import org.snmp4j.Snmp;
import org.snmp4j.Target;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.util.DefaultPDUFactory;
import org.snmp4j.util.RetrievalEvent;
import org.snmp4j.util.TreeEvent;
import org.snmp4j.util.TreeUtils;

import java.util.List;

/**
 * The abstract base-class for all SNMP-type {@link Connection}s.<br>
 * Contains generic implementations for all {@code get()}-requests.
 * 
 * <p>When extending this class, {@link #establish()} has to be implemented. 
 * In addition you will also have to assign the proper Objects to {@code snmp} and {@code target} 
 * if you want to use this class' {@code get()}-implementations.
 * 
 * @author Elias Frantar
 * @version 2014-10-05
 */
public abstract class SNMPConnection implements ReadConnection {
	/**
	 * The protocol used for connecting to the SNMP-server (UDP)
	 */
	public static final String CONNECTION_PROTOCOL = "udp";
	/**
	 * The maximum number of {@link VariableBinding}s returned by {@link #getTable(String)}
	 */
	public static final int MAX_TREE_SIZE = 500;
	
	protected Snmp snmp;
	protected Target target;
	
	/**
	 * Attempts to create an SNMP-address from the given information.
	 * 
	 * @param address the address the address of the SNMP-server (IP or URL)
	 * @param port the port of the SNMP-server
	 * @return the converted address
	 * @throws ConnectionException thrown if the given parameters do not form a valid address
	 */
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
		PDU pdu;
		if (target.getVersion() == SnmpConstants.version3) // choose the correct PDU-type depending on protocol version
			pdu = new ScopedPDU();
		else
			pdu = new PDU();
		
		for (String oid : oids)
			pdu.add(new VariableBinding(new OID(oid)));
	    pdu.setType(PDU.GET);

	    try {
	    	ResponseEvent response = snmp.send(pdu, target);
	    	
	    	if (response.getResponse() == null) // connection-error
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
		
		if (result.get(0).getStatus() != RetrievalEvent.STATUS_OK) // connection-error
			throw new ConnectionException("request failed");
		
		return result.get(0).getVariableBindings(); // in a single-OID request, it's always 0
	}
}
