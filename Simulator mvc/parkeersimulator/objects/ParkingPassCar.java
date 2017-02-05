package parkeersimulator.objects;

import java.util.Random;

public class ParkingPassCar extends Car {

    /**
     * Creates a "pass" car.
     */
    public ParkingPassCar() {
        Random random = new Random();
        int stayMinutes = 30 + random.nextInt(30) * 6;
        this.setMinutesLeft(stayMinutes);
        this.setHasToPay(false);
        this.setState(2);
        this.setTotalMinutes(stayMinutes);
    }
}
