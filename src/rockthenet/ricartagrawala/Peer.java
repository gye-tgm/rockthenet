package rockthenet.ricartagrawala;

import rockthenet.Listener;
import rockthenet.connections.MulticastConnection;

import java.io.IOException;
import java.net.DatagramPacket;
import java.util.LinkedList;
import java.util.Queue;

/**
 * @author Gary Ye
 */
public class Peer implements Listener {
    private Message myRequest;
    private Queue<Message> otherRequests;
    private MulticastConnection multicastConnection;
    private int numberAccept;
    private int groupSize;

    public Peer() {
        this(new MulticastConnection());
    }
    public Peer(MulticastConnection multicastConnection){
        this.myRequest = null;
        this.otherRequests = new LinkedList<>();
        this.multicastConnection = multicastConnection;
    }

    public void connect(){
        multicastConnection.setListener(this);
        multicastConnection.start();
    }

    public synchronized void lock() {
        myRequest = new Message(Message.MessageType.REQUEST, multicastConnection.getInetAddress());
        try {
            multicastConnection.send(myRequest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        numberAccept = 0;
        groupSize = 0;

        // Waits until everyone sent OK
        do {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (numberAccept < groupSize);
    }

    public synchronized void unlock() {
        while (!otherRequests.isEmpty()) {
            try {
                replyOK(otherRequests.poll());
            }  catch (IOException e) {
                e.printStackTrace();
            }
        }
        myRequest = null;
    }

    public synchronized void replyOK(Message request) throws IOException {
        multicastConnection.send(new Message(Message.MessageType.OK, multicastConnection.getInetAddress(), request.getSrcAddress()));
    }

    public synchronized void replyDeny(Message request) throws IOException {
        multicastConnection.send(new Message(Message.MessageType.DENY, multicastConnection.getInetAddress(), request.getSrcAddress()));
    }

    @Override
    public synchronized void notify(Object obj) {
        DatagramPacket datagramPacket = (DatagramPacket) obj;
        Message otherRequest = null;
        try {
            otherRequest = (Message) MulticastConnection.deserialize(datagramPacket.getData());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        if (otherRequest.getDstAddress() != null && otherRequest.getDstAddress() != multicastConnection.getInetAddress())
            return;
        try {
            switch (otherRequest.getType()) {
                case REQUEST:
                    handleRequest(otherRequest);
                    break;
                case OK:
                    handleOK(otherRequest);
                    break;
                case DENY:
                    handleDeny(otherRequest);
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleDeny(Message otherRequest) {
        // TODO: Maybe check with IP
        groupSize++;
    }

    private void handleOK(Message otherRequest) {
        // TODO: Maybe check with IP
        groupSize++;
        numberAccept++;
    }

    private void handleRequest(Message otherRequest) throws IOException {
        if (myRequest == null) {
            replyOK(otherRequest);
        } else {
            otherRequests.add(otherRequest);
            replyDeny(otherRequest);
        }
    }
}
