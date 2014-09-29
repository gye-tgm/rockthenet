package rockthenet.firewall.junipernetscreen5gt;

import net.percederberg.mibble.MibLoaderException;
import net.percederberg.mibble.MibValue;
import rockthenet.MibHelper;
import rockthenet.connections.ConnectionException;
import rockthenet.connections.ConnectionFactory;
import rockthenet.connections.ReadConnection;
import rockthenet.connections.SNMPv2cConnection;
import rockthenet.firewall.SnmpRetriever;
import org.snmp4j.PDU;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by gary on 28/09/14.
 */
public class JuniperNetscreen5GTRetriever extends SnmpRetriever {
    private ReadConnection readConnection;
    private HashMap<String, MibValue> oidDictionary;

    public JuniperNetscreen5GTRetriever(String address, int port, String readCommunity) throws IOException, MibLoaderException,
            ConnectionException {
        // TODO: Fall back to Snmp2 if Snmp3 does not work
        MibHelper mibHelper = new MibHelper();
        oidDictionary = mibHelper.extractOids(mibHelper.loadMib(new File("res/asn1-3224-mibs/NETSCREEN-POLICY-MIB.mib")));
        readConnection = ConnectionFactory.createSNMPv2cConnection(address, port, readCommunity);
    }

    public List<JuniperNetscreen5GTPolicy> retrievePolicies(){
        List<JuniperNetscreen5GTPolicy> policies = new ArrayList<JuniperNetscreen5GTPolicy>();
        try {
            PDU pdu = readConnection.get(".1.3.6.1.4.1.3224.5");
            for(int i = 0; i < pdu.size(); i++) {
                pdu.get(i).getVariable().toInt();
            }
        } catch (ConnectionException e) {
            e.printStackTrace();
        }
        return policies;
    }

    public ReadConnection getReadConnection() {
        return readConnection;
    }

    public HashMap<String, MibValue> getOidDictionary() {
        return oidDictionary;
    }
}
