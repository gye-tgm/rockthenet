package rockthenet.connections.ssh;

import com.jcraft.jsch.*;
import java.io.IOException;
import java.io.PrintStream;

import rockthenet.connections.ConnectionException;
import rockthenet.connections.WriteConnection;

/* TODO: maybe open `commander` only once */

/**
 * This class implements a connection via the <i>SSH</i> protocol.
 * 
 * @author Elias Frantar
 * @version 2014-10-19
 */
public class SSHConnection implements WriteConnection {
	Session session;
	
	/**
	 * Creates a new {@link rockthenet.connections.snmp.SSHConnection}
	 * 
	 * <p><i>Note:</i> {@link #establish()} must be called before usage!
	 * 
	 * @param host the host address of the SSH-server (IP or URL)
	 * @param username the username to use for connecting
	 * @param password the corresponding password
	 * 
	 * @throws ConnectionException thrown if connecting failed (see Exception-message for more information)
	 */
	public SSHConnection(String host, String username, String password) throws ConnectionException {
		try {
			JSch.setConfig("StrictHostKeyChecking", "no"); // ignore unknown host key prompts
			
			session = new JSch().getSession(username, host);
			session.setPassword(password);
		} catch (JSchException e) {
			throw new ConnectionException("SSH error");
		}
	}
	
	@Override
	public void establish() throws ConnectionException {
		try {
			session.connect();
		} catch (JSchException e) {
			throw new ConnectionException("connection failed");
		}
	}

	@Override
	public void close() {
		session.disconnect();
	}

	@Override
	public void execute(String command) throws ConnectionException {
		try {
			Channel channel = (ChannelShell) session.openChannel("shell");
            PrintStream commander = new PrintStream(channel.getOutputStream(), true);
            
            /* connect */
            channel.setOutputStream(System.out, true);
            channel.connect();
            
            /* execute command */
            commander.println(command);
            commander.flush();
            
            /* close channel */
            commander.close();
			channel.disconnect();
		} catch (IOException | JSchException e) {
			throw new ConnectionException("Failed to execute command");
		}
	}
}
