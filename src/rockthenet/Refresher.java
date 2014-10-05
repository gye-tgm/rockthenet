package rockthenet;


import rockthenet.Refreshable;

/**
 * Created by Niko on 05/10/14.
 */
public class Refresher extends Thread {
    private int interval;
    private Refreshable refreshObject;

    /**
     *
     * @param interval the interval in milliseconds
     * @param refreshObject the object to refresh
     */
    public Refresher(int interval, Refreshable refreshObject){
        this.interval = interval;
        this.refreshObject = refreshObject;
    }

    public void run(){
        try{
            while(true){
                refreshObject.refresh();
                sleep(interval);
            }
        }catch(InterruptedException e) {
            System.out.println("interrupted.");
        }
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }


}
