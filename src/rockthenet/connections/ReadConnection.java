package rockthenet.connections;

import org.snmp4j.smi.VariableBinding;

/**
 * The base interface for all connections providing read access over <i>SNMP</i>.<br>
 * Defines methods for fetching values for given OIDs
 * 
 * @author Elias Frantar
 * @version 2014-09-28
 */
public interface ReadConnection extends Connection { // TODO: maybe rename this to `SNMPReadConnection`
	
	/**
	 * Returns the received <i>data unit</i> from requesting the given OID<br>
	 * (equivalent to the call <code>get(String[]{oid})</code>)
	 * 
	 * @see #get(String[])
	 */
	public VariableBinding get(String oid) throws ConnectionException;
	
	/**
	 * Returns the received <i>data unit</i> from requesting the given OIDs
	 * 
	 * <p><i>NOTE:</i> If a non existing OID is passed, this method will return a PDU with value <i>noSuchObject</i> at
	 * that OIDs position.
	 * 
	 * @param oids the identifier of the data unit to return
	 * @return the data unit for the given OIDs
	 * @throws ConnectionException thrown if requesting failed
	 */
	public VariableBinding[] get(String[] oids) throws ConnectionException;
	
	/**
	 * Returns the data
	 * 
	 * @param rootOID
	 * @return
	 */
	public VariableBinding[] getTable(String rootOID) throws ConnectionException;

}
