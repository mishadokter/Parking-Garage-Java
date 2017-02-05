package parkeersimulator.controller;

import parkeersimulator.logic.*;
import parkeersimulator.main.CarParkSim;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JButton;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class CarParkGui extends AbstractController {
    private TreeMap<String, JTextField> optionFields;
    private JPanel tabArrivals,tabOptions,tabCount;
    private JFrame guiFrame;

    public CarParkGui(CarParkModel model, boolean running) {
        super(model);
        optionFields = new TreeMap<>();
        guiFrame = new JFrame();
        //Panelen aanmaken voor tab content//
        tabOptions = new JPanel(new GridLayout(0,3));
        tabArrivals = new JPanel(new GridLayout(0,3));
        tabCount = new JPanel(new GridLayout(0,3));
        //Tabjes paneel aanmaken//
        JTabbedPane jtp = new JTabbedPane();
        //JFrame settings//
        guiFrame.setResizable(false);
        guiFrame.getContentPane().add(jtp);
        guiFrame.setTitle("Parking garage GUI");
        guiFrame.setSize(550, 300); //set size so the user can "see" it

        //start button en listener//
        JButton startSim = new JButton("Start simulatie");
        startSim.setLocation(0,0);
        startSim.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                guiFrame.dispose();
                new CarParkSim(genInt("numberOfFloors"), genInt("numberOfRows"), genInt("numberOfPlaces"), genInt("numberOfPasses"));
            }
        } );
        setDefaults();
        //JPanel tabs toevoegen aan tab panel//
        if(running == false){
            jtp.addTab("Model", tabCount);
            guiFrame.getContentPane().add(startSim, BorderLayout.SOUTH);
            guiFrame.setVisible(true);
        }else{
            jtp.addTab("Options", tabOptions);
            jtp.addTab("Arrivals", tabArrivals);
        }
    }

    public void setDefaults(){
        Set set = model.getOptions().entrySet();
        Iterator i = set.iterator();
        while(i.hasNext()) {
            JPanel selected = tabOptions;
            Map.Entry me = (Map.Entry)i.next();
            int val = Integer.parseInt(me.getValue().toString());
            int range = Integer.parseInt(me.getValue().toString()) * 10;
            if(val == 0){
                range = 20;
            }
            if (me.getKey().toString().contains("Arrivals")){
                selected = tabArrivals;
            }else if(me.getKey().toString().contains("number")){
                selected = tabCount;
                range = val;
            }



            JTextField textField = new JTextField(me.getValue().toString());
            JSlider slider = new JSlider(JSlider.HORIZONTAL, 0, range, val);
            slider.setLabelTable(slider.createStandardLabels(10));
            slider.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    textField.setText(String.valueOf(slider.getValue()));
                    submitOptions();
                }
            });
            selected.add(new JLabel(me.getKey().toString()));

            optionFields.put(me.getKey().toString(), textField);
            selected.add(textField);
            selected.add(slider);
        }
    }


    public void submitOptions(){
        Set set = optionFields.entrySet();
        Iterator i = set.iterator();
        while(i.hasNext()) {
            Map.Entry me = (Map.Entry)i.next();
            JTextField temp = (JTextField)me.getValue();
            //System.out.println(me.getKey().toString() + " | " + temp.getText());
            model.setOption(me.getKey().toString(), Integer.parseInt(temp.getText()));
        }
        model.setReferences();
    }

    public void openGui(){
        guiFrame.setVisible(true);
    }

    public int genInt(String val){
        JTextField temp = optionFields.get(val);
        return Integer.parseInt(temp.getText());
    }
}
