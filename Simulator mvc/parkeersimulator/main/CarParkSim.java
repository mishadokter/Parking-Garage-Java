package parkeersimulator.main;

import parkeersimulator.controller.AbstractController;
import parkeersimulator.controller.RunController;
import parkeersimulator.logic.CarParkModel;
import parkeersimulator.view.AbstractView;
import parkeersimulator.view.CarParkView;
import parkeersimulator.view.StatsView;

import javax.swing.*;
import java.awt.*;
import java.util.TreeMap;

/**
 * This class creates all needed objects to start.
 */
public class CarParkSim {

    private JFrame screen;
    private JFrame options;
    private AbstractView carParkView;
    private CarParkModel carParkModel;
    private AbstractController runController;
    private StatsView statView;

    /**
     * The constructor
     */
    public CarParkSim() {
        carParkModel = new CarParkModel(3, 6, 30);
        runController = new RunController(carParkModel);
        carParkView = new CarParkView(carParkModel);
        statView = new StatsView(carParkModel);

        screen = new JFrame("CityPark Groningen parking simulator");
        screen.setSize(850, 650);
        screen.setLayout(null);
        screen.setResizable(false);
        screen.getContentPane().add(carParkView, BorderLayout.CENTER);
        screen.getContentPane().add(runController);
        screen.getContentPane().add(statView);
        carParkView.setBounds(10, 10, 800, 500);
        runController.setBounds(0, 550, 500, 50);
        screen.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        screen.setVisible(true);
    }
    /*
    * Overloading! jaja
    *
     */
    public CarParkSim(TreeMap<String,String> optionFields) {

        carParkModel = new CarParkModel(optionFields);
        runController = new RunController(carParkModel);
        carParkView = new CarParkView(carParkModel);
        statView = new StatsView(carParkModel);

        screen = new JFrame("CityPark Groningen parking simulator");
        screen.setSize(850, 650);
        screen.setLayout(null);
        screen.setResizable(false);
        screen.getContentPane().add(carParkView, BorderLayout.CENTER);
        screen.getContentPane().add(runController);
        screen.getContentPane().add(statView);
        carParkView.setBounds(10, 10, 800, 500);
        runController.setBounds(0, 550, 450, 50);
        screen.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        screen.setVisible(true);
    }
}
