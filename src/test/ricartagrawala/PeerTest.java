package test.ricartagrawala;

import org.junit.Ignore;
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

    @Ignore
    @Test
    public void peerLock() throws UnknownHostException, InterruptedException {
        InetAddress group = InetAddress.getByName("224.0.0.1");
        int port = 6667;

        peer1 = new Peer(new MulticastConnection(group, port));
        peer1.connect();

        peer1.lock();
        System.out.println("Inside");
        Thread.sleep(20000);
        System.out.println("Done");
        peer1.unlock();
    }
}
