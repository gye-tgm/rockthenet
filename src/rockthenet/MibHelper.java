package rockthenet;

import net.percederberg.mibble.*;
import net.percederberg.mibble.value.ObjectIdentifierValue;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

/**
 * @author gary
 */
public class MibHelper {
    private HashMap<String, MibValue> oidDictionary;

    public MibHelper() {
    }

    public MibHelper(String filename) {
        try {
            oidDictionary = extractOids(loadMib(new File(filename)));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MibLoaderException e) {
            e.printStackTrace();
        }
    }

    public String getOID(String variableName){
        return oidDictionary.get(variableName).toString();
    }
    /**
     * Loads the Mib from the specified file and returns it.
     *
     * @param file the file of the mib
     * @return the mib
     * @throws IOException
     * @throws MibLoaderException
     */
    public Mib loadMib(File file) throws IOException, MibLoaderException {
        MibLoader mibLoader = new MibLoader();
        mibLoader.addDir(file.getParentFile());
        return mibLoader.load(file);
    }

    public HashMap<String, MibValue> extractOids(Mib mib) {
        HashMap<String, MibValue>  map = new HashMap();
        Iterator iter = mib.getAllSymbols().iterator();
        MibSymbol symbol;
        MibValue value;

        while (iter.hasNext()) {
            symbol = (MibSymbol) iter.next();
            value = extractOid(symbol);;
            if (value != null) {
                map.put(symbol.getName(), value);
            }
        }
        return map;
    }


    public ObjectIdentifierValue extractOid(MibSymbol symbol) {
        MibValue value;

        if (symbol instanceof MibValueSymbol) {
            value = ((MibValueSymbol) symbol).getValue();
            if (value instanceof ObjectIdentifierValue) {
                return (ObjectIdentifierValue) value;
            }
        }
        return null;
    }

}
