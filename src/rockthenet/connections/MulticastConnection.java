package rockthenet.connections;

import java.io.*;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * This class provides a multicast connection.
 *
 * @author Gary Ye
 */
public class MulticastConnection implements Connection {
    private InetAddress group;
    private MulticastSocket multicastSocket;
    private int port;

    public MulticastConnection() {
    }

    public MulticastConnection(InetAddress group, int port) {
        this.port = port;
        this.group = group;
    }

    @Override
    public void establish() throws ConnectionException {
        try {
            multicastSocket = new MulticastSocket(port);
            multicastSocket.joinGroup(group);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        try {
            multicastSocket.leaveGroup(group);
            multicastSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Serializes the given object, which should have implemented Serizliable.
     *
     * @param obj the object to serialize.
     * @return the object as a byte array
     * @throws IOException
     */
    public static byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(obj);
        return out.toByteArray();
    }

    /**
     * Desiralizes the given byte array.
     *
     * @param data the given byte array
     * @return the deserialized object.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(in);
        return is.readObject();
    }

    /**
     * Sends the given object to the multicast group. Everyone in the group will receive it.
     *
     * @param object the object to send
     * @throws IOException
     */
    public void send(Object object) throws IOException {
        byte[] bytes = serialize(object);

        DatagramPacket packet = new DatagramPacket(bytes, bytes.length);
        multicastSocket.send(packet);
    }

    /**
     * Receives a DatagramPacket from the MulticastGroup
     *
     * @return the datagram packet
     */
    public DatagramPacket receive() {
        byte[] buf = new byte[1000];
        DatagramPacket datagramPacket = new DatagramPacket(buf, buf.length);
        try {
            multicastSocket.receive(datagramPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return datagramPacket;
    }

}
