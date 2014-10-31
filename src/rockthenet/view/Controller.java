package rockthenet.view;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
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
import rockthenet.SessionSettings;
import rockthenet.utils.Refreshable;
import rockthenet.utils.Refresher;
import rockthenet.connections.snmp.SNMPConnectionFactory;
import rockthenet.connections.ssh.SSHConnection;
import rockthenet.datamanagement.snmp.JNS5GTRetriever;
import rockthenet.datamanagement.snmp.JNS5GTWriter;
import rockthenet.firewall.Firewall;
import rockthenet.firewall.Policy;
import rockthenet.firewall.jns5gt.JNS5GTFirewall;
import rockthenet.firewall.jns5gt.JNS5GTPolicy;
import rockthenet.firewall.jns5gt.ThruPutMonitorModel;

import java.util.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * This is the control class of the main GUI-frame.
 *
 * @author Samuel Schmidt
 * @author Elias Frantar
 * @author Nikolaus  Schrack
 * @version 2014-10-29
 */
@SuppressWarnings("unchecked")
public class Controller implements Refreshable {

    /* fields mapped to FXML */
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
    private ProgressIndicator progressIndicator;
    @FXML
    private Button newRule;
    @FXML
    private TableView<PolicyRow> tableView;

    /* other attributes */
    private ObservableList<PolicyRow> policies; // data model for the table
    private HashMap<Integer, PolicyRow> policy; // maps ids to their corresponding policies

    /* line chart */
    private PolicyLineChart policyLineChart;
    private ThruPutMonitorModel monitorModel;

    private Main main;
    private SessionSettings session;
    private List<Integer> selected; // selected rules

    @FXML
    private void initialize() {
        session = SessionSettings.getInstance(); // start a new session

		/* configure the image of the refresh-button */
        Image image = new Image(getClass().getResourceAsStream("../resources/refresh-icon.png"));
        refreshButton.setGraphic(new ImageView(image));

        selected = new ArrayList<>(); // per default there are no rules selected
        
        /* initialize the table */
        policies = FXCollections.observableArrayList();
        policy = new HashMap<Integer, PolicyRow>();
        tableView.setItems(policies);
        tableView.setEditable(true);
        
        /* create columns */
        TableColumn<PolicyRow, Boolean> lineChartEnabled = new TableColumn<>("LineChart");
        TableColumn<PolicyRow, Integer> id = new TableColumn<>("Id");
        TableColumn<PolicyRow, String> name = new TableColumn<>("Name");
        TableColumn<PolicyRow, String> srcZone = new TableColumn<>("Source-Zone");
        TableColumn<PolicyRow, String> dstZone = new TableColumn<>("Destination-Zone");
        dstZone.setPrefWidth(80.0);
        TableColumn<PolicyRow, String> srcAddress = new TableColumn<>("Source-Address");
        TableColumn<PolicyRow, String> dstAddress = new TableColumn<>("Destination-Address");
        TableColumn<PolicyRow, Integer> service = new TableColumn<>("Service");
        TableColumn<PolicyRow, Integer> action = new TableColumn<>("Action");
        TableColumn<PolicyRow, Integer> activeStatus = new TableColumn<>("Enabled");

        tableView.getColumns().setAll(lineChartEnabled, id, name, srcZone, dstZone, srcAddress, dstAddress, service,
                action, activeStatus);

        /* configure as CheckBoxCells */
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

        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); // dynamically resize the columns

        /* apply action-handlers to all buttons */
        newConnection.setOnAction((event) -> newConnectionDialog());
        newConnectionV3.setOnAction((event) -> newConnectionDialogV3());
        settings.setOnAction((event) -> settingsDialog());
        about.setOnAction((event) -> aboutDialog());
        refreshButton.setOnAction((event) -> refreshButtonPressed());
        newRule.setOnAction((event) -> newRuleButtonPressed());
        newRule.setDisable(true);

        policyLineChart = new PolicyLineChart(lineChart);

        /* add a context-menu to all row in the table */
        tableView.setRowFactory(
                tableView -> {
                    final TableRow<PolicyRow> row = new TableRow<>();
                    final ContextMenu rowMenu = new ContextMenu();
                    MenuItem editItem = new MenuItem("Edit Rule...");
                    editItem.setOnAction(event -> editButtonPressed(row.getItem()));
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

    /**
     * Tries to establish a new SSH-connection. <br>
     * Shows an error-dialog if failing.
     *
     * @param username the username to use for connecting
     * @param password the corresponding password
     * @return true if a connection could be successfully established; false otherwise
     */
    protected boolean establishSSHConnection(String username, String password) {
        try {
            session.getFirewall().setDataWriter(new JNS5GTWriter(new SSHConnection(session.getHost(), username,
                    password)));
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

    /**
     * Tries to establish a new SNMPv2c-connection. <br>
     * Shows an error-dialog if failing.
     *
     * @param address       the address to connect to
     * @param port          the port to connect to
     * @param communityName the community to connect to
     * @param securityName  the corresponding security-name
     * @return true if a connection could be successfully established; false otherwise
     */
    protected boolean establishConnectionV2(String address, int port, String communityName, String securityName) {
        policies.clear();

        /* 
        // uncomment for testing
        session.setFirewall(getTestFirewall());
        monitorModel = new ThruPutMonitorModel(session.getFirewall());
        (new Refresher(this)).start();
        newRule.setDisable(false);
        return true;
        */

        try {
            session.setFirewall(new JNS5GTFirewall(new JNS5GTRetriever(SNMPConnectionFactory.createSNMPv2cConnection
                    (address, port, communityName, securityName)), null));
            monitorModel = new ThruPutMonitorModel((JNS5GTFirewall) session.getFirewall());
            session.setRefresher(new Refresher(this, -1));
            session.getRefresher().start();
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

    /**
     * Tries to establish a new SNMPv3-connection. <br>
     * Shows an error-dialog if failing.
     *
     * @param address                  the address to connect to
     * @param port                     the port to connect to
     * @param username                 the username to use for connecting
     * @param authentificationPassword the corresponding authentificationPassword
     * @param securityPassword         the corresponding securityPassword
     * @return true if a connection could be successfully established; false otherwise
     */
    protected boolean establishConnectionV3(String address, int port, String username,
                                            String authentificationPassword, String securityPassword) {
        policies.clear();
        
        /*
        // ucomment for testing
        session.setFirewall(getTestFirewall());
        monitorModel = new ThruPutMonitorModel(session.getFirewall());
        (new Refresher(this)).start();
        newRule.setDisable(false);
        return true;
        */

        try {
            session.setFirewall(new JNS5GTFirewall(new JNS5GTRetriever(SNMPConnectionFactory.createSNMPv3Connection
                    (address, port, username, authentificationPassword, securityPassword)), null));
            monitorModel = new ThruPutMonitorModel((JNS5GTFirewall) session.getFirewall());
            session.setRefresher(new Refresher(this, -1));
            session.getRefresher().start();

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
    
    
    /* methods for adding, removing and updating rules */

    /**
     * Deletes the given rule on the firewall.
     *
     * @param tableRow the rule to delete (as row from the table)
     */
    private void removeRule(PolicyRow tableRow) {
        if (!session.getLoggedIn())
            if (!main.showSSHConnectionDialog())
                return;

        JNS5GTPolicy deletePolicy = new JNS5GTPolicy(tableRow.getId(), tableRow.getSrcZone(), tableRow.getDstZone(),
                tableRow.getSrcAddress(), tableRow.getDstAddress(), tableRow.getService(), tableRow.getAction(),
                tableRow.getActiveStatus(), tableRow.getName());

        session.getFirewall().deletePolicy(deletePolicy);
        refresh(); // refresh the GUI to remove the rule
    }

    /**
     * Creates a new rule on the firewall with the values specified as parameters:
     *
     * @param id
     * @param name
     * @param sourceZone
     * @param destinationZone
     * @param sourceAddress
     * @param destinationAddress
     * @param service
     * @param action
     * @param enabled
     */
    protected void newRule(int id, String name, String sourceZone, String destinationZone, String sourceAddress,
                           String destinationAddress, Integer service, Integer action, Integer enabled) {
        session.getFirewall().addPolicy(new JNS5GTPolicy(id, sourceZone, destinationZone, sourceAddress,
                destinationAddress, service, action, enabled, name));
        refresh();
    }

    /**
     * Updates rule on the firewall with the values specified as parameters:
     *
     * @param id                 the id of the rule to update
     * @param name
     * @param sourceZone
     * @param destinationZone
     * @param sourceAddress
     * @param destinationAddress
     * @param service
     * @param action
     * @param enabled
     */
    protected void updateRule(int id, String name, String sourceZone, String destinationZone, String sourceAddress,
                              String destinationAddress, Integer service, Integer action, Integer enabled) {
        Policy old = new JNS5GTPolicy();
        old.setId(id);

        session.getFirewall().updatePolicy(old, new JNS5GTPolicy(id, sourceZone, destinationZone, sourceAddress,
                destinationAddress, service, action, enabled, name));
        refresh();
    }
    
    
    /* button handlers */

    /**
     * Handles the click-event of the refresh-button
     */
    private void refreshButtonPressed() {
        if (policies.size() > 0) { // doens't do anything when there are no policies
            buttonThread();
        }
    }

    /**
     * Starts a new refresh-Thread
     */
    private void buttonThread() {
        Thread a = new Thread(new Runnable() {
            public void run() {
                refresh();
            }

        });
        a.setDaemon(true); // TODO: remove this to fix previously reported issue?
        a.start();
    }

    /**
     * Handles the click-event of the newRule-button
     */
    private void newRuleButtonPressed() {
        if (!session.getLoggedIn())
            if (!main.showSSHConnectionDialog())
                return;

        newRuleDialog();
    }

    /**
     * Handles the click-event of the editRule-button
     */
    private void editButtonPressed(PolicyRow policy) {
        if (!session.getLoggedIn())
            if (!main.showSSHConnectionDialog())
                return;

        editRuleDialog(policy);
    }

    @Override
    public void refresh() {
        Platform.runLater(new Runnable() {
            public void run() {
                progressIndicator.setVisible(true);
            }
        });
        session.getFirewall().refreshPolicies();

        HashMap<Integer, PolicyRow> addPr = new HashMap<Integer, PolicyRow>();
        LinkedList<PolicyRow> objectRemove = new LinkedList<PolicyRow>();
        LinkedList<Integer> oldId = new LinkedList<Integer>();
        LinkedList<Integer> newId = new LinkedList<Integer>();
        //Getting the old ids from the table
        for (int i : policy.keySet()) {
            oldId.add(i);
        }
        //Getting the refreshed ids from the firewall
        for (Policy newPolicy : session.getFirewall().getPolicies()) {
            IntegerProperty id = new SimpleIntegerProperty(newPolicy.getId());
            newId.add(id.getValue());
            //If the new id wasn't in the old ids it is added
            if (!oldId.contains(id.getValue()))
                addPr.put(newPolicy.getId(), new PolicyRow(newPolicy));
        }
        policy.putAll(addPr);

        //Getting the ids that were removed
        for (int i : oldId) {
            if (!newId.contains(i)) {
                objectRemove.add(policy.get(i));
                policy.remove(i);
            }
        }
        refreshLineChartPre();

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (session.isConnected()) {
                    Iterator<PolicyRow> it = policies.iterator();
                    while (it.hasNext()) {
                        PolicyRow user = it.next();
                        for (PolicyRow row : objectRemove) {
                            if (user.getId().equals(row.getId())) {
                                it.remove();
                            }
                        }
                    }

                    policies.addAll(addPr.values());
                    refreshLineChart();

                    progressIndicator.setVisible(false);
                }
            }
        });
    }

    /**
     * Sets up the refreshing of the line-chart by fetching the current data from the firewall
     */
    protected void refreshLineChartPre() {
        for (PolicyRow row : policy.values()) {
            if (row.getLineChartEnabled()) {
                selected.add(row.getId());
            }
        }
        monitorModel.refresh();
    }

    /**
     * Redraws the line-chart
     */
    protected void refreshLineChart() {
        policyLineChart.clean();
        policyLineChart.addPolicies(monitorModel, convertIntegers(selected), session.getFirewall());
        selected.clear();
    }

    /**
     * http://stackoverflow.com/questions/718554/how-to-convert-an-arraylist-containing-integers-to-primitive-int-array
     */
    public static int[] convertIntegers(List<Integer> integers) {
        int[] ret = new int[integers.size()];
        for (int i = 0; i < ret.length; i++)
            ret[i] = integers.get(i);
        return ret;
    }

    /* simple Getters and Setters; no documentation necessary */
    public void setMain(Main main) {
        this.main = main;
    }

    /* methods for showing the dialogs; no documentation necessary */
    private void settingsDialog() {
        main.showSettingsDialog();
    }

    private void aboutDialog() {
        main.showAboutDialog();
    }

    private void newConnectionDialog() {
        main.showNewConnectionDialog();
    }

    private void newConnectionDialogV3() {
        main.showNewConnectionDialogV3();
    }

    private void newRuleDialog() {
        main.showNewRuleDialog();
    }

    private void editRuleDialog(PolicyRow policy) {
        main.showEditRuleDialog(policy);
    }

    /**
     * Creates a mocked test-firewall.
     * <p>
     * <p><b>For testing only!</b>
     *
     * @return a fully initialized mocked test-firewall
     */
    @SuppressWarnings("unused")
    private Firewall getTestFirewall() {
        Firewall firewall = mock(Firewall.class);
        ArrayList<Policy> testPolicies = new ArrayList<>();
        JNS5GTPolicy policy = mock(JNS5GTPolicy.class);
        when(policy.getName()).thenReturn("Policy 1");
        when(policy.getThruPut()).thenReturn(2580, 129, 3410, 239, 5, 399, 28, 1000, 2409, 3010, 2912, 10209, 3921,
                5201);
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
        when(policy2.getThruPut()).thenReturn(1293, 4192, 3912, 5993, 4393, 83, 999, 444, 5192, 334, 12, 551, 1200,
                5060);
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
