package parkeersimulator.objects;

import java.util.Random;

public class ParkingPassCar extends Car {

    public ParkingPassCar() {
        Random random = new Random();
        int stayMinutes = (int) (15 + random.nextFloat() * 3 * 60);
        this.setMinutesLeft(stayMinutes);
        this.setHasToPay(false);
        this.setState(2);
    }
}
