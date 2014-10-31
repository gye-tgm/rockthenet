package rockthenet;

/**
 * All classes that implement this interface indicate that they are operating as a listener.
 *
 * @author Gary ye
 * @version 2014-10-31
 */
public interface Listener {
    /**
     * The object will be notified with a given message.
     * @param obj the message
     */
    public void notify(Object obj);
}
