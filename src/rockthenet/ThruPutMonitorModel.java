package rockthenet;

import rockthenet.firewall.Firewall;
import rockthenet.firewall.Policy;
import rockthenet.firewall.jns5gt.JNS5GTPolicy;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by gary on 05/10/14.
 */
public class ThruPutMonitorModel {
    // The data source
    private Firewall firewall;
    // Number of updates currently
    private int updateCount;

    // The history of the policies thru puts
    // The number of updates can be determined by the size of the history list
    private HashMap<Integer, ArrayList<ThruPutData>> history;

    public ThruPutMonitorModel(Firewall firewall) {
        this.firewall = firewall;
        this.history = new HashMap<>();
    }

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
