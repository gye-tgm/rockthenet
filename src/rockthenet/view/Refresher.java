package rockthenet.view;

/**
 * Created by Niko on 05/10/14.
 */
public class Refresher extends Thread {

    private int interval;
    private Controller controller;

    public Refresher(int interval, Controller controller){
        this.interval = interval;
        this.controller = controller;
        this.setDaemon(true);
        start();
    }

    public void run(){
        try{
            while(true){
                sleep(interval*1000);
                controller.refresh();
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
