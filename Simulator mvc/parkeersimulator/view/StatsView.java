package parkeersimulator.view;

import parkeersimulator.logic.CarParkModel;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by Alan on 1/26/2017.
 */
public class StatsView extends AbstractView {

    private JPanel carStats;
    private Thread thread;
    private Map<String, JLabel> statDisplay;
    private boolean runView = true;

    public StatsView(CarParkModel model) {
        super(model);
        statDisplay = new HashMap<>();
        Border paddingBorder = BorderFactory.createEmptyBorder(10, 25, 10, 25);
        Border border = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
        carStats = new JPanel(new GridLayout(0, 1));
        carStats.setVisible(true);
        add(carStats);
        setVisible(true);
        thread = new Thread() {
            public void run() {
                try {
                    while (runView) {
                        Set set = model.getStats().entrySet();
                        Iterator i = set.iterator();
                        while (i.hasNext()) {
                            Map.Entry me = (Map.Entry) i.next();
                            String keyName = me.getKey().toString();
                            String val = me.getValue().toString();
                            JLabel temp = new JLabel(keyName + ": " + val, JLabel.LEFT);
                            temp.setPreferredSize(new Dimension(200, 40));
                            temp.setOpaque(true);
                            temp.setBackground(Color.WHITE);
                            temp.setIcon(new ImageIcon(getClass().getResource("resources/" + keyName + ".png")));
                            temp.setBorder(BorderFactory.createCompoundBorder(border, paddingBorder));
                            if (statDisplay.get(keyName) == null) {
                                statDisplay.put(keyName, temp);
                                carStats.add(temp, BorderLayout.EAST);
                            } else {
                                statDisplay.get(keyName).setText(keyName + ": " + val);
                            }
                        }
                        Thread.sleep(100);
                    }
                } catch (InterruptedException ex) {
                    //SomeFishCatching
                }
            }
        };
        thread.start();
    }
}
