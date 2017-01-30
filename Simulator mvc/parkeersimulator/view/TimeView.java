package parkeersimulator.view;

import parkeersimulator.logic.CarParkModel;

import javax.swing.*;

/**
 * Created by sanderpost on 26-01-17.
 */
public class TimeView extends AbstractView {

    private JPanel time;
    private JLabel dayLabel;
    private JLabel hourLabel;
    private JLabel minuteLabel;

    public TimeView(CarParkModel model) {
        super(model);
        time = new JPanel();
        dayLabel = new JLabel();
        hourLabel = new JLabel();
        minuteLabel = new JLabel();
        this.add(dayLabel);
        this.add(hourLabel);
        this.add(minuteLabel);
    }

    public void updateView() {
        dayLabel.setText(model.getDay() + " :");
        hourLabel.setText(model.getHour() + " :");
        minuteLabel.setText(model.getMinute());
    }

}
