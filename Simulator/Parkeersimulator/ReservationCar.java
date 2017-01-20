package Parkeersimulator;

import java.awt.*;
import java.util.Random;

public class ReservationCar extends Car {
    private static final Color COLOR = Color.green;

    public ReservationCar() {
        int stayMinutes = 999;
        this.setMinutesLeft(stayMinutes);
        this.setHasToPay(false);
    }

    @Override
    public Color getColor() {
        return COLOR;
    }
}
