package rockthenet.view;

import javafx.beans.property.*;
import rockthenet.firewall.Policy;

/**
 * Model for ObservableList for use in the TableView
 * Created by Samuel on 06.10.2014.
 */
public class PolicyRow {
    private BooleanProperty lineChartEnabled;
    private IntegerProperty id;
    private StringProperty srcZone;
    private StringProperty dstZone;
    private StringProperty srcAddress;
    private StringProperty dstAddress;
    private IntegerProperty service;
    private IntegerProperty action;
    private IntegerProperty activeStatus;
    private StringProperty name;

    /**
     * Default constructor of the policy
     */
    public PolicyRow() {
        this(Boolean.FALSE, 0, "N/A", "N/A", "N/A", "N/A", -1, -1, -1, "");
    }

    /**
     * Turns a Policy into a PolicyRow, usable in OberservableList
     *
     * @param policy
     */
    public PolicyRow(Policy policy) {
        setLineChartEnabled(Boolean.TRUE);
        setId(policy.getId());
        setSrcZone(policy.getSrcZone());
        setDstZone(policy.getDstZone());
        setSrcAddress(policy.getSrcAddress());
        setDstAddress(policy.getDstAddress());
        setService(policy.getService());
        setAction(policy.getAction());
        setActiveStatus(policy.getActiveStatus());
        setName(policy.getName());
    }

    /**
     * Constructs a policy with the given properties.
     *
     * @param id           the id of the firewall policy, which every policy should have
     * @param srcZone      the source zone
     * @param dstZone      the destination zone
     * @param srcAddress   the source address
     * @param dstAddress   the destination address
     * @param service      the service of the policy as an integer
     * @param action       the action of the policy
     * @param activeStatus the active status
     * @param name         the name of the policy
     */
    public PolicyRow(Boolean lineChartEnabled, Integer id, String srcZone, String dstZone, String srcAddress, String dstAddress, Integer service, Integer action, Integer activeStatus, String name) {
        setLineChartEnabled(lineChartEnabled);
        setId(id);
        setSrcZone(srcZone);
        setDstZone(dstZone);
        setSrcAddress(srcAddress);
        setDstAddress(dstAddress);
        setService(service);
        setAction(action);
        setActiveStatus(activeStatus);
        setName(name);
    }

    /* Getters */
    public BooleanProperty getLineChartEnabled() 				{ return lineChartEnabled; }
    public IntegerProperty getId() 								{ return id; }
    public StringProperty getSrcZone() 							{ return srcZone; }
    public StringProperty getDstZone() 							{ return dstZone; }
    public StringProperty getSrcAddress() 						{ return srcAddress; }
    public StringProperty getDstAddress() 						{ return dstAddress; }
    public IntegerProperty getService() 						{ return service; }
    public IntegerProperty getAction() 							{ return action; }
    public IntegerProperty getActiveStatus() 					{ return activeStatus; }
    public StringProperty getName()  							{ return name; }
    
    /* Setters */
    public void setLineChartEnabled(Boolean lineChartEnabled) 	{ this.lineChartEnabled.set(lineChartEnabled); }
    public void setId(Integer id) 								{ this.id.set(id); }
    public void setSrcZone(String srcZone) 						{ this.srcZone.set(srcZone); }
    public void setDstZone(String dstZone) 						{ this.dstZone.set(dstZone); }
    public void setSrcAddress(String srcAddress) 				{ this.srcAddress.set(srcAddress); }
    public void setDstAddress(String dstAddress) 				{ this.dstAddress.set(dstAddress); }
    public void setService(Integer service) 					{ this.service.set(service); }
    public void setAction(Integer action) 						{ this.action.set(action); }
    public void setActiveStatus(Integer activeStatus) 			{ this.activeStatus.set(activeStatus); }
    public void setName(String name) 							{ this.name.set(name); }
}
