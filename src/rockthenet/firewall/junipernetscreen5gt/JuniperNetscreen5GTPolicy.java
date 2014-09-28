package rockthenet.firewall.junipernetscreen5gt;

import rockthenet.firewall.Policy;

/**
 * Created by gary on 28/09/14.
 */
public class JuniperNetscreen5GTPolicy extends Policy {
    public JuniperNetscreen5GTPolicy(Integer id, String srcZone, String dstZone, String srcAddress, String dstAddress, Integer service, Integer action, Integer activeStatus, String name) {
        super(id, srcZone, dstZone, srcAddress, dstAddress, service, action, activeStatus, name);
    }
}
