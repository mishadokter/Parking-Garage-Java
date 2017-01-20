package parkeersimulator.view;

import javax.swing.JPanel;

import parkeersimulator.logic.AbstractModel;
import parkeersimulator.logic.CarParkModel;

public class AbstractView extends JPanel {

    protected CarParkModel model;

    public AbstractView(CarParkModel model) {
        this.model = model;
        model.addView(this);
    }

    public AbstractModel getModel() {
        return this.model;
    }

    public void updateView() {
        repaint();
    }
}
