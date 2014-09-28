package firewall;

import java.util.HashMap;

/**
 * Created by gary on 28/09/14.
 */
public abstract class SnmpRetriever implements IDataRetriever {
    protected HashMap<String, String> oidDictionary; // (name, oid)
    // TODO: WriteConnection

    @Override
    public Object get(String name){
        String oid = oidDictionary.get(name);

        return null;
    }
}
