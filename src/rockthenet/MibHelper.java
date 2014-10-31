package rockthenet;

import net.percederberg.mibble.*;
import net.percederberg.mibble.value.ObjectIdentifierValue;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author gary
 */
public class MibHelper {
    private HashMap<String, String> oidDictionary;   // (name -> oid)
    private HashMap<String, String> namesDictionary; // (oid -> name)


    public MibHelper() {
    }

    public MibHelper(String filename) {
        try {
            oidDictionary = extractOids(loadMib(new File(filename)));
            namesDictionary = new HashMap<>();
            for (Map.Entry<String, String> e : oidDictionary.entrySet()) {
                namesDictionary.put(e.getValue(), e.getKey());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MibLoaderException e) {
            e.printStackTrace();
        }
    }

    public String getOID(String variableName) {
        return oidDictionary.get(variableName);
    }

    public String getName(String oid) {
        return namesDictionary.get(oid);
    }

    /* Loads the Mib from the specified file and returns it.
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

    public HashMap<String, String> extractOids(Mib mib) {
        HashMap<String, String> map = new HashMap();
        Iterator iter = mib.getAllSymbols().iterator();
        MibSymbol symbol;
        MibValue value;

        while (iter.hasNext()) {
            symbol = (MibSymbol) iter.next();
            value = extractOid(symbol);
            if (value != null) {
                map.put(symbol.getName(), value.toString());
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
