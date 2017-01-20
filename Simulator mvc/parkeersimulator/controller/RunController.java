package parkeersimulator.controller;

import parkeersimulator.logic.Model;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RunController extends AbstractController implements ActionListener {

    private JButton stepOne;
    private JButton stepHundred;

    public RunController(Model model) {
        super(model);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
