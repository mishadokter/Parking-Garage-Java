package parkeersimulator.view;

import parkeersimulator.logic.CarParkModel;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.util.*;


public class CaptionView extends AbstractView {

    private JPanel captionPanel;
    private LinkedHashMap<String, String> legend;

    public CaptionView(CarParkModel model) {
        super(model);
        // Create a new HashMap.
        legend = new LinkedHashMap<>();
        // Initialize the values of the array.
        fillHashMap();
        // Create the panel.
        createTable();
        // Create labels & attach.
        attachToPanel();
    }

    /**
     * The method to create the actual JPanel with our data on it.
     */
    private void createTable() {
        // Initiating our panel, and set it to visible.
        captionPanel = new JPanel(new GridLayout(0, 1));
        add(captionPanel);
        captionPanel.setVisible(true);
    }

    /**
     * Looping through our map of color entries.
     * For each iteration we make a JLabel & adding items from the map on it.
     * After the label is created & filled with it's values, we attach it to the JPanel
     */
    private void attachToPanel() {
        // Create the labels borders.
        Border paddingBorder = BorderFactory.createEmptyBorder(10, 25, 10, 25);
        Border border = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
        // We want to initiate an entryset & iterator on our hashmap.
        // Because each loop our iterator can pick up the set of that entry.
        Set entrySet = legend.entrySet();
        Iterator it = entrySet.iterator();
        // Do a while loop until we don't have entries left in our map.
        // Attach the key & value in it's own way to a JLabel.
        while (it.hasNext()) {
            Map.Entry me = (Map.Entry) it.next();
            JLabel label = new JLabel(me.getKey().toString(), JLabel.LEFT);
            label.setPreferredSize(new Dimension(200, 40));
            label.setOpaque(true);
            label.setBackground(Color.WHITE);
            label.setBorder(BorderFactory.createCompoundBorder(border, paddingBorder));
            label.setIcon(new ImageIcon(getClass().getResource("resources/cars/" + me.getValue())));
            captionPanel.add(label);
        }
    }

    /**
     * Creating a method to fill our HashMap that's been initiated in te constructor.
     * The key of each entry is a hexadecimal color.
     * The value of each entry represents the color by name.
     * Both of them are strings.
     */
    private void fillHashMap() {
        legend.put("Normal car", "adhoccar.png");
        legend.put("Pass car", "passcar.png");
        legend.put("Bad parked car", "badparkercar.png");
        legend.put("Empty car spot", "empty.png");
        legend.put("Pass car spot", "passplace.png");
        legend.put("Reserved car spot", "rescar.png");
    }
}
