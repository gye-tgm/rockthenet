package rockthenet.firewall;

import rockthenet.datamanagement.IDataRetriever;
import rockthenet.datamanagement.IDataWriter;

import javax.security.auth.Refreshable;
import java.util.List;

/**
 * This is an abstract class for managing the various types of firewalls. The firewall can be either
 * virtual or an appliance and using this class one should be able to communicate the firewall.
 * @author Gary Ye
 */
public abstract class Firewall implements Refreshable {
    protected List<Policy> policies;
    protected String name;
    protected IDataRetriever dataRetriever;
    protected IDataWriter dataWriter;

    /**
     * Constructs a new Firewall object with the given name, retriever and writer.
     *
     * @param name the name of the firewall
     * @param dataRetriever the retriever of the firewall
     * @param dataWriter the writer of the firewall
     */
    protected Firewall(String name, IDataRetriever dataRetriever, IDataWriter dataWriter) {
        this.name = name;
        this.dataRetriever = dataRetriever;
        this.dataWriter = dataWriter;
    }

    @Override
    public void refresh(){
        refreshPolicies();
    }

    /**
     * Refresh all policies by loading the data from the connected firewall.
     */
    public abstract void refreshPolicies();

    /**
     * Return the name of the firewall.
     * @return the name of the firewall
     */
    public String getName() {
        return name;
    }

    /**
     * Return all policies it currently has. For getting the latest data, the
     * refreshPolicies method should be called before this method is called.
     * @return the local policies as a list
     */
    public List<Policy> getPolicies() {
        return policies;
    }

    /**
     * Sets all policies with the given list.
     * @param policies the list of policies
     */
    public void setPolicies(List<Policy> policies) {
        this.policies = policies;
    }

    /**
     * Returns the policy with the given id
     * @param id the id of the wanted policy
     * @return the policy with the given id
     */
    public Policy getPolicy(Integer id){
        for(Policy policy: policies)
            if(policy.getId().equals(id))
                return policy;
        return null;
    }

    public IDataRetriever getDataRetriever() {
        return dataRetriever;
    }

    public IDataWriter getDataWriter() {
        return dataWriter;
    }

    public void setDataRetriever(IDataRetriever dataRetriever) {
        this.dataRetriever = dataRetriever;
    }

    public void setDataWriter(IDataWriter dataWriter) {
        this.dataWriter = dataWriter;
    }
}
