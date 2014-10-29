package rockthenet;

import rockthenet.firewall.Firewall;

/**
 * This is a global singleton class managing the settings of a single session. 
 * (such as the firewall connected to, the configured refresh interval, ...)
 * 
 * @author Elias Frantar
 * @version 2014-10-29
 */
public class SessionSettings {

    public static final int DEFAULT_INTERVAL = 5;
    /* this class is a Singleton */
	private static SessionSettings sessionSettings = new SessionSettings();
	private SessionSettings() {} // no instance should be created from outside
	/**
	 * Returns the singleton-instance of this class
	 * @return the only existing instance of this class
	 */
	public static SessionSettings getInstance() { return sessionSettings; }
	
	/* General Settings */
	private Firewall firewall;
	private boolean loggedIn;
	private String host;
    private Refresher refresher;

    /* Settings Dialog */
	private String email = "";

	/**
	 * Returns true if this object contains an instance of a {@link Firewall}. ({@code firewall != null})
	 * @return true if yes; false otherwise
	 */
	public boolean isConnected() { return firewall != null; }
	
	/* Setters */
	public void setRefreshInterval(int newInterval) {
        refresher.setRefreshInterval(newInterval);
    }
	public void setEmail(String email) { this.email = email; }
	public void setFirewall(Firewall firewall) { this.firewall = firewall; }
	public void setLoggedIn(boolean loggedIn) { this.loggedIn = loggedIn; }
	public void setHost(String host) { this.host = host; }
    /* Getters */
	public int getRefreshInterval() { return refresher.getRefreshInterval(); }
	public String getEmail() { return email; }
	public Firewall getFirewall() { return firewall; }
	public boolean getLoggedIn() { return loggedIn; }
	public String getHost() { return host; }

    public void setRefresher(Refresher refresher) {
        this.refresher = refresher;
    }

    public Refresher getRefresher() {
        return refresher;
    }
}
