package rockthenet.connections;

import org.snmp4j.PDU;

public interface ReadConnection extends Connection {
	public PDU get(String oid) throws ConnectionException;
	public PDU get(String[] oids) throws ConnectionException;
}
