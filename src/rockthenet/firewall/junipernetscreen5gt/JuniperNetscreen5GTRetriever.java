package rockthenet.firewall.junipernetscreen5gt;

import net.percederberg.mibble.MibLoaderException;
import org.snmp4j.PDU;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.VariableBinding;
import rockthenet.MibHelper;
import rockthenet.connections.ConnectionException;
import rockthenet.connections.ConnectionFactory;
import rockthenet.connections.ReadConnection;
import rockthenet.firewall.SnmpRetriever;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by gary on 28/09/14.
 */
public class JuniperNetscreen5GTRetriever extends SnmpRetriever {
    private ReadConnection readConnection;
    private MibHelper mibHelper;

    public JuniperNetscreen5GTRetriever(String address, int port, String readCommunity) {
        mibHelper = new MibHelper("res/asn1-3224-mibs/NETSCREEN-POLICY-MIB.mib");

        // TODO: Fall back to Snmp2 if Snmp3 does not work; this should be considered in the ConnectionFactory or here
        try {
            readConnection = ConnectionFactory.createSNMPv2cConnection(address, port, readCommunity);
        } catch (ConnectionException e) {
            e.printStackTrace();
        }
    }

    public HashMap<Integer, JuniperNetscreen5GTPolicy> retrievePolicies(){
        HashMap<Integer, JuniperNetscreen5GTPolicy> policies = new HashMap<>();
        try {
            VariableBinding[] variableBindings = readConnection.getTable(mibHelper.getOID("nsPlyTable"));
            for(int i = 0; i < variableBindings.length; i++){
                // We first retrieve the id of the rule
                int[] arr = variableBindings[i].getOid().toIntArray();
                Integer id = arr[arr.length - 2];
                String oidString = mibHelper.getName(new OID(Arrays.copyOf(arr, arr.length - 2)).toString());
                // Then put it into the hashmap if not already done.
                if(!policies.containsKey(id)) {
                    policies.put(id, new JuniperNetscreen5GTPolicy());
                }
                // Now we have a reference to the policy that we want to change the value(s)
                JuniperNetscreen5GTPolicy policy = policies.get(id);

                switch(oidString){
                    case "nsPlyId":
                        policy.setId(variableBindings[i].getVariable().toInt());
                        break;
                    case "nsPlySrcZone":
                        policy.setSrcZone(variableBindings[i].getVariable().toString());
                        break;
                    case "nsPlyDstZone":
                        policy.setDstZone(variableBindings[i].getVariable().toString());
                        break;
                    case "nsPlySrcAddr":
                        policy.setSrcAddress(variableBindings[i].getVariable().toString());
                        break;
                    case "nsPlyDstAddr":
                        policy.setDstAddress(variableBindings[i].getVariable().toString());
                        break;
                    case "nsPlyService":
                        policy.setService(variableBindings[i].getVariable().toInt());
                        break;
                    case "nsPlyAction":
                        policy.setAction(variableBindings[i].getVariable().toInt());
                        break;
                    case "nsPlyActiveStatus":
                        policy.setActiveStatus(variableBindings[i].getVariable().toInt());
                        break;
                    case "nsPlyName":
                        policy.setName(variableBindings[i].getVariable().toString());
                        break;
                    default:
                        break;
                }
                // System.out.println(variableBindings[i].getOid() + " " + variableBindings[i].getVariable());
            }
        } catch (ConnectionException e) {
            e.printStackTrace();
        }
        return policies;
    }

    public ReadConnection getReadConnection() {
        return readConnection;
    }
}
