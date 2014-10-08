package rockthenet.firewall.jns5gt;

import net.percederberg.mibble.MibLoaderException;
import rockthenet.datamanagement.snmp.JNS5GTRetriever;
import rockthenet.datamanagement.snmp.JNS5GTWriter;
import rockthenet.firewall.Firewall;
import rockthenet.firewall.Policy;

import java.io.IOException;
import java.util.ArrayList;

/**
 * This class represents the firewall designed for the JNS5GT Firewall appliance.
 * @author gary
 */
public class JNS5GTFirewall extends Firewall {
    /**
     * Constructs a firewall with the given retriever and writer.
     * @param retriever the retriever
     * @param writer the writer
     * @throws IOException
     * @throws MibLoaderException
     */
    public JNS5GTFirewall(JNS5GTRetriever retriever, JNS5GTWriter writer) throws IOException, MibLoaderException {
        super("Juniper Netscreen 5GT", retriever, writer);
    }

    @SuppressWarnings("unchecked")
	@Override
    public void refreshPolicies() {
		setPolicies((ArrayList<Policy>) dataRetriever.get("policies"));
    }

    @Override
    public boolean isCurrent() {
        return false;
    }
}
