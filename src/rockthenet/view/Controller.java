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
        // Columns
        TableColumn<PolicyRow, Boolean> lineChartEnabled = new TableColumn<>("LineChart");
        lineChartEnabled.setCellValueFactory(new PropertyValueFactory("lineChartEnabled"));
        TableColumn<PolicyRow, String> name = new TableColumn<>("Name");
        name.setCellValueFactory(new PropertyValueFactory("name"));
        TableColumn<PolicyRow, String> srcZone = new TableColumn<>("Source-Zone");
        srcZone.setCellValueFactory(new PropertyValueFactory("srcZone"));
        TableColumn<PolicyRow, String> dstZone = new TableColumn<>("Destination-Zone");
        dstZone.setCellValueFactory(new PropertyValueFactory("dstZone"));
        TableColumn<PolicyRow, String> srcAddress = new TableColumn<>("Source-Address");
        srcAddress.setCellValueFactory(new PropertyValueFactory("srcAddress"));
        TableColumn<PolicyRow, String> dstAddress = new TableColumn<>("Destination-Address");
        dstAddress.setCellValueFactory(new PropertyValueFactory("dstAddress"));
        TableColumn<PolicyRow, Integer> service = new TableColumn<>("Service");
        service.setCellValueFactory(new PropertyValueFactory("service"));
        TableColumn<PolicyRow, Integer> action = new TableColumn<>("Action");
        action.setCellValueFactory(new PropertyValueFactory("action"));
        TableColumn<PolicyRow, Integer> activeStatusProperty = new TableColumn<>("Enabled");
        activeStatusProperty.setCellValueFactory(new PropertyValueFactory("activeStatusProperty"));

        tableView.getColumns().setAll(lineChartEnabled, name, srcZone, dstZone, srcAddress, dstAddress,
                service, action, activeStatusProperty);



        settings.setOnAction((event) -> settingsDialog());
        newConnection.setOnAction((event) -> newConnectionDialog());
        about.setOnAction((event) -> aboutDialog());


        (new Refresher(4000, this)).start();

        // TODO: Only for testing purposes
        firewall = mock(Firewall.class);

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

        policyLineChart = new PolicyLineChart(lineChart);
        monitorModel = new ThruPutMonitorModel(firewall);
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
    private void refreshButtonPressed(){
        refreshLineChart();
    }

    protected void refreshLineChart(){
        lineChart.getData().clear();

        int[] selected = {1, 2};

        monitorModel.refresh();
        for(int i = 0; i < selected.length; i++){
            policyLineChart.addPolicy(monitorModel.getPolicyHistory(selected[i]), firewall.getPolicy(selected[i]).getName());
        }
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