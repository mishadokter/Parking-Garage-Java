package parkeersimulator.view;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.Plot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import parkeersimulator.logic.CarParkModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.util.Iterator;


public class PieChart extends ApplicationFrame {
    CarParkModel model;
    Thread thread;
    Boolean open;
    PiePlot piePlot;
    DefaultPieDataset dataset = new DefaultPieDataset();

    public PieChart(String title, CarParkModel model) {
        super(title);
        this.model = model;
        open = true;
        JPanel chart = createDemoPanel();
        setContentPane(chart);
        thread = new Thread() {
            public void run() {
                try {
                    while (open) {
                        createDataset();
                        piePlot.setSectionPaint("AdHoc: " + model.getAdHoc(), Color.RED);
                        piePlot.setSectionPaint("Empty: " + model.getEmpty(), Color.WHITE);
                        piePlot.setSectionPaint("Pass: " + model.getPass(), Color.BLUE);
                        piePlot.setSectionPaint("OpenPassPlace: " + model.getPassPlace(), Color.GREEN);
                        piePlot.setSectionPaint("BadParker: " + model.getBad(), Color.GRAY);
                        Thread.sleep(100);
                    }
                } catch (Exception ex) {

                }
            }
        };
        thread.start();
    }

    private JFreeChart createChart(PieDataset dataset) {
        JFreeChart chart = ChartFactory.createPieChart(
                "Garage statistics",  // chart title
                dataset,        // data
                true,           // include legend
                true,
                false);
        piePlot = (PiePlot) chart.getPlot();
        return chart;
    }

    public JPanel createDemoPanel() {
        JFreeChart chart = createChart(createDataset());
        return new ChartPanel(chart);
    }

    private PieDataset createDataset() {
        dataset.clear();
        dataset.setValue("AdHoc: " + model.getAdHoc(), new Double(model.getAdHoc()));
        dataset.setValue("Empty: " + model.getEmpty(), new Double(model.getEmpty()));
        dataset.setValue("Pass: " + model.getPass(), new Double(model.getPass()));
        dataset.setValue("OpenPassPlace: " + model.getPassPlace(), new Double(model.getPassPlace()));
        dataset.setValue("BadParker: " + model.getBad(), new Double(model.getBad()));
        return dataset;
    }

    @Override
    public void windowClosing(WindowEvent event) {
        open = false;
        dispose();
    }
}