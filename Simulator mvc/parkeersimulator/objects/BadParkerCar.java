package parkeersimulator.objects;

import java.util.Random;

public class BadParkerCar extends Car {
    private Location loc2;
    private int state2;

    /**
     * Creates a "bad parked" car.
     */
    public BadParkerCar() {
        Random random = new Random();
        int stayMinutes = 30 + random.nextInt(30) * 6;
        this.setMinutesLeft(stayMinutes);
        this.setHasToPay(true);
        this.setState(6);
        state2 = 7;
    }

    /**
     * Gets the second location of the car.
     * @return Returns the second location of a bad parked car.
     */
    public Location getSecondLocation() {
        return loc2;
    }

    /**
     * Sets the second location of a bad parked car.
     * @param location The second location of a bad parked car.
     */
    public void setSecondLocation(Location location) {
        this.loc2 = location;
    }

    /**
     * Gets the second location state of a bad parked car.
     * @return The second location state of a bad parked car.
     */
    public int getState2() {
        return state2;
    }
}
