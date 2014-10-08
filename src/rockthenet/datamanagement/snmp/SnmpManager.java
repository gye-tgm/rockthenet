package rockthenet.datamanagement.snmp;

import rockthenet.dictionaries.VariableToOidDictionary;

/**
 * Created by gary on 08/10/14.
 */
public abstract class SnmpManager  {
    protected VariableToOidDictionary dictionary;
    public SnmpManager(VariableToOidDictionary dictionary){
        this.dictionary = dictionary;
    }
}
