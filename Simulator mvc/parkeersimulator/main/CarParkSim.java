package parkeersimulator.main;

import parkeersimulator.controller.AbstractController;
import parkeersimulator.controller.CarParkGui;
import parkeersimulator.controller.RunController;
import parkeersimulator.logic.CarParkModel;
import parkeersimulator.view.*;

import javax.swing.*;
import javax.swing.plaf.LayerUI;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * This class creates all needed objects to start.
 */
public class CarParkSim implements PropertyChangeListener {

    private JFrame screen;
    private JFrame options;
    private CarParkModel carParkModel;
    private CarParkView carParkView;
    private AbstractController runController;
    private StatsView statView;
    private CaptionView captionView;
    private ColorOverlay colorOverlay;
    private LayerUI<JComponent> layerUI;
    private JLayer jLayer;
    private CarParkGui startGui;
    /**
     * The constructor
     */

    public CarParkSim() {
        carParkModel = new CarParkModel();
        configSim();
    }

    public CarParkSim(boolean gui) {
        carParkModel = new CarParkModel();
        startGui = new CarParkGui(carParkModel, false);
    }

    public CarParkSim(int numberOfFloors, int numberOfRows, int numberOfPlaces, int numberOfPasses) {
        carParkModel = new CarParkModel(numberOfFloors, numberOfRows, numberOfPlaces, numberOfPasses);
        configSim();
    }



    public void configSim(){
        screen = new JFrame("CityPark Groningen parking simulator");
        carParkModel.addPropertyListener(this);
        colorOverlay = new ColorOverlay(carParkModel);
        // Create the run controller.
        runController = new RunController(carParkModel, screen);
        // Create the other views.
        carParkView = new CarParkView(carParkModel);
        captionView = new CaptionView(carParkModel);
        statView = new StatsView(carParkModel);
        createUI();
    }



    /**
     * Creating the User Interface.
     * Besides the constructor, we got this method to seperate the creating of frame & the models.
     */
    public void createUI() {
        // Creating the frame, where we 'draw' on.

        // Creating a Panel, where we put our loose panels on.
        JPanel jPanel = new JPanel(null);
        // Adding our loose panels on our panel above.
        jPanel.add(captionView);
        captionView.setBounds(850, 342, 200, 300);
        jPanel.add(runController);
        runController.setBounds(0, 500, 800, 200);
        jPanel.add(carParkView);
        carParkView.setBounds(0, 0, 800, 500);
        jPanel.add(statView);
        statView.setBounds(850, 52, 200, 500);
        // Creating a new layer and layer UI.
        layerUI = colorOverlay;
        jLayer = new JLayer<>(jPanel, layerUI);
        // Adding the new layer to our frame.
        screen.add(jLayer);
        screen.setSize(1100, 650);
        screen.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        screen.setVisible(true);
    }

    public void resetSim(){
        screen.dispose();
    }

    /**
     * Overriding a repaint method. Because we want to listen to the fired beans from the model.
     *
     * @param evt fired beans from the model.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        screen.repaint();
        jLayer.repaint();
        jLayer.updateUI();
    }
}
