package rockthenet.firewall.jns5gt;

import rockthenet.datamanagement.snmp.JNS5GTRetriever;
import rockthenet.datamanagement.snmp.JNS5GTWriter;
import rockthenet.firewall.Firewall;
import rockthenet.firewall.Policy;

import java.util.List;

/**
 * This class represents the firewall designed for the JNS5GT Firewall appliance.
 *
 * @author Gary Ye
 * @version 2014-10-29
 */
public class JNS5GTFirewall extends Firewall {

    /**
     * Constructs a firewall with the given retriever and writer.
     *
     * @param retriever the JNS5GTRetriever
     * @param writer    the JNS5GTWriter
     */
    public JNS5GTFirewall(JNS5GTRetriever retriever, JNS5GTWriter writer) {
        super("Juniper Netscreen 5GT", retriever, writer);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void refreshPolicies() {
        setPolicies((List<Policy>) dataRetriever.get("policies"));
    }

    @Override
    public void deletePolicy(Policy policy) {
        dataWriter.unset(JNS5GTWriter.POLICY, policy);
    }

    @Override
    public void addPolicy(Policy policy) {
        dataWriter.set(JNS5GTWriter.POLICY, policy);
    }

    @Override
    public void updatePolicy(Policy oldPolicy, Policy newPolicy) {
        // Updating in JNS5GT can only be done by deleting the policy beforehand and adding the policy afterwards.
        deletePolicy(oldPolicy);
        addPolicy(newPolicy);
    }
}
