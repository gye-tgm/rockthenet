package test.ricartagrawala;

import org.junit.Test;
import rockthenet.connections.MulticastConnection;
import rockthenet.ricartagrawala.Peer;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author Gary Ye
 */
public class PeerTest {
    private Peer peer1;
    private Peer peer2;

    @Test
    public void peerLock() throws UnknownHostException {
        InetAddress group = InetAddress.getByName("224.0.0.1");
        int port = 6667;

        peer1 = new Peer(new MulticastConnection(group, port));
        peer1.connect();
        peer2 = new Peer(new MulticastConnection(group, port));
        peer2.connect();

        peer1.lock();
        peer1.unlock();
    }
}
