package rockthenet.firewall;

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