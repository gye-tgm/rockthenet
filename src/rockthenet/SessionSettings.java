package rockthenet;

import rockthenet.connections.WriteConnection;
import rockthenet.firewall.Firewall;

public class SessionSettings {
	/* this class is a Singleton */
	private static SessionSettings sessionSettings = new SessionSettings();
	private SessionSettings() {}
	public static SessionSettings getInstance() { return sessionSettings; }
	
	/* General Settings */
	private Firewall firewall;
    private WriteConnection writeConnection;

    /* Settings Dialog */
	private String email = "";
	private int refreshInterval = 5;
	
	public boolean isConnected() { return firewall != null; }
	/* Setters */
	public void setRefreshInterval(int newInterval) { this.refreshInterval = newInterval; }
	public void setEmail(String email) { this.email = email; }
	public void setFirewall(Firewall firewall) { this.firewall = firewall; }

    public void setWriteConnection(WriteConnection writeConnection) {
        this.writeConnection = writeConnection;
    }

    /* Getters */
	public int getRefreshInterval() { return refreshInterval; }
	public String getEmail() { return email; }
	public Firewall getFirewall() { return firewall; }

    public WriteConnection getWriteConnection() {
        return writeConnection;
    }
}
