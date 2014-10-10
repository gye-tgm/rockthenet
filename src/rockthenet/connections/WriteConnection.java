package rockthenet.connections;

public interface WriteConnection extends Connection {

	public void execute(String command) throws ConnectionException;

}
