package rockthenet.ricartagrawala;

import java.util.Queue;

/**
 * @author Gary Ye
 */
public class Peer extends Thread {
    private Request myRequest;
    private Queue<Request> otherRequests;

    public Peer(){
        myRequest = null;
    }

    @Override
    public void run(){
        // Listen for other messages
    }

    public boolean done(){
        return true;
    }

    public synchronized void lock(){
        myRequest = new Request();
        // Sends requests to all others . . .

        // Waits until everyone sent OK
        while (!done()) {
            // Busy waiting
        }
    }

    public synchronized void unlock(){
        while(!otherRequests.isEmpty()){
            reply(otherRequests.poll());
        }
        myRequest = null;
    }

    public synchronized void reply(Request request){

    }
}
