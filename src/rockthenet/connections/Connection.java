package rockthenet.connections;

/**
 * The base interface for all connections.<br>
 * Defines methods for establishing and closing a connection
 *
 * @author Elias Frantar
 * @version 2014-09-28
 */
public interface Connection {

    /**
     * Tries to establish a connection to the host/service. (must have been previously initialized)
     * <p>
     * <p><i>NOTE:</i> If this method is called while the connection is up, it will simply try to reconnect.
     * You therefore may also use this method for reconnecting when the connection has been lost.
     *
     * @throws ConnectionException thrown if connecting failed
     */
    public void establish() throws ConnectionException;

    /**
     * Closes the connection if it is running; Doesn't do anything otherwise
     */
    public void close();

}
