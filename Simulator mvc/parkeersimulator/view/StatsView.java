package parkeersimulator.view;

import parkeersimulator.logic.CarParkModel;

import javax.swing.*;

/**
 * Created by Alan on 1/26/2017.
 */
public class StatsView  extends AbstractView {

    private JPanel carStats;
    private JLabel entranceCars;
    private JLabel entrancePasses;
    private JLabel paymentCars;
    private JLabel exitCars;

    public StatsView(CarParkModel model){
        super(model);
        carStats = new JPanel();
        entranceCars = new JLabel();
        entrancePasses = new JLabel();
        paymentCars = new JLabel();
        exitCars = new JLabel();
        this.add(entranceCars);
        this.add(entrancePasses);
        this.add(paymentCars);
        this.add(exitCars);
    }

    public void updateStats(int enterCars, int enterPasses, int payCars, int leaveCars){
        entranceCars.setText(""+enterCars);
        paymentCars.setText(""+payCars);
        entrancePasses.setText(""+enterPasses);
        exitCars.setText(""+leaveCars);
    }


}
