package rockthenet.view;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import rockthenet.ThruPutData;
import rockthenet.ThruPutMonitorModel;
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

        this.lineChart.setTitle("Policy Thru Put");
        this.lineChart.setCreateSymbols(false);
        this.lineChart.setAnimated(false);
    }

    /**
     * Adds a policy to the line chart
     * @param list
     * @param policyName
     */
	public void addPolicy(ArrayList<ThruPutData> list, String policyName) {
        XYChart.Series series = new XYChart.Series();
        series.setName(policyName);
        for (ThruPutData data : list) {
            series.getData().add(new XYChart.Data(data.getTimeUnit(), data.getBytesPerSec()));
        }
        lineChart.getData().add(series);
    }

    public void clean(){
        lineChart.getData().clear();
    }

    public void addPolicies(ThruPutMonitorModel monitorModel, int[] selected, Firewall firewall) {
        for(int i = 0; i < selected.length; i++){
            if(monitorModel.getPolicyHistory(selected[i]) != null)
                addPolicy(monitorModel.getPolicyHistory(selected[i]), firewall.getPolicy(selected[i]).getName());
        }
    }
}
