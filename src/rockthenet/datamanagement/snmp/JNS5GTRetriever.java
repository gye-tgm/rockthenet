package rockthenet.datamanagement.snmp;

import net.percederberg.mibble.MibLoaderException;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.Variable;
import org.snmp4j.smi.VariableBinding;
import rockthenet.connections.ConnectionException;
import rockthenet.connections.ReadConnection;
import rockthenet.connections.snmp.SNMPConnectionFactory;
import rockthenet.firewall.jns5gt.JNS5GTPolicy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * This is the JNS5GT retriever which retrieves the data by using SNMP protocol.
 *
 * @author Gary Ye
 */
public class JNS5GTRetriever extends SnmpRetriever {
    private ReadConnection readConnection;
    private final static String POLICY_ROOT_OID = "1.3.6.1.4.1.3224.10";

    /**
     * Constructs a new retriever for the JNS5GT firewall appliance.
     *
     * @param readConnection the read connection; specifies which firewall to connect to.
     * @throws ConnectionException
     * @throws IOException
     * @throws MibLoaderException
     */
    public JNS5GTRetriever(ReadConnection readConnection) throws ConnectionException, IOException, MibLoaderException {
        super("res/asn1-3224-mibs/NETSCREEN-POLICY-MIB.mib");
        this.readConnection = readConnection;
    }

    /**
     * Constructs a new retriever for the JNS5GT appliance by building an SNMP connection with
     * the given connection data.
     *
     * @param address       the address of the firewall appliance
     * @param port          the port number of the firewall appliance
     * @param readCommunity the read community
     */
    public JNS5GTRetriever(String address, int port, String readCommunity) throws ConnectionException, IOException,
            MibLoaderException {
        // TODO: Fall back to Snmp2 if Snmp3 does not work; this should be considered in the ConnectionFactory or here
        this(SNMPConnectionFactory.createSNMPv2cConnection(address, port, readCommunity,
                readCommunity)); // TODO: communityName != securityName
    }

    /**
     * Retrieves all policies from the firewall, to which a connection has been established.
     *
     * @return the policies retrieved from the firewall (it can be empty)
     */
    public List<JNS5GTPolicy> retrievePolicies() {
        HashMap<Integer, JNS5GTPolicy> policies = new HashMap<>();
        try {
            VariableBinding[] variableBindings = readConnection.getTable(POLICY_ROOT_OID);
            // TODO: nsPlyTable was old
            for (int i = 0; i < variableBindings.length; i++) {
                // We first retrieve the id of the rule
                int[] arr = variableBindings[i].getOid().toIntArray();
                Integer id = arr[arr.length - 2];
                String oidString = dictionary.getOidToVariable(new OID(Arrays.copyOf(arr, arr.length - 2)).toString());
                // Then put it into the hashmap if not already done.
                if (!policies.containsKey(id)) {
                    policies.put(id, new JNS5GTPolicy());
                }
                // Now we have a reference to the policy that we want to change the value(s)
                JNS5GTPolicy policy = policies.get(id);
                Variable variable = variableBindings[i].getVariable();
                switch (oidString) {
                    case "nsPlyId":
                        policy.setId(variable.toInt());
                        break;
                    case "nsPlySrcZone":
                        policy.setSrcZone(variable.toString());
                        break;
                    case "nsPlyDstZone":
                        policy.setDstZone(variable.toString());
                        break;
                    case "nsPlySrcAddr":
                        policy.setSrcAddress(variable.toString());
                        break;
                    case "nsPlyDstAddr":
                        policy.setDstAddress(variable.toString());
                        break;
                    case "nsPlyService":
                        policy.setService(variable.toInt());
                        break;
                    case "nsPlyAction":
                        policy.setAction(variable.toInt());
                        break;
                    case "nsPlyActiveStatus":
                        policy.setActiveStatus(variable.toInt());
                        break;
                    case "nsPlyName":
                        policy.setName(variable.toString());
                        break;
                    case "nsPlyMonBytePerSec":
                        policy.setThruPut(variable.toInt());
                        break;
                    default:
                        break;
                }
            }
        } catch (ConnectionException e) {
            e.printStackTrace();
        }
        return new ArrayList<>(policies.values());
    }

    @Override
    public Object get(String variableName) {
        switch (variableName) {
            case "policies":
                return retrievePolicies();
            default:
                return null;
        }
    }
}
