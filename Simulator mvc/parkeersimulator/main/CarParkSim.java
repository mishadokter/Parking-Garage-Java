package parkeersimulator.main;

import javax.swing.*;
import java.awt.*;

import parkeersimulator.controller.*;
import parkeersimulator.logic.*;
import parkeersimulator.view.*;

/**
 * This class creates all needed objects to start.
 */
public class CarParkSim {

    private JFrame screen;
    private AbstractView carParkView;
    private CarParkModel carParkModel;
    private AbstractController runController;

    /**
     * The constructor
     */
    public CarParkSim() {
        carParkModel = new CarParkModel(3, 6, 30);
        runController = new RunController(carParkModel);
        carParkView = new CarParkView(carParkModel);

        screen = new JFrame("CityPark Groningen parking simulator");
        screen.setSize(850, 650);
        screen.setLayout(null);
        screen.setResizable(false);
        screen.getContentPane().add(carParkView, BorderLayout.CENTER);
        screen.getContentPane().add(runController);
        carParkView.setBounds(10, 10, 800, 500);
        runController.setBounds(0, 550, 450, 50);
        screen.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        screen.setVisible(true);
    }
}
