package rockthenet.view;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import org.controlsfx.dialog.Dialogs;

import rockthenet.Main;
import rockthenet.Refreshable;
import rockthenet.Refresher;
import rockthenet.SessionSettings;
import rockthenet.connections.snmp.SNMPConnectionFactory;
import rockthenet.connections.ssh.SSHConnection;
import rockthenet.datamanagement.snmp.JNS5GTRetriever;
import rockthenet.datamanagement.snmp.JNS5GTWriter;
import rockthenet.firewall.Firewall;
import rockthenet.firewall.Policy;
import rockthenet.firewall.ThruPutMonitorModel;
import rockthenet.firewall.jns5gt.JNS5GTFirewall;
import rockthenet.firewall.jns5gt.JNS5GTPolicy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 * @author Samuel Schmidt, Elias Frantar
 * @version 2014-10-11
 */
@SuppressWarnings("unchecked")
public class Controller implements Refreshable {

    @FXML
    private MenuItem settings;
    @FXML
    private MenuItem newConnection;
    @FXML
    private MenuItem newConnectionV3;
    @FXML
    private MenuItem about;
    
    @SuppressWarnings("rawtypes")
	@FXML
    private LineChart lineChart;
    
    @FXML
    private Button refreshButton;
    @FXML
    private Button newRule;
    @FXML
    private TableView<PolicyRow> tableView;
    private ObservableList<PolicyRow> policies;

    private PolicyLineChart policyLineChart;
    private ThruPutMonitorModel monitorModel;

    private Main main;
    private SessionSettings session;
    private Set<Integer> checkedPolicy;

	@FXML
    private void initialize() {
		session = SessionSettings.getInstance(); // start a new session

        Image image = new Image(getClass().getResourceAsStream("../resources/refresh-icon.png"));
        refreshButton.setGraphic(new ImageView(image));
        
        /* initialize the table */
        policies = FXCollections.observableArrayList();

        tableView.setItems(policies);
        tableView.setEditable(true);
        
        /* create columns */
        TableColumn<PolicyRow, Boolean> lineChartEnabled = new TableColumn<>("LineChart");
        TableColumn<PolicyRow, Integer> id = new TableColumn<>("Id");
        TableColumn<PolicyRow, String>  name = new TableColumn<>("Name");
        TableColumn<PolicyRow, String>  srcZone = new TableColumn<>("Source-Zone");
        TableColumn<PolicyRow, String>  dstZone = new TableColumn<>("Destination-Zone");
        dstZone.setPrefWidth(80.0);
        TableColumn<PolicyRow, String>  srcAddress = new TableColumn<>("Source-Address");
        TableColumn<PolicyRow, String>  dstAddress = new TableColumn<>("Destination-Address");
        TableColumn<PolicyRow, Integer> service = new TableColumn<>("Service");
        TableColumn<PolicyRow, Integer> action = new TableColumn<>("Action");
        TableColumn<PolicyRow, Integer> activeStatus = new TableColumn<>("Enabled");
        
        tableView.getColumns().setAll(lineChartEnabled, id, name, srcZone, dstZone, srcAddress, dstAddress, service, action, activeStatus);
        
        lineChartEnabled.setCellValueFactory(new PropertyValueFactory<PolicyRow, Boolean>("lineChartEnabled"));
        lineChartEnabled.setCellFactory(CheckBoxTableCell.forTableColumn(lineChartEnabled));
        lineChartEnabled.setEditable(true);
        
        id.setCellValueFactory(new PropertyValueFactory<PolicyRow, Integer>("id"));
        name.setCellValueFactory(new PropertyValueFactory<PolicyRow, String>("name"));
        srcZone.setCellValueFactory(new PropertyValueFactory<PolicyRow, String>("srcZone"));
        dstZone.setCellValueFactory(new PropertyValueFactory<PolicyRow, String>("dstZone"));
        srcAddress.setCellValueFactory(new PropertyValueFactory<PolicyRow, String>("srcAddress"));
        dstAddress.setCellValueFactory(new PropertyValueFactory<PolicyRow, String>("dstAddress"));
        service.setCellValueFactory(new PropertyValueFactory<PolicyRow, Integer>("service"));
        action.setCellValueFactory(new PropertyValueFactory<PolicyRow, Integer>("action"));
        activeStatus.setCellValueFactory(new PropertyValueFactory<PolicyRow, Integer>("activeStatus"));

        newConnection.setOnAction((event) -> newConnectionDialog());
        newConnectionV3.setOnAction((event) -> newConnectionDialogV3());
        settings.setOnAction((event) -> settingsDialog());
        about.setOnAction((event) -> aboutDialog());

        checkedPolicy = new HashSet<>();
        newRule.setOnAction((event) -> newRuleDialog());
        newRule.setDisable(true);
        
        policyLineChart = new PolicyLineChart(lineChart);

        tableView.setRowFactory(
                tableView -> {
                    final TableRow<PolicyRow> row = new TableRow<>();
                    final ContextMenu rowMenu = new ContextMenu();
                    MenuItem editItem = new MenuItem("Edit Rule...");
                    editItem.setOnAction(event -> editRuleDialog());
                    MenuItem removeItem = new MenuItem("Delete Rule");
                    removeItem.setOnAction(event -> removeRule(row.getItem()));
                    rowMenu.getItems().addAll(editItem, removeItem);

                    // only display context menu for non-null items:
                    row.contextMenuProperty().bind(
                            Bindings.when(Bindings.isNotNull(row.itemProperty()))
                                    .then(rowMenu)
                                    .otherwise((ContextMenu) null));
                    return row;
                });
    }
	
	
	/* methods for establishing connections */

    protected boolean establishSSHConnection(String username, String password) {
        try {
        	session.getFirewall().setDataWriter(new JNS5GTWriter(new SSHConnection(session.getHost(), username, password)));
            session.setLoggedIn(true);
        	return true;
        } catch (Exception e) {
            Dialogs.create()
                    .owner(main.getPrimaryStage())
                    .title("Connection Failed ...")
                    .masthead("Something went wrong")
                    .message(e.getMessage())
                    .showError();
            return true;
        }
    }

    protected boolean establishConnectionV2(String address, int port, String communityName, String securityName) {
        policies.clear();

        boolean test = false;
        if (test) {
            session.setFirewall(getTestFirewall());
            monitorModel = new ThruPutMonitorModel(session.getFirewall());
            (new Refresher(this)).start();
            newRule.setDisable(false);
            return true;
        } else {
            try {
                session.setFirewall(new JNS5GTFirewall(new JNS5GTRetriever(SNMPConnectionFactory.createSNMPv2cConnection(address, port, communityName, securityName)), null));
                monitorModel = new ThruPutMonitorModel(session.getFirewall());
                (new Refresher(this)).start();
                newRule.setDisable(false);
                session.setHost(address);
                return true;
            } catch (Exception e) {
                Dialogs.create()
                        .owner(main.getPrimaryStage())
                        .title("Connection Failed ...")
                        .masthead("Something went wrong")
                        .message(e.getMessage())
                        .showError();
                return false;
            }
        }
    }
    
    protected boolean establishConnectionV3(String address, int port, String username, String authentificationPassword, String securityPassword) {
    	policies.clear();
        
        /* TODO: Remove, for testing purposes only */
        session.setFirewall(getTestFirewall());
        monitorModel = new ThruPutMonitorModel(session.getFirewall());
        (new Refresher(this)).start();
        newRule.setDisable(false);
        return true;
        
        /* TODO: uncomment, for real application
        try {
            session.setFirewall(new JNS5GTFirewall(new JNS5GTRetriever(SNMPConnectionFactory.createSNMPv3Connection(address, port, username, authentificationPassword, securityPassword)), null));
            monitorModel = new ThruPutMonitorModel(session.getFirewall());
            (new Refresher(this)).start();
            newRule.setDisable(false);
            session.setHost(address);
            return true;
        } catch (Exception e) {
            Dialogs.create()
                    .owner(main.getPrimaryStage())
                    .title("Connection Failed ...")
                    .masthead("Something went wrong")
                    .message(e.getMessage())
                    .showError();
            return false;
        }
        */
    }

    
    private void removeRule(PolicyRow tableRow) {
        if (!session.getLoggedIn())
            newSSHConnectionDialog();
        
        JNS5GTPolicy deletePolicy = new JNS5GTPolicy(tableRow.getId(), tableRow.getSrcZone(), tableRow.getDstZone(), tableRow.getSrcAddress(), tableRow.getDstAddress(), tableRow.getService(), tableRow.getAction(), tableRow.getActiveStatus(), tableRow.getName());

        session.getFirewall().getDataWriter().unset(JNS5GTWriter.POLICY, deletePolicy);
        refresh(); // refresh the GUI to remove the rule
    }

    protected void newRule(String name, String sourceZone, String destinationZone, String sourceAddress, String destinationAddress, Integer service, Integer action, Integer enabled) {
    	if (!session.getLoggedIn())
            newSSHConnectionDialog();
 
    	/* TODO: not yet implemented */
    	
    	/*
        JNS5GTPolicy policy = new JNS5GTPolicy(id, sourceZone, destinationZone, sourceAddress, destinationAddress, service, action, enabled, name);

        session.getFirewall().getDataWriter().set(JNS5GTWriter.POLICY, policy);
        refresh();
        */
    }

    @FXML
    private void refreshButtonPressed() {
        refresh();
    }
    
    @Override
    public void refresh() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (session.isConnected()) {
	                refreshLineChart();
                    policies.clear();
                    policies.clear();
	                session.getFirewall().refreshPolicies();
                    for (Policy policy : session.getFirewall().getPolicies()) {
                        PolicyRow pr = new PolicyRow(policy);
                        if(checkedPolicy.contains(pr.getId()))
                            pr.setLineChartEnabled(true);
                        policies.add(pr);
                    }
            	}
            }
        });
    }

    protected void refreshLineChart() {
    	List<Integer> selected = new ArrayList<>();
        checkedPolicy.clear();

        for (PolicyRow row : policies) {
            if (row.getLineChartEnabled()){
                selected.add(row.getId());
                checkedPolicy.add(row.getId());
            }
        }
        policyLineChart.clean();
        monitorModel.refresh();
        policyLineChart.addPolicies(monitorModel, convertIntegers(selected), session.getFirewall());
    }
    
    
    /**
     * http://stackoverflow.com/questions/718554/how-to-convert-an-arraylist-containing-integers-to-primitive-int-array
     */
    public static int[] convertIntegers(List<Integer> integers)
    {
        int[] ret = new int[integers.size()];
        for (int i=0; i < ret.length; i++)
        {
            ret[i] = integers.get(i).intValue();
        }
        return ret;
    }
    
    
    public void setMain(Main main) { this.main = main; }
    
    /* methods for showing the dialogs */
    private void settingsDialog() { main.showSettingsDialog(); }
    private void aboutDialog() { main.showAboutDialog(); }
    private void newConnectionDialog() { main.showNewConnectionDialog(); }
    private void newConnectionDialogV3() { main.showNewConnectionDialogV3(); }
    private void newRuleDialog() { main.showNewRuleDialog(); }
    private void editRuleDialog() { main.showEditRuleDialog(); }
    private void newSSHConnectionDialog() { main.showSSHConnectionDialog(); }
    
    
    private Firewall getTestFirewall(){
        Firewall firewall = mock(Firewall.class);
        ArrayList<Policy> testPolicies = new ArrayList<>();
        JNS5GTPolicy policy = mock(JNS5GTPolicy.class);
        when(policy.getName()).thenReturn("Policy 1");
        when(policy.getThruPut()).thenReturn(2580, 129, 3410, 239, 5, 399, 28, 1000, 2409, 3010, 2912, 10209, 3921, 5201);
        when(policy.getId()).thenReturn(1);
        when(policy.getSrcZone()).thenReturn("Trust");
        when(policy.getDstZone()).thenReturn("Untrust");
        when(policy.getAction()).thenReturn(0);
        when(policy.getActiveStatus()).thenReturn(0);
        when(policy.getSrcAddress()).thenReturn("0.0.0.0");
        when(policy.getDstAddress()).thenReturn("127.0.0.1");
        when(policy.getService()).thenReturn(0);

        JNS5GTPolicy policy2 = mock(JNS5GTPolicy.class);
        when(policy2.getName()).thenReturn("Policy 2");
        when(policy2.getThruPut()).thenReturn(1293, 4192, 3912, 5993, 4393, 83, 999, 444, 5192, 334, 12, 551, 1200, 5060);
        when(policy2.getId()).thenReturn(2);
        when(policy2.getSrcZone()).thenReturn("Trust");
        when(policy2.getDstZone()).thenReturn("Untrust");
        when(policy2.getAction()).thenReturn(0);
        when(policy2.getActiveStatus()).thenReturn(0);
        when(policy2.getSrcAddress()).thenReturn("0.0.0.0");
        when(policy2.getDstAddress()).thenReturn("127.0.0.1");
        when(policy2.getService()).thenReturn(0);

        testPolicies.add(policy);
        testPolicies.add(policy2);

        when(firewall.getPolicies()).thenReturn(testPolicies);
        when(firewall.getPolicy(1)).thenReturn(policy);
        when(firewall.getPolicy(2)).thenReturn(policy2);
        return firewall;
    }
}