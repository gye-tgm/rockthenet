package rockthenet.connections;

/**
 * This Exception is thrown when a connection-error occurred.<br>
 * It should contain a message on what probably happened.
 *
 * @author Elias Frantar
 * @version 2014-10-05
 */
@SuppressWarnings("serial") // we don't need a serial-ID
public class ConnectionException extends Exception {
    private String message;

    /* constructors; self explanatory */
    public ConnectionException() {
        message = "unknown cause";
    }

    public ConnectionException(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ConnectionException: " + message;
    }

    public String getMessage() {
        return message;
    }
}
