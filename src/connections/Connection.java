package connections;

public interface Connection {
	public void establish() throws ConnectionException;
	public void close();
}
