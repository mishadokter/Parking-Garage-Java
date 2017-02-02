package parkeersimulator.view;

import parkeersimulator.logic.CarParkModel;

import javax.swing.*;
import javax.swing.plaf.LayerUI;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Created by sanderpost on 01-02-17.
 */
public class ColorOverlay extends LayerUI<JComponent> implements PropertyChangeListener {

    String newColor = "#000000";
    CarParkModel model;

    /**
     * The constructor of this class.
     * @param model We need the model, because we want to now what the time is.
     */
    public ColorOverlay(CarParkModel model) {
        this.model = model;
    }

    /**
     * We are needing this, otherwise we can't implement the PropertyChangeListener class.
     * This class is not abstract & we can't initiate an abstract class.
     * @param e The property change event.
     */
    public void propertyChange(PropertyChangeEvent e) {}

    /**
     * Overriding the paint method
     * We want to our own look and feel to the layer that's used on the main panel.
     * @param g Here goes our graphical component that is our current screen. (I think offset painting)
     * @param c Here goes our component that needs the layer.
     */
    @Override
    public void paint (Graphics g, JComponent c){
        super.paint(g, c);

        newColor = model.getColor();

        Graphics2D g2 = (Graphics2D) g.create();

        int w = c.getWidth();
        int h = c.getHeight();
        g2.setComposite(AlphaComposite.getInstance(
                AlphaComposite.SRC_OVER, .5f));

        Color exitColor = new Color(255,255,255,0);

        g2.setPaint(new GradientPaint(0, 0, Color.decode(newColor), 0, h,exitColor));

        g2.fillRect(0, 0, w, h);

        g2.dispose();
    }
}


