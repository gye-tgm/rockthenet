package rockthenet.datamanagement;

/**
 * This interface is for retrieving data of the data source by using various
 * ways of communication.
 * @author gary
 */
public interface IDataRetriever {
    /**
     * Retrieves the object from the data source by using an agreed
     * variable name.
     * @param variableName the variable with the agreed variable name
     * @return the value of the wanted variable retrieved from the data source
     */
    public Object get(String variableName);
}
