package parkeersimulator.controller;

import parkeersimulator.logic.CarParkModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class RunController extends AbstractController implements ActionListener {

    private JButton stepOne;
    private JButton stepHundred;
    private JButton startSteps;
    private JButton stopSteps;
    private ArrayList<JButton> buttons;

    public RunController(CarParkModel model) {
        super(model);
        setSize(800, 500);
        stepOne = new JButton("Step one");
        stepOne.addActionListener(this);
        stepHundred = new JButton("Step hundred");
        stepHundred.addActionListener(this);
        startSteps = new JButton("Start");
        startSteps.addActionListener(this);
        stopSteps = new JButton("Stop");
        stopSteps.addActionListener(this);

        buttons = new ArrayList<>();
        buttons.add(stepOne);
        buttons.add(stepHundred);
        buttons.add(startSteps);

        this.setLayout(null);
        add(stepOne);
        add(stepHundred);
        add(startSteps);
        add(stopSteps);
        stepOne.setBounds(50, 10, 70, 30);
        stepHundred.setBounds(140, 10, 70, 30);
        startSteps.setBounds(230, 10, 70, 30);
        stopSteps.setBounds(320, 10, 70, 30);

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
        if (e.getSource() == stepOne) {
            disableButtons();
            try {
                model.start(1);
            } catch (Exception ex) {
                ex.printStackTrace();
                enableButtons();
            }
            return;
        }

        if (e.getSource() == stepHundred) {
            disableButtons();
            try {
                model.start(100);
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

        if (e.getSource() == stopSteps) {
            model.stopSteps();
            enableButtons();
        }

    }
}
