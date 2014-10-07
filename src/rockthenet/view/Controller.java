package rockthenet.view;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import net.percederberg.mibble.MibLoaderException;

import org.controlsfx.dialog.Dialogs;

import rockthenet.Main;
import rockthenet.Refreshable;
import rockthenet.Refresher;
import rockthenet.ThruPutMonitorModel;
import rockthenet.connections.ConnectionException;
import rockthenet.connections.ConnectionFactory;
import rockthenet.connections.ReadConnection;
import rockthenet.firewall.Firewall;
import rockthenet.firewall.Policy;
import rockthenet.firewall.jns5gt.JNS5GTFirewall;
import rockthenet.firewall.jns5gt.JNS5GTPolicy;
import rockthenet.firewall.jns5gt.JNS5GTRetriever;
import rockthenet.firewall.jns5gt.JNS5GTWriter;

import java.io.IOException;
import java.util.ArrayList;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 * Created by Samuel on 29.09.2014.
 */
@SuppressWarnings("unchecked")
public class Controller implements Refreshable {

    @FXML
    private MenuItem settings;
    @FXML
    private MenuItem newConnection;
    @FXML
    private MenuItem about;
    
    @SuppressWarnings("rawtypes")
	@FXML
    private LineChart lineChart;
    
    @FXML
    private Button refreshButton;
    @FXML
    private TableView<PolicyRow> tableView;
    private ObservableList<PolicyRow> policies;

    private Main main;

    private PolicyLineChart policyLineChart;

    private ThruPutMonitorModel monitorModel;

    private Firewall firewall;
    private ReadConnection readConnection;
    private JNS5GTRetriever retriever;
    private JNS5GTWriter writer;


	@FXML
    private void initialize() {
        Image image = new Image(getClass().getResourceAsStream("../resources/refresh-icon.png"));
        refreshButton.setGraphic(new ImageView(image));

        //TODO: fix Table resize
        
        // Table-Stuff
        policies = FXCollections.observableArrayList();
        policies.add(new PolicyRow());
        policies.add(new PolicyRow());
        policies.add(new PolicyRow());

        tableView.setItems(policies);
        tableView.setEditable(true);
        
        // Columns
        TableColumn<PolicyRow, Boolean> lineChartEnabled = new TableColumn<>("LineChart");
        TableColumn<PolicyRow, String> name = new TableColumn<>("Name");
        TableColumn<PolicyRow, String> srcZone = new TableColumn<>("Source-Zone");
        TableColumn<PolicyRow, String> dstZone = new TableColumn<>("Destination-Zone");
        TableColumn<PolicyRow, String> srcAddress = new TableColumn<>("Source-Address");
        TableColumn<PolicyRow, String> dstAddress = new TableColumn<>("Destination-Address");
        TableColumn<PolicyRow, Integer> service = new TableColumn<>("Service");
        TableColumn<PolicyRow, Integer> action = new TableColumn<>("Action");
        TableColumn<PolicyRow, Integer> activeStatusProperty = new TableColumn<>("Enabled");
        
        lineChartEnabled.setCellValueFactory(new PropertyValueFactory<PolicyRow, Boolean>("lineChartEnabled"));
        lineChartEnabled.setCellFactory(CheckBoxTableCell.forTableColumn(lineChartEnabled));
        lineChartEnabled.setEditable(true);
        
        name.setCellValueFactory(new PropertyValueFactory<PolicyRow, String>("name"));
        srcZone.setCellValueFactory(new PropertyValueFactory<PolicyRow, String>("srcZone"));
        dstZone.setCellValueFactory(new PropertyValueFactory<PolicyRow, String>("dstZone"));
        srcAddress.setCellValueFactory(new PropertyValueFactory<PolicyRow, String>("srcAddress"));
        dstAddress.setCellValueFactory(new PropertyValueFactory<PolicyRow, String>("dstAddress"));
        service.setCellValueFactory(new PropertyValueFactory<PolicyRow, Integer>("service"));
        action.setCellValueFactory(new PropertyValueFactory<PolicyRow, Integer>("action"));
        activeStatusProperty.setCellValueFactory(new PropertyValueFactory<PolicyRow, Integer>("activeStatusProperty"));
        
        tableView.getColumns().setAll(lineChartEnabled, name, srcZone, dstZone, srcAddress, dstAddress, service, action, activeStatusProperty);

        settings.setOnAction((event) -> settingsDialog());
        newConnection.setOnAction((event) -> newConnectionDialog());
        about.setOnAction((event) -> aboutDialog());



        (new Refresher(4000, this)).start();

        // TODO: Only for testing purposes
        firewall = getFirewall();

        policyLineChart = new PolicyLineChart(lineChart);
        monitorModel = new ThruPutMonitorModel(firewall);
    }

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

    private Firewall getFirewall(){
        try {
            Firewall firewall = new JNS5GTFirewall(new JNS5GTRetriever("10.0.100.10", 161, "5xHIT"), new JNS5GTWriter());
            return firewall;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MibLoaderException e) {
            e.printStackTrace();
        } catch (ConnectionException e) {
            e.printStackTrace();
        }
        return  firewall;
    }
    /**
     * Enables the New Connection Dialog Scene(Window)
     */
    private void newConnectionDialog() {
        main.showNewConnectionDialog();
    }

    protected void establishConnection(String address, int port, String commmunityName, String securityName) {
        try {
            readConnection = ConnectionFactory.createSNMPv2cConnection(address, port, commmunityName, securityName);
            retriever = new JNS5GTRetriever(readConnection);
            writer = new JNS5GTWriter();
            firewall = new JNS5GTFirewall(retriever, writer);
        } catch (ConnectionException e) {
            Dialogs.create()
                    .title("Something went wrong...")
                    .masthead("ConnectionException")
                    .message(e.getMessage())
                    .showError();
        } catch (MibLoaderException e) {
            Dialogs.create()
                    .title("Something went wrong...")
                    .masthead("MibLoaderException")
                    .message(e.getMessage())
                    .showError();
        } catch (IOException e) {
            Dialogs.create()
                    .title("Something went wrong...")
                    .masthead("IOException")
                    .message(e.getMessage())
                    .showError();
        }

    }

    /**
     * Enables the Settings Dialog Scene(Window)
     */
    private void settingsDialog() {
        main.showSettingsDialog();
    }

    /**
     * Enables the About Dialog Scene(Window)
     */
    private void aboutDialog() {
        main.showAboutDialog();
    }

    public void setMain(Main main) {
        this.main = main;
    }

    @FXML
    private void refreshButtonPressed() {
        refreshLineChart();
    }

    protected void refreshLineChart() {
        int[] selected = {1, 2, 3};

        policyLineChart.clean();
        monitorModel.refresh();
        policyLineChart.addPolicies(monitorModel, selected, firewall);
    }

    @Override
    public void refresh() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                refreshLineChart();
            }
        });
    }
}