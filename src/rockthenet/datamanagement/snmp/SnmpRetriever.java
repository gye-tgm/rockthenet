package rockthenet.datamanagement.snmp;

import net.percederberg.mibble.MibLoaderException;
import rockthenet.datamanagement.IDataRetriever;
import rockthenet.dictionaries.VariableToOidDictionary;

import java.io.IOException;

/**
 * A data retriever designed for retrieving data with SNMP.
 *
 * @author Gary Ye
 */
public abstract class SnmpRetriever extends SnmpManager implements IDataRetriever {
    /**
     * Constructs a new snmp retriever with the given filename as the
     * mib.
     *
     * @param mibFile the name of the mib file.
     * @throws IOException        will be thrown if reading from the
     * @throws MibLoaderException
     */
    public SnmpRetriever(String mibFile) throws IOException, MibLoaderException {
        super(new VariableToOidDictionary(mibFile));
    }
}
