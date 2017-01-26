package parkeersimulator.objects;

import java.util.Random;

public class AdHocCar extends Car {

    public AdHocCar() {
        Random random = new Random();
        int stayMinutes = (int) (15 + random.nextFloat() * 3 * 60);
        this.setMinutesLeft(stayMinutes);
        this.setHasToPay(true);
        this.setState(1);
    }
}
