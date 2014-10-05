package rockthenet;

import rockthenet.firewall.Firewall;
import rockthenet.firewall.Policy;

import java.util.*;

/**
 * Created by gary on 05/10/14.
 */
public class ThruPutMonitorModel {
    private Firewall firewall;
    private ArrayList<PolicyThruPut> abc;
    public ThruPutMonitorModel(Firewall firewall) {
        this.firewall = firewall;
    }

    public void refresh() {
        firewall.refreshPolicies();
        for(Policy policy: firewall.getPolicies()){
        }
    }
}
