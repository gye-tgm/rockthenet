package rockthenet;

/**
 * All classes that implement this interface indicate that they are refreshable.
 * <p>
 * The main method that should be implemented is the {@code refresh()} method.
 *
 * @author Gary Ye
 * @version 2014-10-29
 */
public interface Refreshable {
    /**
     * Refreshes the object that implements this interface.
     */
    public void refresh();
}
