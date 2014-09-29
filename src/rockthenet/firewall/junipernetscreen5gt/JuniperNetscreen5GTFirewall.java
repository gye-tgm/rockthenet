package rockthenet.firewall.junipernetscreen5gt;

import net.percederberg.mibble.MibLoaderException;
import rockthenet.firewall.Firewall;
import rockthenet.firewall.IDataRetriever;

import java.io.IOException;

/**
 * Created by gary on 28/09/14.
 */
public class JuniperNetscreen5GTFirewall extends Firewall {
    public JuniperNetscreen5GTFirewall(IDataRetriever dataRetriever) throws IOException, MibLoaderException {
        super("Juniper Netscreen 5GT", dataRetriever, new JuniperNetscreen5GTWriter());
    }

    @Override
    public void refreshPolicies() {

    }

    @Override
    public boolean isCurrent() {
        return false;
    }
}
