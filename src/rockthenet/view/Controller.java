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
import rockthenet.Main;
import rockthenet.Refreshable;
import rockthenet.Refresher;
import rockthenet.SessionSettings;
import rockthenet.connections.ConnectionException;
import rockthenet.connections.ssh.SSHConnection;
import rockthenet.datamanagement.snmp.JNS5GTRetriever;
import rockthenet.datamanagement.snmp.JNS5GTWriter;
import rockthenet.firewall.Firewall;
import rockthenet.firewall.Policy;
import rockthenet.firewall.ThruPutMonitorModel;
import rockthenet.firewall.jns5gt.JNS5GTFirewall;
import rockthenet.firewall.jns5gt.JNS5GTPolicy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    private TableView<PolicyRow> tableView;
    private ObservableList<PolicyRow> policies;

    private PolicyLineChart policyLineChart;
    private ThruPutMonitorModel monitorModel;

    private Main main;
    private SessionSettings session;

	@FXML
    private void initialize() {
		session = SessionSettings.getInstance(); // start a new session
		
        Image image = new Image(getClass().getResourceAsStream("../resources/refresh-icon.png"));
        refreshButton.setGraphic(new ImageView(image));
        
        /* initialize the table */
        policies = FXCollections.observableArrayList();

        tableView.setItems(policies);
        tableView.setEditable(true);
        
        // Columns
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
        
        policyLineChart = new PolicyLineChart(lineChart);
    }

    protected boolean establishConnection(String address, int port, String commmunityName, String securityName) {
    	policies.clear();
    	
    	/* TODO: only for testing */
    	session.setFirewall(getTestFirewall());
    	
        monitorModel = new ThruPutMonitorModel(session.getFirewall());
        (new Refresher(this)).start();

    	return true;
        
    	/* TODO: uncomment */
    	/*
        try {
            firewall = new JNS5GTFirewall(new JNS5GTRetriever(address, port, commmunityName), new JNS5GTWriter());
            return true;
        } catch (Exception e) {
        	Dialogs.create()
            		.owner(primaryStage)
                    .title("Connection Failed ...")
                    .masthead("Something went wrong")
                    .message(e.getMessage())
                    .showError();
        	
        	return false;
        }
        */
    }

    protected void establishConnectionV3(String address, int port, String username, String authentificationPassword, String securityPassword) {
//        try {
//            firewall = new JNS5GTFirewall(new JNS5GTRetriever(address, port, commmunityName), new JNS5GTWriter());
//        } catch (ConnectionException e) {
//            Dialogs.create()
//                    .owner(primaryStage)
//                    .title("Something went wrong...")
//                    .masthead("ConnectionException")
//                    .message(e.getMessage())
//                    .showError();
//        } catch (MibLoaderException e) {
//            Dialogs.create()
//                    .owner(primaryStage)
//                    .title("Something went wrong...")
//                    .masthead("MibLoaderException")
//                    .message(e.getMessage())
//                    .showError();
//        } catch (IOException e) {
//            Dialogs.create()
//                    .owner(primaryStage)
//                    .title("Something went wrong...")
//                    .masthead("IOException")
//                    .message(e.getMessage())
//                    .showError();
//        }

    }

    @FXML
    private void refreshButtonPressed() {
        refreshLineChart();
    }
    
    @Override
    public void refresh() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
            	if (session.isConnected()) {
	                refreshLineChart();
	                
	                /* TODO: fix CheckBox issue */
	                policies.clear();
	                session.getFirewall().refreshPolicies();
	                for (Policy policy : session.getFirewall().getPolicies())
	                	policies.add(new PolicyRow(policy));
            	}
            }
        });
    }

    protected void refreshLineChart() {
    	List<Integer> selected = new ArrayList<>();
    	for (PolicyRow row : policies) 
    		if (row.getLineChartEnabled()) {
    			selected.add(row.getId());
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
            Firewall firewall = new JNS5GTFirewall(new JNS5GTRetriever("10.0.100.10", 161, "5xHIT"), new JNS5GTWriter(new SSHConnection("10.0.100.10", "5ahit", "Waeng7ohch8o")));
            return firewall;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MibLoaderException e) {
            e.printStackTrace();
        } catch (ConnectionException e) {
            e.printStackTrace();
        }
        
        return null;
    }
}