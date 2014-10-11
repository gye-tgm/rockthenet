package rockthenet.datamanagement;

import rockthenet.firewall.Policy;

import java.util.List;

/**
 * Created by gary on 11/10/14.
 */
public abstract class FirewallRetriever implements IDataRetriever {
    public final static String POLICIES = "policies";

    @Override
    public Object get(String variableName) {
        switch (variableName){
            case POLICIES:
                return retrievePolicies();
            default:
                return null;
        }
    }

    protected abstract List<Policy> retrievePolicies();
}
