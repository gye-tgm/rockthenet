package rockthenet.connections.ssh;

import com.jcraft.jsch.*;
import java.io.IOException;
import java.io.PrintStream;

import rockthenet.connections.ConnectionException;
import rockthenet.connections.WriteConnection;
import org.apache.log4j.Logger;

/**
 * This class implements a connection via the <i>SSH</i> protocol.
 * 
 * @author Elias Frantar
 * @version 2014-10-19
 */
public class SSHConnection implements WriteConnection {
    private static org.apache.log4j.Logger log = Logger.getLogger(SSHConnection.class);

    Session session;
    Channel channel;
    PrintStream commander;
	
	/**
	 * Creates a new {@link rockthenet.connections.ssh.SSHConnection}
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
			
			/* connect the channel */
			channel = (ChannelShell) session.openChannel("shell");
            channel.setOutputStream(System.out, true);
            channel.connect();
            
            commander = new PrintStream(channel.getOutputStream(), true);
		} catch (JSchException | IOException e) {
			throw new ConnectionException("connection failed");
		}
	}

	@Override
	public void close() {
		channel.disconnect();
		session.disconnect();
		commander.close();
	}

	@Override
	public void execute(String command) throws ConnectionException {
        log.info(command);
            
        /* execute command */
        commander.println(command);
        commander.flush();
	}
}
