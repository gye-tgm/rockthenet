package rockthenet.firewall;

import rockthenet.firewall.jns5gt.JNS5GTPolicy;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * The Thru Put Monitor model is a class, which manages the thru put of all the polices. It retrieves
 * the thru put of every policy in the firewall and saves them in a history.
 * @author Gary Ye
 */
public class ThruPutMonitorModel {
    // The data source
    private Firewall firewall;
    // Number of updates currently
    private int updateCount;
    // The history of the policies thru puts
    private HashMap<Integer, ArrayList<ThruPutData>> history;

    /**
     * Constructs a new object with the given firewall.
     * @param firewall the firewall from which the thru put and polices are retrieved
     */
    public ThruPutMonitorModel(Firewall firewall) {
        this.firewall = firewall;
        this.history = new HashMap<>();
    }

    /**
     * Refreshing means, adding the thru puts of all firewall polices to the history.
     */
    public void refresh() {
        firewall.refreshPolicies();
        for (Policy policy : firewall.getPolicies()) {
            if(!history.containsKey(policy.getId()))
                history.put(policy.getId(), new ArrayList<>());
            history.get(policy.getId()).add(new ThruPutData(updateCount, ((JNS5GTPolicy)policy).getThruPut()));
        }
        updateCount++;
    }

    public int getUpdateCount() {
        return updateCount;
    }

    public ArrayList<ThruPutData> getPolicyHistory(int id) {
        return history.get(id);
    }
}
