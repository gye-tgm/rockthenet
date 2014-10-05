package rockthenet.view;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import rockthenet.Main;
import rockthenet.Refreshable;
import rockthenet.Refresher;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Samuel on 29.09.2014.
 */
public class Controller implements Refreshable {

    @FXML
    private Menu menuBar;
    @FXML
    private MenuItem settings;
    @FXML
    private MenuItem newConnection;
    @FXML
    private MenuItem about;
    @FXML
    private LineChart lineChart;
    @FXML
    private Button refreshButton;

    private Main main;

    private int refreshTime;

    @FXML
    private void initialize() {
        Image image = new Image(getClass().getResourceAsStream("../resources/refresh-icon.png"));
        refreshButton.setGraphic(new ImageView(image));

        settings.setOnAction((event) -> settingsDialog());
        newConnection.setOnAction((event) -> newConnectionDialog());
        about.setOnAction((event) -> aboutDialog());
        refreshTime = 4000;
        (new Refresher(refreshTime, this)).start();
    }

    /**
     * Enables the New Connection Dialog Scene(Window)
     */
    private void newConnectionDialog() {
        main.showNewConnectionDialog();
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
        lineChart.setTitle("Monitoring Thru Put");

        List<String> rules = getLineChartRule();
        for (int i = 0; i < rules.size(); i++) {
            XYChart.Series series = new XYChart.Series();
            series.setName(rules.get(i));
            for (int d = 0; d < refreshTime; d++) {
                int[] a = getData(d, rules.get(i));
                series.getData().add(new XYChart.Data(a[0], a[1]));
            }
            lineChart.setCreateSymbols(false);
            // lineChart.getData().add(series);
        }

    }

    /**
     * Gives the data through-put from a firewall rule at a specific time back.
     * @param time The time at which the data is measured
     * @param name The name of the rule
     * @return
     */
    public int[] getData(int time, String name){
        //TODO Real Data
        int [] a = {time, (int)(Math.random()*30)};
        return a;
    }

    public List<String> getLineChartRule(){
        //TODO Real Rules which are checked
        ArrayList<String> rules = new ArrayList<String>();
        for(int i = 0; i <= 2; i++ ){
            rules.add(i,"test"+1);
        }
        return rules;
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