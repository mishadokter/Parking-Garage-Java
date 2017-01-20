package parkeersimulator.view;

import javax.swing.JPanel;
import parkeersimulator.logic.Model

public class AbstractView  extends JPanel {

    protected Model model;

    public AbstractView(Model model) {
        this.model = model;
        model.addView(this);
    }

    public getModel() {
        return this.model;
    }

    public void updateView() {
        this.updateView();
        this.repaint();
    }
}
