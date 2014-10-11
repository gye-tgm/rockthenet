package rockthenet;

public class SessionSettings {
	/* this class is a Singleton */
	private static SessionSettings sessionSettings = new SessionSettings();
	private SessionSettings() {}
	public static SessionSettings getInstance() { return sessionSettings; }
	
	/* Settings Dialog */
	private String email = "";
	private int refreshInterval = 5;
	
	/* Setters */
	public void setRefreshInterval(int newInterval) { this.refreshInterval = newInterval; }
	public void setEmail(String email) { this.email = email; }
	
	/* Getters */
	public int getRefreshInterval() { return refreshInterval; }
	public String getEmail() { return email; }
}
