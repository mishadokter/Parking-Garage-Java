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
        int curHour = model.getHour();
        Color newColor;
        Color exitColor = new Color(0,0,0,0);
        System.out.println(curHour);

        if(curHour > 6 && curHour < 18) {
            newColor = new Color(0, 0, 0, 0);
        }else if(curHour > 17 && curHour < 24){
            newColor = new Color(0, 0, 0, 1 + (curHour*10));
        }else if(curHour > 0 && curHour < 7){
            newColor = new Color(0, 0, 0, 255 - (curHour*10));
        }else{
            newColor = new Color(0, 0, 0, 255);
        }


        //System.out.println(newColor);
        Graphics2D g2 = (Graphics2D) g.create();

        int w = c.getWidth();
        int h = c.getHeight() / 5;
        g2.setComposite(AlphaComposite.getInstance(
                AlphaComposite.SRC_OVER, .75f));


        g2.setPaint(new GradientPaint(0, 0, newColor, 0, h,exitColor));

        g2.fillRect(0, 0, w, h);

        g2.dispose();
    }
}


