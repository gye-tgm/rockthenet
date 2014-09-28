package firewall;

import javax.security.auth.Refreshable;
import java.util.List;

/**
 * @author gary
 */
public abstract class Firewall implements Refreshable {
    protected List<Policy> policies;
    protected String name;
    // writeConnection
    // readConneciton

    @Override
    public void refresh(){
        refreshPolicies();
    }

    public abstract void refreshPolicies();
}
