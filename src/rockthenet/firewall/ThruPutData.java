package rockthenet.firewall;

/**
 * The class is used for saving the history of the thru put of the policies. With
 * the given time unit and the given bytes per second, we can build a line chart out of it.
 *
 * @author Gary Ye
 */
public class ThruPutData {
    private int timeUnit;
    private int bytesPerSec;

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