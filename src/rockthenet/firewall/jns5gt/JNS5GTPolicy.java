package rockthenet.firewall.jns5gt;

import rockthenet.firewall.Policy;

/**
 * This class represents the policy designed for the JuniperNetscreen5GT firewall appliance.
 * It does not contain any further properties than the policy.
 * @author Gary Ye
 */
public class JNS5GTPolicy extends Policy {
    /**
     * The default constructor
     */
    public JNS5GTPolicy(){
        super();
    }
    public JNS5GTPolicy(Integer id, String srcZone, String dstZone, String srcAddress, String dstAddress, Integer service, Integer action, Integer activeStatus, String name) {
        super(id, srcZone, dstZone, srcAddress, dstAddress, service, action, activeStatus, name);
    }
}
