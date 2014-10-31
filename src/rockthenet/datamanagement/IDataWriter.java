package rockthenet.datamanagement;

/**
 * An interface for a data writer, which writes the data to the data source.
 *
 * @author Gary Ye
 */
public interface IDataWriter {
    /**
     * Sets the variable with the given variable name with the
     * new value.
     *
     * @param variableName the name of the variable to modify
     * @param newValue     the new value of the given variable
     */
    public void set(String variableName, Object newValue);

    /**
     * Deletes for the given variableName and the given value of the object the corresponding variable.
     *
     * @param variableName the name of the variable to unset
     * @param value        the value of the object to delete
     */
    public void unset(String variableName, Object value);
}
