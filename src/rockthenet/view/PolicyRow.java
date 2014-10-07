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
     * Turns a Policy into a PolicyRow, usable in OberservableList
     *
     * @param policy
     */
    public PolicyRow(Policy policy) {
        lineChartEnabled = new SimpleBooleanProperty(false);
        id = new SimpleIntegerProperty(policy.getId());
        srcZone = new SimpleStringProperty(policy.getSrcZone());
        dstZone = new SimpleStringProperty(policy.getDstZone());
        srcAddress = new SimpleStringProperty(policy.getSrcAddress());
        dstAddress = new SimpleStringProperty(policy.getDstAddress());
        service = new SimpleIntegerProperty(policy.getService());
        action = new SimpleIntegerProperty(policy.getAction());
        activeStatus = new SimpleIntegerProperty(policy.getActiveStatus());
        name = new SimpleStringProperty(policy.getName());
    }
    
    @Override
    public boolean equals(Object o) { // TODO: implement save equals
    	if (!(o instanceof Policy))
    		return false;
    	
    	return id.equals(((Policy) o).getId());
    }

    /* Getters */
    public Boolean getLineChartEnabled() 						{ return lineChartEnabled.get(); }
    public Integer getId() 										{ return id.get(); }
    public String  getSrcZone() 								{ return srcZone.get(); }
    public String  getDstZone() 								{ return dstZone.get(); }
    public String  getSrcAddress() 								{ return srcAddress.get(); }
    public String  getDstAddress() 								{ return dstAddress.get(); }
    public Integer getService() 								{ return service.get(); }
    public Integer getAction() 									{ return action.get(); }
    public Integer getActiveStatus() 							{ return activeStatus.get(); }
    public String  getName()  									{ return name.get(); }
    
    public BooleanProperty lineChartEnabledProperty()			{ return lineChartEnabled; }
    
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
