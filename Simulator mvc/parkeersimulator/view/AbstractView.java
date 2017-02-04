package parkeersimulator.view;

import javax.swing.JPanel;

import parkeersimulator.logic.AbstractModel;
import parkeersimulator.logic.CarParkModel;

import java.awt.*;

public class AbstractView extends JPanel {

    protected CarParkModel model;

    public AbstractView(CarParkModel model) {
        this.model = model;
        model.addView(this);
    }

    /**
     * Gets the main model.
     * @return Gets the main model.
     */
    public AbstractModel getModel() {
        return model;
    }

    /**
     * Updates the view.
     */
    public void updateView() {
        repaint();
    }
}
