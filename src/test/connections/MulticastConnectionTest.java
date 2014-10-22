package test.connections;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import rockthenet.connections.ConnectionException;
import rockthenet.connections.MulticastConnection;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.junit.Assert.assertEquals;

/**
 * Tests the given Multicast Connection class
 * @author Gary Ye
 */
public class MulticastConnectionTest {
    private MulticastConnection client1, client2;
    private InetAddress group ;
    private int port = 6666;

    @Before
    public void setup() throws ConnectionException, UnknownHostException {
        group = InetAddress.getByName("224.0.0.1");
        client1 = new MulticastConnection(group, port);
        client1.establish();

        client2 = new MulticastConnection(group, port);
        client2.establish();
    }

    @Ignore
    @Test
    public void testSend() throws IOException, ClassNotFoundException {
        String hallo = "abc";
        client1.send(hallo);
        DatagramPacket datagramPacket = client2.receive();
        String received = (String) MulticastConnection.deserialize(datagramPacket.getData());
        assertEquals(hallo, received);
    }
}
