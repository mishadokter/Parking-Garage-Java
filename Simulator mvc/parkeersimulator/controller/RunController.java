package parkeersimulator.controller;

import parkeersimulator.exception.CarParkException;
import parkeersimulator.logic.Model;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RunController extends AbstractController implements ActionListener {

    private JButton stepOne;
    private JButton stepHundred;
    private JButton startSteps;
    private JButton stopSteps;

    public RunController(Model model) {
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

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == stepOne) {
            try {
                model.start(1);
            } catch (CarParkException ex) {
                ex.printStackTrace();
            }
            return;
        }

        if (e.getSource() == stepHundred) {
            try {
                model.start(100);
            } catch (CarParkException ex) {
                ex.printStackTrace();
            }
            return;
        }

        if (e.getSource() == startSteps) {
            try {
                model.start(5000);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return;
        }

        if (e.getSource() == stopSteps) {
            model.stopSteps();
        }

    }
}
}
