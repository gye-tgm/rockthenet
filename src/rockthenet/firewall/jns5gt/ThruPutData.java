package rockthenet.firewall.jns5gt;

/**
 * The class is used for saving the history of the thru put of the policies. With
 * the given time unit and the given bytes per second, we can build a line chart out of it.
 *
 * @author Gary Ye
 * @version 2014-10-29
 */
public class ThruPutData {
    private int timeUnit;
    private int bytesPerSec;

    /**
     * Constructs a new ThruPutData with the given time this data was recorded and the bytes per second.
     * @param timeUnit the time this data was recorded
     * @param bytesPerSec the bytes per second of the thruput
     */
    public ThruPutData(int timeUnit, int bytesPerSec) {
        this.timeUnit = timeUnit;
        this.bytesPerSec = bytesPerSec;
    }

    public int getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(int timeUnit) {
        this.timeUnit = timeUnit;
    }

    public int getBytesPerSec() {
        return bytesPerSec;
    }

    public void setBytesPerSec(int bytesPerSec) {
        this.bytesPerSec = bytesPerSec;
    }
}