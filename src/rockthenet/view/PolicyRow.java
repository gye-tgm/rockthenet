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
     * Turns a Policy into a PolicyRow, usable in ObersvableList
     *
     * @param policy
     */
    public PolicyRow(Policy policy) {
        setLineChartEnabled(Boolean.FALSE);
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

    public Boolean getLineChartEnabled() {
        return lineChartEnabled.get();
    }

    public Integer getId() {
        return idProperty().get();
    }

    public String getSrcZone() {
        return srcZoneProperty().get();
    }

    public String getDstZone() {
        return dstZoneProperty().get();
    }

    public String getSrcAddress() {
        return srcAddressProperty().get();
    }

    public String getDstAddress() {
        return dstAddressProperty().get();
    }

    public Integer getService() {
        return serviceProperty().get();
    }

    public Integer getAction() {
        return actionProperty().get();
    }

    public Integer getActiveStatus() {
        return activeStatusProperty().get();
    }

    public String getName() {
        return nameProperty().get();
    }


    public void setLineChartEnabled(Boolean lineChartEnabled) {
        lineChartEnabledProperty().set(lineChartEnabled);
    }

    public void setId(Integer id) {
        idProperty().set(id);
    }

    public void setSrcZone(String srcZone) {
        srcZoneProperty().set(srcZone);
    }

    public void setDstZone(String dstZone) {
        dstZoneProperty().set(dstZone);
    }

    public void setSrcAddress(String srcAddress) {
        srcAddressProperty().set(srcAddress);
    }

    public void setDstAddress(String dstAddress) {
        dstAddressProperty().set(dstAddress);
    }

    public void setService(Integer service) {
        serviceProperty().set(service);
    }

    public void setAction(Integer action) {
        actionProperty().set(action);
    }

    public void setActiveStatus(Integer activeStatus) {
        activeStatusProperty().set(activeStatus);
    }

    public void setName(String name) {
        nameProperty().set(name);
    }

    public BooleanProperty lineChartEnabledProperty() {
        if (lineChartEnabled == null) lineChartEnabled = new SimpleBooleanProperty(this, "lineChartEnabled");
        return lineChartEnabled;
    }

    public IntegerProperty idProperty() {
        if (id == null) id = new SimpleIntegerProperty(this, "id");
        return id;
    }

    public StringProperty nameProperty() {
        if (name == null) name = new SimpleStringProperty(this, "name");
        return name;
    }

    public StringProperty srcZoneProperty() {
        if (srcZone == null) srcZone = new SimpleStringProperty(this, "srcZone");
        return srcZone;
    }

    public StringProperty dstZoneProperty() {
        if (dstZone == null) dstZone = new SimpleStringProperty(this, "dstZone");
        return dstZone;
    }

    public StringProperty srcAddressProperty() {
        if (srcAddress == null) srcAddress = new SimpleStringProperty(this, "srcAddress");
        return srcAddress;
    }

    public StringProperty dstAddressProperty() {
        if (dstAddress == null) dstAddress = new SimpleStringProperty(this, "dstAddress");
        return dstAddress;
    }

    public IntegerProperty serviceProperty() {
        if (service == null) service = new SimpleIntegerProperty(this, "service");
        return service;
    }

    public IntegerProperty actionProperty() {
        if (action == null) action = new SimpleIntegerProperty(this, "action");
        return action;
    }

    public IntegerProperty activeStatusProperty() {
        if (activeStatus == null) activeStatus = new SimpleIntegerProperty(this, "activeStatus");
        return activeStatus;
    }

}
