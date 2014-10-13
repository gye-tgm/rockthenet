package rockthenet.connections.ssh;

import com.jcraft.jsch.*;
import java.io.IOException;
import java.io.PrintStream;

import rockthenet.connections.ConnectionException;
import rockthenet.connections.WriteConnection;

/* TODO: maybe open `commander` only once */

public class SSHConnection implements WriteConnection {
	Session session;
	
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
			throw new ConnectionException("failed to execute command");
		}
	}
}
