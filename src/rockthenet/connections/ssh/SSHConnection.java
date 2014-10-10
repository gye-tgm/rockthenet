package rockthenet.connections.ssh;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.IOException;

import rockthenet.connections.ConnectionException;
import rockthenet.connections.WriteConnection;

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
			ChannelExec channel = (ChannelExec) session.openChannel("exec");
			channel.setCommand(command);
			
			channel.connect(); // execute command
			while(channel.getInputStream().read() != 0xffffffff); // wait until command is complete
			
			channel.disconnect(); // close command
		} catch (IOException | JSchException e) {
			throw new ConnectionException("failed to execute command");
		}
	}
}
