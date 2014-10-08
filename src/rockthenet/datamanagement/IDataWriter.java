package rockthenet.datamanagement;

/**
 * An interface for a data writer, which writes the data to the data source.
 * @author Gary Ye
 */
public interface IDataWriter {
    public void set(String variableName, Object newValue);
}
