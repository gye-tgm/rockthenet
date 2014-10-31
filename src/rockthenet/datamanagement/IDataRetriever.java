package rockthenet.datamanagement;

/**
 * This interface is for retrieving data of the data source by using various
 * ways of communication.
 *
 * @author Gary Ye
 * @version 2014-10-31
 */
public interface IDataRetriever {
    /**
     * Retrieves the object from the data source by using a pre defined
     * variable name.
     *
     * @param variableName the variable with the pre defined variable name
     * @return the value of the wanted variable retrieved from the data source
     */
    public Object get(String variableName);
}
