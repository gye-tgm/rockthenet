package rockthenet.firewall;

import rockthenet.Refreshable;
import rockthenet.datamanagement.IDataRetriever;
import rockthenet.datamanagement.IDataWriter;

import java.util.List;

/**
 * This is an abstract class for managing various types of firewalls. The firewall can be either virtual or tangible and
 * by using this class one should be able to communicate with the firewall.
 * <p>
 * When designing a class for a firewall, this class should be extended since it offers the must have methods of every
 * firewall.
 * <p>
 * It contains abstract data managers which can communicate with the actual firewall in various ways (ethernet,
 * filesystems, XML, JDBC, ...).
 *
 * @author Gary Ye
 * @version 2014-10-29
 */
public abstract class Firewall implements Refreshable {
    protected List<Policy> policies;
    protected String name;
    protected IDataRetriever dataRetriever;
    protected IDataWriter dataWriter;

    /**
     * Constructs a new Firewall object with the given name, retriever and writer.
     *
     * @param name          the name of the firewall
     * @param dataRetriever the retriever of the firewall
     * @param dataWriter    the writer of the firewall
     */
    protected Firewall(String name, IDataRetriever dataRetriever, IDataWriter dataWriter) {
        this.name = name;
        this.dataRetriever = dataRetriever;
        this.dataWriter = dataWriter;
    }

    @Override
    public void refresh() {
        refreshPolicies();
    }

    /**
     * Refreshes all policies by loading the data from the connected firewall. This method is supposed to store the
     * loaded data to {@code policies}.
     */
    public abstract void refreshPolicies();

    /**
     * Returns the name of the firewall, {@code name}
     *
     * @return the name of the firewall, {@code name}
     */
    public String getName() {
        return name;
    }

    /**
     * Returns all policies it currently has. For getting the latest data, the {@code refreshPolicies()} method should
     * be called beforehand.
     *
     * @return the local policies as a list
     */
    public List<Policy> getPolicies() {
        return policies;
    }

    /**
     * Sets the local policies with the given list. Note that the old data will be overridden.
     *
     * @param policies the list of policies
     */
    public void setPolicies(List<Policy> policies) {
        this.policies = policies;
    }

    /**
     * Returns the policy, which is stored locally, with the given id.
     *
     * @param id the id of the wanted policy
     * @return the policy with the given id or null if there is none.
     */
    public Policy getPolicy(Integer id) {
        for (Policy policy : policies)
            if (policy.getId().equals(id))
                return policy;
        return null;
    }

    /**
     * Sets the data retriever of the firewall.
     *
     * @param dataRetriever the data retriever
     */
    public void setDataRetriever(IDataRetriever dataRetriever) {
        this.dataRetriever = dataRetriever;
    }

    /**
     * Sets the data writer of the firewall.
     *
     * @param dataWriter the data writer
     */
    public void setDataWriter(IDataWriter dataWriter) {
        this.dataWriter = dataWriter;
    }

    /**
     * Remotely deletes the given policy from the firewall.
     *
     * @param policy the policy to delete
     */
    public abstract void deletePolicy(Policy policy);

    /**
     * Remotely adds the given policy to the firewall.
     *
     * @param policy the policy to add
     */
    public abstract void addPolicy(Policy policy);

    /**
     * Remotely updates the old policy with the new one.
     *
     * @param oldPolicy the policy to update
     * @param newPolicy the new value of the policy
     */
    public abstract void updatePolicy(Policy oldPolicy, Policy newPolicy);
}
