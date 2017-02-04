package parkeersimulator.objects;


public abstract class Car {

    private Location location;
    private int minutesLeft;
    private int totalMinutes;
    private boolean isPaying;
    private boolean hasToPay;
    private int state;


    /**
     * Gets the location of a car.
     * @return The location of a car.
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Sets the location of a car.
     * @param location The location a car needs to set.
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * Gets the amount of minutes a car stays at the carpark.
     * @return The amount of minutes a car stays at the carpark.
     */
    public int getMinutesLeft() {
        return minutesLeft;
    }

    /**
     * Sets the amount of minutes a car stays at the carpark.
     * @param minutesLeft The amount of minutes a car stays at the carpark.
     */
    public void setMinutesLeft(int minutesLeft) {
        this.minutesLeft = minutesLeft;
    }


    public int getTotalMinutes() {
        return totalMinutes;
    }

    /**
     * Gets wheter a car is paying.
     * @return If a car is paying.
     */
    public boolean getIsPaying() {
        return isPaying;
    }

    /**
     * Sets if a car is paying.
     * @param isPaying If a car is paying.
     */
    public void setIsPaying(boolean isPaying) {
        this.isPaying = isPaying;
    }

    /**
     * Gets if a car has to pay.
     * @return If a car has to pay.
     */
    public boolean getHasToPay() {
        return hasToPay;
    }

    /**
     * Sets if a car has to pay.
     * @param hasToPay If a car has to pay.
     */
    public void setHasToPay(boolean hasToPay) {
        this.hasToPay = hasToPay;
    }

    /**
     * Decrease the amount of minutes a car is staying.
     */
    public void tick() {
        minutesLeft--;
    }

    /**
     * Gets the state of a car.
     * @return The state of a car.
     */
    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
        System.out.println("# State is: " + state);
    }
}