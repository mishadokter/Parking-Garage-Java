package parkeersimulator.controller;

import parkeersimulator.logic.CarParkModel;
import parkeersimulator.view.ManagementView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;


public class RunController extends AbstractController implements ActionListener {

    private JButton stepDay;
    private JButton stepWeek;
    private JButton startSteps;
    private JButton stopSteps;
    private JButton settings;
    private ArrayList<JButton> buttons;

    public RunController(CarParkModel model) {
        super(model);
        //setSize(400, 500);
        stepDay = new JButton("Day");
        stepDay.addActionListener(this);
        stepWeek = new JButton("Week");
        stepWeek.addActionListener(this);
        startSteps = new JButton("Start");
        startSteps.addActionListener(this);
        stopSteps = new JButton("Stop");
        stopSteps.addActionListener(this);
        settings = new JButton("Settings");
        settings.addActionListener(this);


        buttons = new ArrayList<>();
        buttons.add(stepDay);
        buttons.add(stepWeek);
        buttons.add(startSteps);

        this.setLayout(null);
        add(stepDay);
        add(stepWeek);
        add(startSteps);
        add(stopSteps);
        add(settings);
        stepDay.setBounds(50, 10, 70, 30);
        stepWeek.setBounds(140, 10, 70, 30);
        startSteps.setBounds(230, 10, 70, 30);
        stopSteps.setBounds(320, 10, 70, 30);
        settings.setBounds(410, 10, 140, 30);

        setVisible(true);

    }

    private void disableButtons() {
        for (JButton button : buttons) {
            button.setEnabled(false);
        }
    }

    private void enableButtons() {
        for (JButton button : buttons) {
            button.setEnabled(true);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == stepDay) {
            try {
                model.start(1440);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return;
        }

        if (e.getSource() == stepWeek) {
            disableButtons();
            try {
                model.start(10080);
            } catch (Exception ex) {
                ex.printStackTrace();
                enableButtons();
            }
            return;
        }

        if (e.getSource() == startSteps) {
            disableButtons();
            try {
                model.start(5000);
            } catch (Exception ex) {
                ex.printStackTrace();
                enableButtons();
            }
            return;
        }

        if (e.getSource() == settings) {
            disableButtons();
            try {

                ManagementView mv = new ManagementView();
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        mv.run();
                    }
                });
            } catch (Exception ex) {
                System.out.println("Settings file not found");
            }
            return;
        }

        if (e.getSource() == stopSteps) {
            model.stopSteps();
            enableButtons();
        }

    }
}
