package parkeersimulator.objects;

import java.util.Random;

public class BadParkerCar extends Car {
    private Location loc2;

    public BadParkerCar() {
        Random random = new Random();
        int stayMinutes = (int) (15 + random.nextFloat() * 3 * 60);
        this.setMinutesLeft(stayMinutes);
        this.setHasToPay(true);
        this.setState(6);
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
}
