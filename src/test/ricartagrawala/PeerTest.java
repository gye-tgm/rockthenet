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

    @Test
    public void peerLock() throws UnknownHostException {
        InetAddress group = InetAddress.getByName("224.0.0.1");
        int port = 6667;

        peer1 = new Peer(new MulticastConnection(group, port));
        peer1.connect();

        peer1.lock();
        for(int i = 0; i < 200; i++)
            for(int j = 0; j < 1000; j++)
                System.out.println(i + " " +  j);
        peer1.unlock();
    }
}
