package rockthenet.firewall.jns5gt;

import rockthenet.firewall.Policy;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * The ThruPut Monitor model is a special class that manages the thruput of a JNS5GT firewall.
 * It retrieves the thruput of every policy in the firewall and stores them in a history list.
 *
 * This class does not optimize any memory consumption and consequently the memory usage can be high after a long usage.
 *
 * @author Gary Ye
 * @version 2014-10-29
 */
public class ThruPutMonitorModel {
    private JNS5GTFirewall firewall;
    private int updateCount;
    private HashMap<Integer, ArrayList<ThruPutData>> history;

    /**
     * Constructs a new model with the given firewall.
     *
     * @param firewall the firewall from which the thru put and polices are retrieved
     */
    public ThruPutMonitorModel(JNS5GTFirewall firewall) {
        this.firewall = firewall;
        this.history = new HashMap<>();
    }

    /**
     * Refreshes the firewall. In this context refreshing means retrieving the thruput of the polices that
     * are currently cached in the firewall.
     */
    public void refresh() {
        for (Policy policy : firewall.getPolicies()) {
            // We first check if the policy is already known, if not, we initialize a list in the hash map.
            if (!history.containsKey(policy.getId()))
                history.put(policy.getId(), new ArrayList<>());
            history.get(policy.getId()).add(new ThruPutData(updateCount, ((JNS5GTPolicy) policy).getThruPut()));
        }
        updateCount++;
    }

    /**
     * Returns the update count.
     * @return the update count.
     */
    public int getUpdateCount() {
        return updateCount;
    }

    /**
     * Returns the thruput data of the policy with the given id
     * @param id the id of the policy from which the thruput data should be retrieved
     * @return the thruput data of policy with the given id
     */
    public ArrayList<ThruPutData> getPolicyHistory(Integer id) {
        return history.get(id);
    }
}
