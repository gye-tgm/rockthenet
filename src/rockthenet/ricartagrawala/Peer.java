package rockthenet.ricartagrawala;

import org.apache.log4j.Logger;
import rockthenet.utils.Listener;
import rockthenet.connections.MulticastConnection;

import java.io.IOException;
import java.net.DatagramPacket;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Implements a Peer for the Ricart Agrawala algorithm.
 *
 * @author Gary Ye
 * @version 2014-10-31
 */
public class Peer implements Listener {
    private static org.apache.log4j.Logger log = Logger.getLogger(Peer.class);

    private Message myRequest;
    private Queue<Message> otherRequests;
    private MulticastConnection multicastConnection;
    private int numberAccept;
    private int groupSize;

    public Peer() {
        this(new MulticastConnection());
    }

    /**
     * Constructs a Peer with a given, not started, multicast connection.
     *
     * @param multicastConnection the multicast connection
     */
    public Peer(MulticastConnection multicastConnection) {
        this.myRequest = null;
        this.otherRequests = new LinkedList<>();
        this.multicastConnection = multicastConnection;
    }

    /**
     * Starts the multicast connection.
     */
    public void connect() {
        multicastConnection.setListener(this);
        multicastConnection.start();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Locks all other users in the Multicast connection from doing any further actions.
     */
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

    /**
     * Unlocks, so that all other users can do their things.
     */
    public synchronized void unlock() {
        while (!otherRequests.isEmpty()) {
            try {
                replyOK(otherRequests.poll());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        myRequest = null;
    }

    /**
     * Responds to the given request with an OK.
     *
     * @param request the request to respond to
     * @throws IOException will be thrown if sending was not successful
     */
    public synchronized void replyOK(Message request) throws IOException {
        multicastConnection.send(new Message(Message.MessageType.OK, multicastConnection.getInetAddress(),
                request.getSrcAddress()));
    }

    /**
     * Responds to the given request with a DENY.
     *
     * @param request the request to respond to
     * @throws IOException will be thrown if sending was not successful
     */
    public synchronized void replyDeny(Message request) throws IOException {
        multicastConnection.send(new Message(Message.MessageType.DENY, multicastConnection.getInetAddress(),
                request.getSrcAddress()));
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

        if (otherRequest.getDstAddress() != null && otherRequest.getDstAddress() != multicastConnection
                .getInetAddress())
            return;

        try {
            switch (otherRequest.getType()) {
                case REQUEST:
                    log.debug("Received Request message from " + otherRequest.getSrcAddress());
                    handleRequest(otherRequest);
                    break;
                case OK:
                    log.debug("Received OK message from " + otherRequest.getSrcAddress());
                    handleOK(otherRequest);
                    break;
                case DENY:
                    log.debug("Received DENY message from " + otherRequest.getSrcAddress());
                    handleDeny(otherRequest);
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles a deny message. Should be called if a deny message was received
     *
     * @param otherRequest the deny request to handle
     */
    private void handleDeny(Message otherRequest) {
        // TODO: Maybe check with IP
        groupSize++;
    }

    /**
     * Handles an OK message. Should be called if the
     *
     * @param otherRequest the OK request ot handle
     */
    private void handleOK(Message otherRequest) {
        // TODO: Maybe check with IP
        groupSize++;
        numberAccept++;
    }

    /**
     * Handle request, wrapper method.
     *
     * @param otherRequest the request to handle
     * @throws IOException will be thrown if replying was not successful
     */
    private void handleRequest(Message otherRequest) throws IOException {
        if (myRequest == null) {
            replyOK(otherRequest);
        } else {
            otherRequests.add(otherRequest);
            replyDeny(otherRequest);
        }
    }
}
