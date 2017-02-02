package parkeersimulator.view;

import parkeersimulator.logic.CarParkModel;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.util.*;

/**
 * Created by sanderpost on 31-01-17.
 * This class makes our, in Dutch; "Legenda".
 */
public class CaptionView extends AbstractView {

    private JPanel captionPanel;
    private HashMap<String, String> colors;

    public CaptionView(CarParkModel model) {
        super(model);
        // Create a new HashMap.
        colors = new HashMap<String, String>();
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
        captionPanel = new JPanel(new GridLayout(0,1));
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
        Border paddingBorder = BorderFactory.createEmptyBorder(10,25,10,25);
        Border border = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
        // We want to initiate an entryset & iterator on our hashmap.
        // Because each loop our iterator can pick up the set of that entry.
        Set entrySet = colors.entrySet();
        Iterator it = entrySet.iterator();
        // Do a while loop until we don't have entries left in our map.
        // Attach the key & value in it's own way to a JLabel.
        while (it.hasNext()) {
            Map.Entry me = (Map.Entry)it.next();
            JLabel label = new JLabel(me.getValue().toString(), JLabel.LEFT);
            label.setPreferredSize(new Dimension(200, 40));
            label.setOpaque(true);
            label.setBackground(Color.decode(me.getKey().toString()));
            label.setBorder(BorderFactory.createCompoundBorder(border, paddingBorder));
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
        colors.put("#a9a9a9", "Dark Gray");
        colors.put("#ffffff", "White");
        colors.put("#ff00000", "Red");
        colors.put("#0000ff", "Blue");
        colors.put("#008000", "Green");
    }
}
