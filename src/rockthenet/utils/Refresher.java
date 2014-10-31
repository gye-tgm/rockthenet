package rockthenet.utils;

import rockthenet.utils.Refreshable;

/**
 * This class refreshes a refreshable object by calling their {@code refresh()} method.
 *
 * @author Nikolaus Schrack
 * @author Gary Ye
 * @version 2014-10-29
 */
public class Refresher extends Thread {
    private Refreshable refreshObject;
    private int refreshInterval;

    /**
     * Constructs a new refresher.
     *
     * @param refreshObject   the object to refresh
     * @param refreshInterval the interval to refresh in seconds
     */
    public Refresher(Refreshable refreshObject, int refreshInterval) {
        this.refreshObject = refreshObject;
        this.refreshInterval = refreshInterval;
    }

    public void run() {
        try {
            while (true) {
                refreshObject.refresh();
                sleep(refreshInterval);
            }
        } catch (InterruptedException e) {
            System.out.println("interrupted.");
        }
    }

    public void setRefreshInterval(int refreshInterval) {
        this.refreshInterval = refreshInterval;
    }

    public int getRefreshInterval() {
        return refreshInterval;
    }
}
