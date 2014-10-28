package rockthenet.view;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import rockthenet.firewall.ThruPutData;
import rockthenet.firewall.ThruPutMonitorModel;
import rockthenet.firewall.Firewall;

import java.util.ArrayList;

/**
 * Handles the View element of the policy line chart.
 * @author Gary Ye
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class PolicyLineChart {
	private LineChart lineChart;

    /**
     * Constructs a new policy line chart object.
     * @param lineChart the line chart view element
     */
    public PolicyLineChart(LineChart lineChart) {
        this.lineChart = lineChart;

        this.lineChart.getXAxis().setLabel("Time Unit");
        this.lineChart.getYAxis().setLabel("Bytes Per Sec");

        //this.lineChart.setTitle("Policy Thru Put");
        this.lineChart.setCreateSymbols(false);
        this.lineChart.setAnimated(false);
    }

    /**
     * Adds a policy to the line chart, it will be displayed right after adding.
     * @param list the list of data which will be needed for the poly line
     * @param policyName the policy name that will be shown as the key
     */
	public void addPolicy(ArrayList<ThruPutData> list, String policyName) {
        XYChart.Series series = new XYChart.Series();
        series.setName(policyName);
        for (ThruPutData data : list) {
            series.getData().add(new XYChart.Data(data.getTimeUnit(), data.getBytesPerSec()));
        }
        lineChart.getData().add(series);
    }

    /**
     * The clean method removes every policy off the display.
     */
    public void clean(){
        lineChart.getData().clear();
    }

    /**
     * Adds all policies of the given model with the selected id values.
     * @param monitorModel the model that has the wanted data
     * @param selected the ids of the policies that should be added
     * @param firewall the firewall for retrieving additional values
     */
    public void addPolicies(ThruPutMonitorModel monitorModel, int[] selected, Firewall firewall) {
        for(int i = 0; i < selected.length; i++){
            if(monitorModel.getPolicyHistory(selected[i]) != null)
                addPolicy(monitorModel.getPolicyHistory(selected[i]), ""+firewall.getPolicy(selected[i]).getId());

        }
    }
}
