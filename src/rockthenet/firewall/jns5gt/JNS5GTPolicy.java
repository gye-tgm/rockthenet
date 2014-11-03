package rockthenet.firewall.jns5gt;

import rockthenet.firewall.Policy;

/**
 * This class represents the policy designed for the JNS5GT firewall appliance.
 * <p>
 * It contains {@code thruPut}, given in bytes per second, which other firewalls might not have.
 *
 * @author Gary Ye
 * @version 2014-10-29
 */
public class JNS5GTPolicy extends Policy {
    protected int thruPut;

    /**
     * Constructs a JNS5GT policy with the default values.
     */
    public JNS5GTPolicy() {
        super();
    }

    /**
     * Constructs a policy with all properties of the firewall.
     *
     * @param id           the id of the firewall policy
     * @param srcZone      the source zone
     * @param dstZone      the destination zone
     * @param srcAddress   the source address
     * @param dstAddress   the destination address
     * @param service      the service of the policy as an integer
     * @param action       the action of the policy
     * @param activeStatus the active status
     * @param name         the name of the policy
     */
    public JNS5GTPolicy(Integer id, String srcZone, String dstZone, String srcAddress, String dstAddress,
                        Integer service, Integer action, Integer activeStatus, String name) {
        super(id, srcZone, dstZone, srcAddress, dstAddress, service, action, activeStatus, name);
    }

    /**
     * Returns the thruput.
     *
     * @return the thruput.
     */
    public int getThruPut() {
        return thruPut;
    }

    /**
     * Sets the thruput.
     *
     * @param thruPut the value to set
     */
    public void setThruPut(int thruPut) {
        this.thruPut = thruPut;
    }
}