package parkeersimulator.main;

import parkeersimulator.controller.AbstractController;
import parkeersimulator.controller.CarParkGui;
import parkeersimulator.controller.RunController;
import parkeersimulator.logic.CarParkModel;
import parkeersimulator.view.*;

import javax.swing.*;
import javax.swing.plaf.LayerUI;
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

    /**
     * The constructor
     */
    public CarParkSim() {
        // Create the model.
        carParkModel = new CarParkModel();
        // Add a listener. if the're is a bean fired. we want to do something with this.
        carParkModel.addPropertyListener(this);
        colorOverlay = new ColorOverlay(carParkModel);
        // Create the run controller.
        runController = new RunController(carParkModel);
        // Create the other views.
        carParkView = new CarParkView(carParkModel);
        captionView = new CaptionView(carParkModel);
        statView = new StatsView(carParkModel);
        // Create the 'adjust' frame.
        CarParkGui guiView = new CarParkGui(carParkModel);
        // Create and show the actual pane.
        createUI();
    }

    /**
     * Creating the User Interface.
     * Besides the constructor, we got this method to seperate the creating of frame & the models.
     */
    public void createUI() {
        // Creating the frame, where we 'draw' on.
        screen = new JFrame("CityPark Groningen parking simulator");
        // Creating a Panel, where we put our loose panels on.
        JPanel jPanel = new JPanel(null);
        // Adding our loose panels on our panel above.
        jPanel.add(captionView);
        captionView.setBounds(850, 362, 200, 200);
        jPanel.add(runController);
        runController.setBounds(0, 550, 800, 500);
        jPanel.add(carParkView);
        carParkView.setBounds(10, 10, 800, 500);
        jPanel.add(statView);
        statView.setBounds(850, 62, 200, 500);
        // Creating a new layer and layer UI.
        layerUI = colorOverlay;
        jLayer = new JLayer<>(jPanel, layerUI);
        // Adding the new layer to our frame.
        screen.add(jLayer);
        screen.setSize(1100, 650);
        screen.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        screen.setVisible(true);
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
        System.out.println("Change");
    }


}
