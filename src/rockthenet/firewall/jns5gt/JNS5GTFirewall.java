package rockthenet.firewall.jns5gt;

import rockthenet.datamanagement.snmp.JNS5GTRetriever;
import rockthenet.datamanagement.snmp.JNS5GTWriter;
import rockthenet.firewall.Firewall;
import rockthenet.firewall.Policy;

import java.util.ArrayList;

/**
 * This class represents the firewall designed for the JNS5GT Firewall appliance.
 *
 * @author Gary Ye
 */
public class JNS5GTFirewall extends Firewall {
    /**
     * Constructs a firewall with the given retriever and writer.
     *
     * @param retriever the retriever
     * @param writer    the writer
     *
     */
    public JNS5GTFirewall(JNS5GTRetriever retriever, JNS5GTWriter writer) {
        super("Juniper Netscreen 5GT", retriever, writer);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void refreshPolicies() {
        setPolicies((ArrayList<Policy>) dataRetriever.get("policies"));
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
        deletePolicy(oldPolicy);
        addPolicy(newPolicy);
    }

    @Override
    public boolean isCurrent() {
        return false;
    }
}
