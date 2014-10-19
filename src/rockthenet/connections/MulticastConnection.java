package rockthenet.connections;

import javax.xml.crypto.Data;
import java.io.*;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * @author Gary Ye
 */
public class MulticastConnection implements Connection {
    private InetAddress group;
    private MulticastSocket multicastSocket;
    private int port;

    public MulticastConnection(){}

    public MulticastConnection(InetAddress group, int port) {
        this.port = port;
        this.group = group;
    }

    /**
     * Tries to establish a connection to the host/service. (must have been previously initialized)
     * <p>
     * <p><i>NOTE:</i> If this method is called while the connection is up, it will simply try to reconnect.
     * You therefore may also use this method for reconnecting when the connection has been lost.
     *
     * @throws rockthenet.connections.ConnectionException thrown if connecting failed
     */
    @Override
    public void establish() throws ConnectionException {
        try {
            multicastSocket = new MulticastSocket(port);
            multicastSocket.joinGroup(group);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(obj);
        return out.toByteArray();
    }
    public static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(in);
        return is.readObject();
    }

    public void send(Object object) throws IOException {
        byte[] bytes = serialize(object);

        DatagramPacket packet = new DatagramPacket(bytes, bytes.length);
        multicastSocket.send(packet);
    }

    public DatagramPacket receive(){
        byte[] buf = new byte[1000];
        DatagramPacket datagramPacket = new DatagramPacket(buf, buf.length);
        try {
            multicastSocket.receive(datagramPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return datagramPacket;
    }


    /**
     * Closes the connection if it is running; Doesn't do anything otherwise
     */
    @Override
    public void close() {
        try {
            multicastSocket.leaveGroup(group);
            multicastSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
