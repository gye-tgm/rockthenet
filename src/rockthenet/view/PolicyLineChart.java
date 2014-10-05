package rockthenet.view;

import javafx.beans.NamedArg;
import javafx.collections.ObservableList;
import javafx.scene.chart.Axis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;

/**
 * Created by gary on 05/10/14.
 */
public class PolicyLineChart extends LineChart {
    public PolicyLineChart(@NamedArg("xAxis") Axis axis, @NamedArg("yAxis") Axis axis2, @NamedArg("data") ObservableList data) {
        super(axis, axis2, data);
        setTitle("ThruPut Monitoring");
    }
    /*
    public PolicyLineChart() {

        super(new NumberAxis("Time", 0.0, 10.0, 1), new NumberAxis("Bytes Per Second", 0.0, 100.0, 1));
        setTitle("ThruPut Monitoring");
    } */

}
