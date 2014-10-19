package rockthenet.connections;

/**
 * The base interface for all connections providing write access.<br>
 * Defines a method for remotely executing a command.
 * 
 * @author Elias Frantar
 * @version 2014-10-19
 */
public interface WriteConnection extends Connection {

	/**
	 * Executes the given command on the connected remote machine.
	 * 
	 * @param command the command to execute
	 * @throws ConnectionException thrown if execution failed (see Exception-message for more information)
	 */
	public void execute(String command) throws ConnectionException;

}
