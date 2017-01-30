package parkeersimulator.main;

import javax.swing.*;
import javax.swing.plaf.LayerUI;
import java.awt.*;
import java.util.Map;
import java.util.TreeMap;

import parkeersimulator.controller.*;
import parkeersimulator.logic.*;
import parkeersimulator.view.*;

/**
 * This class creates all needed objects to start.
 */
public class CarParkSim {

    private JFrame screen;
    private JFrame options;
    private AbstractView carParkView;
    private TimeView timeView;
    private CarParkModel carParkModel;
    private AbstractController runController;
    private CarParkGui guiView;
    private StatsView statView;

    /**
     * The constructor
     */
    public CarParkSim() {
        carParkModel = new CarParkModel();
        runController = new RunController(carParkModel);
        carParkView = new CarParkView(carParkModel);
        timeView = new TimeView(carParkModel);
        guiView = new CarParkGui(carParkModel);
        statView = new StatsView(carParkModel);
        screen = new JFrame("CityPark Groningen parking simulator");

        screen.setSize(1100, 650);
        screen.setLayout(null);
        screen.setResizable(true);
        screen.getContentPane().add(carParkView, BorderLayout.CENTER);
        screen.getContentPane().add(timeView, BorderLayout.SOUTH);
        screen.getContentPane().add(statView, BorderLayout.EAST);
        screen.getContentPane().add(runController);
        timeView.setBounds(350, 510, 200, 50);
        carParkView.setBounds(10, 10, 800, 500);
        runController.setBounds(0, 550, 800, 50);
        statView.setBounds(850,62,200,500);
        screen.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        screen.setVisible(true);
    }

    /*
    * Overloading! jaja
    *
     */
    public CarParkSim(TreeMap<String, String> optionFields) {

        carParkModel = new CarParkModel();
        runController = new RunController(carParkModel);
        carParkView = new CarParkView(carParkModel);
        timeView = new TimeView(carParkModel);

        screen = new JFrame("CityPark Groningen parking simulator");

        JPanel panel = new JPanel();
        panel.setBounds(0, 0, 850, 650);
        Color panelColor = new Color(255, 10, 250, 70);
        panel.setBackground(panelColor);


        screen.setSize(850, 650);
        screen.setLayout(null);
        screen.setResizable(false);
        screen.getContentPane().add(carParkView, BorderLayout.CENTER);
        screen.getContentPane().add(timeView);
        screen.getContentPane().add(runController);

        timeView.setBounds(350, 510, 200, 50);
        //timeView.setBackground(Color.DARK_GRAY);
        carParkView.setBounds(10, 10, 800, 500);
        runController.setBounds(0, 550, 800, 50);
        screen.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        screen.setVisible(true);
    }
}
