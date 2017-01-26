package parkeersimulator.controller;

import parkeersimulator.logic.CarParkModel;

import javax.swing.*;

public abstract class AbstractController extends JPanel {

    protected CarParkModel model;

    public AbstractController(CarParkModel model) {
        this.model = model;
    }


}
