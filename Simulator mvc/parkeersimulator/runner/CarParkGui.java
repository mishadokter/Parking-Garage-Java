package parkeersimulator.runner;

import parkeersimulator.main.CarParkSim;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JButton;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class CarParkGui extends JFrame {
    private TreeMap<String, String> defaultValues;
    private TreeMap<String, JTextField> optionFields;
    private JPanel tabArrivals,tabOptions,tabCount;

    public CarParkGui() {
        defaultValues = new TreeMap<>();
        optionFields = new TreeMap<>();
        //Panelen aanmaken voor tab content//
        tabOptions = new JPanel(new GridLayout(0,4));
        tabArrivals = new JPanel(new GridLayout(0,4));
        tabCount = new JPanel(new GridLayout(0,4));

        //Tabjes paneel aanmaken//
        JTabbedPane jtp = new JTabbedPane();

        //JFrame settings//
        this.getContentPane().add(jtp);
        this.setTitle("Parking garage GUI");
        this.setSize(550, 300); //set size so the user can "see" it

        //start button en listener//
        JButton startSim = new JButton("Start simulatie");
        startSim.setLocation(0,0);
        startSim.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                submitOptions();
            }
        } );
        getContentPane().add(startSim, BorderLayout.SOUTH);

        setDefaults();

        //JPanel tabs toevoegen aan tab panel//
        jtp.addTab("Options", tabOptions);
        jtp.addTab("Arrivals", tabArrivals);

        setVisible(true); //otherwise you won't "see" it
    }
    public void setDefaults(){
        defaultValues.put("numberOfFloors",      "3");
        defaultValues.put("numberOfRows",      "6");
        defaultValues.put("numberOfPlaces",      "30");
        defaultValues.put("weekDayArrivals",      "100");
        defaultValues.put("weekendArrivals",      "200");
        defaultValues.put("weekDayBadArrivals",   "18");
        defaultValues.put("weekendBadArrivals",   "35");
        defaultValues.put("weekDayPassArrivals",  "50");
        defaultValues.put("weekendPassArrivals",  "5");
        defaultValues.put("wumberOfPasses",       "68");
        defaultValues.put("enterSpeed",           "1");
        defaultValues.put("paymentSpeed",         "7");
        defaultValues.put("exitSpeed",            "5");
        defaultValues.put("tickPause",            "10");
        defaultValues.put("steps",                "0");
        defaultValues.put("day",                  "0");
        defaultValues.put("hour",                 "0");
        defaultValues.put("minute",               "0");
        Set set = defaultValues.entrySet();
        Iterator i = set.iterator();
        int count = 0;
        while(i.hasNext()) {
            JPanel selected = tabOptions;
            Map.Entry me = (Map.Entry)i.next();
            if (count > 10){
                selected = tabArrivals;
            }
            selected.add(new JLabel(me.getKey().toString()));
            JTextField textField = new JTextField(me.getValue().toString());
            optionFields.put(me.getKey().toString(), textField);
            selected.add(textField);
            count++;
        }
    }
    public void submitOptions(){
        Set set = optionFields.entrySet();
        Iterator i = set.iterator();
        while(i.hasNext()) {
            Map.Entry me = (Map.Entry)i.next();

            JTextField temp = (JTextField)me.getValue();
            System.out.println(me.getKey().toString() + " | " + temp.getText());
            defaultValues.put(me.getKey().toString(), temp.getText());
        }
        new CarParkSim(defaultValues);
    }

    public void createOptions(JPanel arrivals) {


    }
    public void createCount(JPanel arrivals) {

    }

    public static void main(String[] args) {
        CarParkGui tab = new CarParkGui();
    }

}
