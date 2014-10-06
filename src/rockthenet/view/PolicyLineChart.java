package rockthenet.view;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import rockthenet.ThruPutData;

import java.util.ArrayList;

/**
 * Created by gary on 05/10/14.
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class PolicyLineChart {
	private LineChart lineChart;

    public PolicyLineChart(LineChart lineChart) {
        this.lineChart = lineChart;

        this.lineChart.getXAxis().setLabel("Time Unit");
        this.lineChart.getYAxis().setLabel("Bytes Per Sec");

        this.lineChart.setTitle("Policy Line Chart");
        this.lineChart.setCreateSymbols(false);
        this.lineChart.setAnimated(false);
    }

	public void addPolicy(ArrayList<ThruPutData> list, String policyName) {
        XYChart.Series series = new XYChart.Series();
        series.setName(policyName);
        for (ThruPutData data : list) {
            series.getData().add(new XYChart.Data(data.getTimeUnit(), data.getBytesPerSec()));
        }
        lineChart.getData().add(series);
    }
}
