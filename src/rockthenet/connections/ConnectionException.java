package rockthenet.connections;

@SuppressWarnings("serial")
public class ConnectionException extends Exception {
	private String message = "unknown cause";
	
	public ConnectionException() {}
	public ConnectionException(String message) {
		this.message = message;
	}
	
	@Override
	public String toString() {
		return "ConnectionException: " + message;
	}
}
