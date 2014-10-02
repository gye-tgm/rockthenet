package rockthenet.firewall;

import javax.security.auth.Refreshable;
import java.util.List;

/**
 * This abstract class should be implemented by the various types of firewalls.
 * It can be virtual or a hardware and can communicate with their retrievers
 * and writers.
 * @author gary
 */
public abstract class Firewall implements Refreshable {
    protected List<Policy> policies;
    protected String name;
    protected IDataRetriever dataRetriever;
    protected IDataWriter dataWriter;

    protected Firewall(String name, IDataRetriever dataRetriever, IDataWriter dataWriter) {
        this.name = name;
        this.dataRetriever = dataRetriever;
        this.dataWriter = dataWriter;
    }

    @Override
    public void refresh(){
        refreshPolicies();
    }

    public abstract void refreshPolicies();

    public String getName() {
        return name;
    }
}
