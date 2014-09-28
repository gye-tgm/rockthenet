package connections;

public class SNMPv2cConnection implements ReadConnection {
	
	protected SNMPv2cConnection(String address, int port, String community) {
		
	}

	@Override
	public void establish() throws ConnectionException {
	
	}

	@Override
	public void close() {
	
	}

	@Override
	public void get(String oid) throws ConnectionException {	
	
	}

	@Override
	public void get(String[] oids) throws ConnectionException {
	
	}
}
