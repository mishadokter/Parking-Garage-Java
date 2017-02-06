package parkeersimulator.view;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import parkeersimulator.logic.CarParkModel;

import java.awt.*;
import java.awt.event.WindowEvent;

public class BarChart extends ApplicationFrame {

    private CarParkModel model;
    private Boolean open = true;
    private Thread thread;
    private DefaultCategoryDataset dataset;

    public BarChart(String applicationTitle, String chartTitle, CarParkModel model) {
        super(applicationTitle);
        this.model = model;
        dataset = new DefaultCategoryDataset();
        JFreeChart barChart = ChartFactory.createBarChart(
                chartTitle,
                "Garage spot",
                "Amount of cars",
                createDataset(),
                PlotOrientation.VERTICAL,
                true, true, false);
        ChartPanel chartPanel = new ChartPanel(barChart);
        chartPanel.setPreferredSize(new java.awt.Dimension(560, 367));
        setContentPane(chartPanel);
        barChart.getCategoryPlot().getRangeAxis().setRange(0.0, 540.0);
        barChart.getCategoryPlot().getRangeAxis().setAutoRange(false);
        chartPanel.setDomainZoomable(false);
        chartPanel.setRangeZoomable(false);

        thread = new Thread() {
            public void run() {
                try {
                    while (open) {
                        createDataset();
                        Thread.sleep(100);
                    }
                } catch (Exception ex) {

                }
            }
        };
        thread.start();
    }

    private CategoryDataset createDataset() {
        dataset.clear();
        final String adhoc = "Adhoc";
        final String pass = "PassCar";
        final String badparker = "BadParker";
        final String empty = "Empty";
        final String passplace = "OpenPassPlace";
        final String amount = "Amount";

        dataset.addValue(model.getAdHoc(), adhoc, amount);
        dataset.addValue(model.getPass(), pass, amount);
        dataset.addValue(model.getBad(), badparker, amount);
        dataset.addValue(model.getPassPlace(), passplace, amount);
        dataset.addValue(model.getEmpty(), empty, amount);
        return dataset;
    }

    @Override
    public void windowClosing(WindowEvent event) {
        open = false;
        dispose();
    }
}
