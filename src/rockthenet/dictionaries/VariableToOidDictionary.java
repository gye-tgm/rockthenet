package rockthenet.dictionaries;

import net.percederberg.mibble.*;
import net.percederberg.mibble.value.ObjectIdentifierValue;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

/**
 * @author Gary Ye
 */
public class VariableToOidDictionary extends BidirectionalDictionary<String, String> {
    public VariableToOidDictionary(){}
    public VariableToOidDictionary(String filename) throws IOException, MibLoaderException {
        load(new File(filename));
    }
    public void load(File file) throws IOException, MibLoaderException {
        MibLoader mibLoader = new MibLoader();
        mibLoader.addDir(file.getParentFile());
        Mib mib = mibLoader.load(file);
        Iterator iter = mib.getAllSymbols().iterator();
        while(iter.hasNext()){
            MibSymbol symbol = (MibSymbol) iter.next();
            MibValue value = extractOid(symbol);
            if (value != null) {
                a2b.put(symbol.getName(), value.toString());
            }
        }
        transfera2b();
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

    public String getVariableToOid(String name){
        return getA2BDefinition(name);
    }
    public String getOidToVariable(String name){
        return getB2ADefinition(name);
    }
}
