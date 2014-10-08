package rockthenet;


import rockthenet.Refreshable;

/**
 * Created by Niko on 05/10/14.
 */
public class Refresher extends Thread {
    private Refreshable refreshObject;

    /**
     *
     * @param refreshObject the object to refresh
     */
    public Refresher(Refreshable refreshObject){
        this.refreshObject = refreshObject;
        this.setDaemon(true);
    }

    public void run(){
        try{
            while(true){
                refreshObject.refresh();
                sleep(SessionSettings.getInstance().getRefreshInterval() * 1000);
            }
        }catch(InterruptedException e) {
            System.out.println("interrupted.");
        }
    }
}
