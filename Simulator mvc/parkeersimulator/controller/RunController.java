package parkeersimulator.controller;

import org.jfree.ui.RefineryUtilities;
import parkeersimulator.logic.CarParkModel;
import parkeersimulator.main.CarParkSim;
import parkeersimulator.view.PieChart_AWT;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;


public class RunController extends AbstractController implements ActionListener {

    private JButton stepDay, stepWeek, startSteps, stopSteps, settings, guiButton, resetButton;
    private JFrame simFrame;
    private CarParkGui gui;
    private ArrayList<JButton> buttons;
    private PieChart_AWT chart;

    public RunController(CarParkModel model, JFrame simFrame) {
        super(model);
        this.simFrame = simFrame;
        gui = new CarParkGui(model, true);
        stepDay = new JButton("Day");
        stepWeek = new JButton("Week");
        startSteps = new JButton("Start");
        stopSteps = new JButton("Stop");
        settings = new JButton("Management");
        guiButton = new JButton("Settings");
        resetButton = new JButton("Reset");

        buttons = new ArrayList<>();
        buttons.add(stepDay);
        buttons.add(stepWeek);
        buttons.add(startSteps);

        // Loop om dezelfde style mee te geven aan alle buttons.
        int spacing = 1;
        for (JButton b : Arrays.asList(stepDay, stepWeek, startSteps, stopSteps, settings, guiButton, resetButton)) {
            b.setBackground(new Color(59, 89, 182));
            b.setForeground(Color.WHITE);
            b.setFocusPainted(false);
            b.setFont(new Font("Tahoma", Font.BOLD, 10));
            b.addActionListener(this);
            add(b);
        }
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

        if (e.getSource() == resetButton) {
            model.stopSteps();
            simFrame.dispose();
            new CarParkSim(true);
        }

        if (e.getSource() == guiButton) {
            gui.openGui();
        }

        if (e.getSource() == settings) {
            if (chart != null) {
                chart.dispose();
            }
            chart = new PieChart_AWT("Total cars", model);
            chart.setSize(560, 367);
            RefineryUtilities.centerFrameOnScreen(chart);
            chart.setVisible(true);
        }

        if (e.getSource() == stopSteps) {
            model.stopSteps();
            enableButtons();
        }

    }
}

