package rockthenet.datamanagement.snmp;

import rockthenet.dictionaries.VariableToOidDictionary;

/**
 * The SNMP manager class is an abstract class for all snmp data
 * managers. It contains the Variable to OID dictionary.
 * @author Gary Ye
 */
public abstract class SnmpManager  {
    protected VariableToOidDictionary dictionary;

    /**
     * Constructs a new snmp manager with the
     * given oid dictionary.
     * @param dictionary the ditionary to use
     */
    public SnmpManager(VariableToOidDictionary dictionary){
        this.dictionary = dictionary;
    }
}
