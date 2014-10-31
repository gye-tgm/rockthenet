package rockthenet.ricartagrawala;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.Date;

/**
 * A message used for the Multicast Connection. It contains various message types to identify the type of the message.
 * Additionally the message contains the source and destination address and a timestamp.
 *
 * @author Gary Ye
 */
public class Message implements Serializable {

    public enum MessageType {REQUEST, OK, NOTHING, DENY}

    private InetAddress dstAddress;
    private InetAddress srcAddress;
    private MessageType type;
    private Date timestamp;

    /**
     * Constructs an empty message with NOTHING as the attribute type.
     */
    public Message() {
        this(MessageType.NOTHING, null);
    }

    /**
     * Constructs a message with the given type and the source Address of the sender.
     *
     * @param type       the type of the message
     * @param srcAddress the source address of the sender
     */
    public Message(MessageType type, InetAddress srcAddress) {
        this(type, srcAddress, null);
    }

    /**
     * Constructs a message with the given type and the source Address of the sender and the destination address
     *
     * @param type       the type of the message
     * @param srcAddress the source address of the sender
     * @param dstAddress the destination address
     */
    public Message(MessageType type, InetAddress srcAddress, InetAddress dstAddress) {
        this(type, srcAddress, dstAddress, new Date(System.currentTimeMillis()));
    }

    /**
     * Constructs a message with the given type and the source Address of the sender and the destination address
     *
     * @param type       the type of the message
     * @param srcAddress the source address of the sender
     * @param dstAddress the destination address
     * @param timestamp  the time of which this message was sent
     */
    public Message(MessageType type, InetAddress srcAddress, InetAddress dstAddress, Date timestamp) {
        this.timestamp = timestamp;
        this.srcAddress = srcAddress;
        this.dstAddress = dstAddress;
        this.type = type;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public InetAddress getDstAddress() {
        return dstAddress;
    }

    public void setDstAddress(InetAddress dstAddress) {
        this.dstAddress = dstAddress;
    }

    public InetAddress getSrcAddress() {
        return srcAddress;
    }

    public void setSrcAddress(InetAddress srcAddress) {
        this.srcAddress = srcAddress;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }
}
