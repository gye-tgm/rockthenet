package rockthenet.firewall;

import javax.security.auth.Refreshable;
import java.util.List;

/**
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
}
