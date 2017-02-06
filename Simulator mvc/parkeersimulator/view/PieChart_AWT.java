package parkeersimulator.view;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import parkeersimulator.logic.CarParkModel;

import javax.swing.*;
import java.awt.event.WindowEvent;

public class PieChart_AWT extends ApplicationFrame {
    CarParkModel model;

    public PieChart_AWT(String title, CarParkModel model) {
        super(title);
        this.model = model;
        setContentPane(createDemoPanel());
    }

    private static JFreeChart createChart(PieDataset dataset) {
        JFreeChart chart = ChartFactory.createPieChart(
                "Total cars",  // chart title
                dataset,        // data
                true,           // include legend
                true,
                false);

        return chart;
    }

    public JPanel createDemoPanel() {
        JFreeChart chart = createChart(createDataset());
        return new ChartPanel(chart);
    }

    private PieDataset createDataset() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("AdHoc", new Double(model.getTotal()));
        dataset.setValue("Pass", new Double(model.getOpenSpots()));
        return dataset;
    }

    @Override
    public void windowClosing(WindowEvent event) {
        dispose();
    }
}