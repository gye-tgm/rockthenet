package firewall.junipernetscreen5gt;

import connections.ConnectionException;
import connections.ReadConnection;
import firewall.SnmpRetriever;
import org.snmp4j.PDU;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gary on 28/09/14.
 */
public class JuniperNetscreen5GTRetriever extends SnmpRetriever {
    ReadConnection readConnection;

    public JuniperNetscreen5GTRetriever(){
        // TODO: Load mib here
        // then retrieve
    }
    public List<JuniperNetscreen5GTPolicy> retrievePolicies(){
        // for each...
        //
        List<JuniperNetscreen5GTPolicy> policies = new ArrayList<JuniperNetscreen5GTPolicy>();
        try {
            PDU pdu = readConnection.get("1.1.");

        } catch (ConnectionException e) {
            e.printStackTrace();
        }
        return policies;
    }
}
