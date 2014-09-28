package rockthenet.firewall.junipernetscreen5gt;

import rockthenet.firewall.Firewall;

/**
 * Created by gary on 28/09/14.
 */
public class JuniperNetscreen5GTFirewall extends Firewall {
    public JuniperNetscreen5GTFirewall(){
        super("Juniper Netscreen 5GT", new JuniperNetscreen5GTRetriever(), new JuniperNetscreen5GTWriter());
    }

    @Override
    public void refreshPolicies() {

    }

    @Override
    public boolean isCurrent() {
        return false;
    }
}
