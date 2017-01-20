package parkeersimulator.objects;

import java.awt.*;
import java.util.Random;

public class ResCar extends Car {
    private static final Color COLOR = Color.green;

    public ResCar() {
        int stayMinutes = 9999;
        this.setMinutesLeft(stayMinutes);
        this.setHasToPay(false);
    }

    public Color getColor() {
        return COLOR;
    }
}
