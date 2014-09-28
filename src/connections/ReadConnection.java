package connections;

public interface ReadConnection extends Connection {
	public void get(String oid) throws ConnectionException;
	public void get(String[] oids) throws ConnectionException;
}
