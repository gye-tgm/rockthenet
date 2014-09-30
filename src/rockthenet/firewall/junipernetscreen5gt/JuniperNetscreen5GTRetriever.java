package rockthenet.firewall.junipernetscreen5gt;

import net.percederberg.mibble.MibLoaderException;
import org.snmp4j.PDU;
import rockthenet.MibHelper;
import rockthenet.connections.ConnectionException;
import rockthenet.connections.ConnectionFactory;
import rockthenet.connections.ReadConnection;
import rockthenet.firewall.SnmpRetriever;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gary on 28/09/14.
 */
public class JuniperNetscreen5GTRetriever extends SnmpRetriever {
    private ReadConnection readConnection;
    private MibHelper mibHelper;

    public JuniperNetscreen5GTRetriever(String address, int port, String readCommunity) throws IOException, MibLoaderException,
            ConnectionException {
        // TODO: Fall back to Snmp2 if Snmp3 does not work; this should be considered in the ConnectionFactory or here
        mibHelper = new MibHelper("res/asn1-3224-mibs/NETSCREEN-POLICY-MIB.mib");
        readConnection = ConnectionFactory.createSNMPv2cConnection(address, port, readCommunity);
    }

    public List<JuniperNetscreen5GTPolicy> retrievePolicies(){
        List<JuniperNetscreen5GTPolicy> policies = new ArrayList<JuniperNetscreen5GTPolicy>();
        try {
            PDU pdu = readConnection.get(new String[]{
                    mibHelper.getOID("nsPlyId"),
                    mibHelper.getOID("nsPlySrcZone"),
                    mibHelper.getOID("nsPlyDstZone"),
                    mibHelper.getOID("nsPlySrcZone"),
                    mibHelper.getOID("nsPlyDstAddr"),
                    mibHelper.getOID("nsPlyService"),
                    mibHelper.getOID("nsPlyService"),
                    mibHelper.getOID("nsPlyAction"),
                    mibHelper.getOID("nsPlyActiveStatus"),
                    mibHelper.getOID("nsPlyName"),
            });

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
}
