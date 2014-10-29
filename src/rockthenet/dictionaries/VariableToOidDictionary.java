package rockthenet.dictionaries;

import net.percederberg.mibble.*;
import net.percederberg.mibble.value.ObjectIdentifierValue;

import java.io.File;
import java.io.IOException;

/**
 * Every variable in the MIB is associated with an OID. This OID is a dot separated sequence of integers, which is
 * very hard to remember. Thus this class offers a way to translate them by loading the definitions from a MIB file.
 * <p>
 * The A language is the human readable variable name while the B language is the OID.
 *
 * @author Gary Ye
 * @version 2014-10-29
 */
public class VariableToOidDictionary extends BidirectionalDictionary<String, String> {
    /**
     * Constructs an empty variable to oid dictionary.
     */
    public VariableToOidDictionary() {
    }

    /**
     * Constructs a dictionary which content is based on the given file.
     *
     * @param filename the name of the file
     */
    public VariableToOidDictionary(String filename) throws IOException, MibLoaderException {
        load(new File(filename));
    }

    /**
     * Loads the content of the mib file in to the dictionary.
     *
     * @param file the mib file
     * @throws IOException        will be thrown if loading was unsuccessful
     * @throws MibLoaderException will be thrown if loading the mib was unsuccessful
     */
    public void load(File file) throws IOException, MibLoaderException {
        MibLoader mibLoader = new MibLoader();
        mibLoader.addDir(file.getParentFile());
        Mib mib = mibLoader.load(file);
        for (Object o : mib.getAllSymbols()) {
            MibSymbol symbol = (MibSymbol) o;
            MibValue value = extractOid(symbol);
            if (value != null) {
                a2b.put(symbol.getName(), value.toString());
            }
        }
        transfera2b();
    }

    /**
     * Extracts the
     *
     * @param symbol
     * @return
     */
    private ObjectIdentifierValue extractOid(MibSymbol symbol) {
        MibValue value;

        if (symbol instanceof MibValueSymbol) {
            value = ((MibValueSymbol) symbol).getValue();
            if (value instanceof ObjectIdentifierValue) {
                return (ObjectIdentifierValue) value;
            }
        }
        return null;
    }

    // Wrapper methods
    public String getVariableToOid(String name) {
        return getA2BDefinition(name);
    }

    public String getOidToVariable(String name) {
        return getB2ADefinition(name);
    }
}
