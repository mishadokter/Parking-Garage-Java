package parkeersimulator.objects;

import java.util.Random;

public class AdHocCar extends Car {

    public AdHocCar() {
        Random random = new Random();
        int stayMinutes = 30 + random.nextInt(30) * 6;
        this.setMinutesLeft(stayMinutes);
        this.setTotalMinutes();
        this.setHasToPay(true);
        this.setState(1);
    }
}
