package rockthenet.ricartagrawala;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.Date;

/**
 * @author Gary Ye
 */
public class Message implements Serializable {

    public enum MessageType {REQUEST, OK, NOTHING, DENY}

    private InetAddress dstAddress;
    private InetAddress srcAddress;
    private MessageType type;
    private Date timestamp;

    public Message() {
        this(MessageType.NOTHING, null);
    }

    public Message(MessageType type, InetAddress srcAddress) {
        this(type, srcAddress, null);
    }

    public Message(MessageType type, InetAddress srcAddress, InetAddress dstAddress) {
        this(type, srcAddress, dstAddress, new Date(System.currentTimeMillis()));
    }

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
