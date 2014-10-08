package rockthenet;

public class SessionSettings {
	/* this class is a Singleton */
	private static SessionSettings sessionSettings = new SessionSettings();
	private SessionSettings() {}
	public static SessionSettings getInstance() { return sessionSettings; }
	
	private int refreshInterval = 5;
	
	/* Setters */
	public void setRefreshInterval(int newInterval) { this.refreshInterval = newInterval; }
	
	/* Getters */
	public int getRefreshInterval() { return refreshInterval; }
}
