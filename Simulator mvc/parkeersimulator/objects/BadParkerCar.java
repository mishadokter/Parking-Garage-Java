package parkeersimulator.objects;

import java.util.Random;

public class BadParkerCar extends Car {
    private Location loc2;
    private int state2;

    public BadParkerCar() {
        Random random = new Random();
        int stayMinutes = 30 + random.nextInt(30) * 6;
        this.setMinutesLeft(stayMinutes);
        this.setHasToPay(true);
        this.setState(6);
        setState2();
    }

    /**
     * @return returns location object
     */
    public Location getSecondLocation() {
        return loc2;
    }

    /**
     * @param location 2nd location object of the car
     */
    public void setSecondLocation(Location location) {
        this.loc2 = location;
    }

    public void setState2() {
        state2 = 7;
    }

    public int getState2() {
        return state2;
    }
}
