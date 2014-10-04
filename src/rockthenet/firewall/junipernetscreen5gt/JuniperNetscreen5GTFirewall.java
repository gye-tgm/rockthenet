package rockthenet.firewall.junipernetscreen5gt;

import net.percederberg.mibble.MibLoaderException;
import rockthenet.firewall.Firewall;
import rockthenet.firewall.IDataRetriever;
import rockthenet.firewall.Policy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author gary
 */
public class JuniperNetscreen5GTFirewall extends Firewall {
    public JuniperNetscreen5GTFirewall(JuniperNetscreen5GTRetriever dataRetriever, JuniperNetscreen5GTWriter writer) throws IOException, MibLoaderException {
        super("Juniper Netscreen 5GT", dataRetriever, writer);
    }

    @Override
    public void refreshPolicies() {
        HashMap<Integer, JuniperNetscreen5GTPolicy> h = (HashMap<Integer, JuniperNetscreen5GTPolicy>) dataRetriever.get("policies");
        setPolicies(new ArrayList<>(h.values()));
    }

    @Override
    public boolean isCurrent() {
        return false;
    }
}
