package parkeersimulator.objects;

import java.util.Random;

public class ResCar extends Car {

    public ResCar() {
        Random random = new Random();
        int stayMinutes = 30 + random.nextInt(30) * 6;
        this.setMinutesLeft(stayMinutes);
        this.setHasToPay(false);
        this.setState(8);
    }
}
