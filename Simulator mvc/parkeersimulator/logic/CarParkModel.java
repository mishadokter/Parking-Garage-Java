package parkeersimulator.logic;

import java.util.ArrayList;
import java.util.Random;

import parkeersimulator.objects.*;

/**
 * The main model initializing.
 */
public class CarParkModel extends AbstractModel implements Runnable {

    private static final String AD_HOC = "1";
    private static final String PASS = "2";
    int weekDayArrivals = 100; // average number of arriving cars per hour
    int weekendArrivals = 200; // average number of arriving cars per hour
    int weekDayPassArrivals = 50; // average number of arriving cars per hour
    int weekendPassArrivals = 5; // average number of arriving cars per hour
    int numberOfPasses = 68;
    int enterSpeed = 3; // number of cars that can enter per minute
    int paymentSpeed = 7; // number of cars that can pay per minute
    int exitSpeed = 5; // number of cars that can leave per minute
    private CarQueue entranceCarQueue;
    private CarQueue entrancePassQueue;
    private CarQueue paymentCarQueue;
    private CarQueue exitCarQueue;
    private int day = 0;
    private int hour = 0;
    private int minute = 0;
    private int tickPause = 20;
    private int steps = 0;

    private ArrayList<Location> spots;
    private ArrayList<Location> passHolders;

    private int numOfSteps;
    private boolean run;

    // Fields from SimulatorView
    private int numberOfFloors;
    private int numberOfRows;
    private int numberOfPlaces;
    private int numberOfOpenSpots;
    private Car[][][] cars;

    /**
     * Creates the car park.
     *
     * @param numberOfFloors The number of floors.
     * @param numberOfRows   The number of rows on each floor.
     * @param numberOfPlaces The number of places on each row.
     */
    public CarParkModel(int numberOfFloors, int numberOfRows, int numberOfPlaces) {
        entranceCarQueue = new CarQueue();
        entrancePassQueue = new CarQueue();
        paymentCarQueue = new CarQueue();
        exitCarQueue = new CarQueue();
        setNumberOfOpenSpots();
        this.numberOfFloors = numberOfFloors;
        this.numberOfRows = numberOfRows;
        this.numberOfPlaces = numberOfPlaces;
        run = false;
        this.numberOfOpenSpots = numberOfFloors * numberOfRows * numberOfPlaces;
        cars = new Car[numberOfFloors][numberOfRows][numberOfPlaces];
        spots = new ArrayList<>();
        passHolders = new ArrayList<>();
        assignSpots();
        setPassSpot();
    }

    /**
     * This put all available places into a list.
     */
    public void assignSpots() {
        for (int i = 0; i < numberOfFloors; i++) {
            for (int j = 0; j < numberOfRows; j++) {
                for (int k = 0; k < numberOfPlaces; k++) {
                    spots.add(new Location(i, j, k));
                }
            }
        }
    }

    /**
     * This set places specially for pass holders.
     */
    private void setPassSpot() {
        for (int i = 0; i < numberOfPasses; i++) {
            setCarAt(spots.get(i), new ResCar());
            passHolders.add(spots.get(i));
        }
    }

    /**
     * Starts the simulation.
     *
     * @param numberOfSteps The amount of steps the simulation has to run.
     */
    public void start(int numberOfSteps) {
        numOfSteps = numberOfSteps;
        run = true;
        new Thread(this).start();
    }

    /**
     * Stops the simulation.
     */
    public void stopSteps() {
        run = false;
    }

    /**
     * This methods stands for 1 step.
     */
    private void tick() {
        advanceTime();
        handleExit();
        updateViews();
        // Pause.
        try {
            Thread.sleep(tickPause);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        handleEntrance();
        steps++;
    }

    /**
     * Counting like a clock starting from 00:00.
     */
    private void advanceTime() {
        // Advance the time by one minute.
        minute++;
        while (minute > 59) {
            minute -= 60;
            hour++;
        }
        while (hour > 23) {
            hour -= 24;
            day++;
        }
        while (day > 6) {
            day -= 7;
        }

    }

    private void handleEntrance() {
        carsArriving();
        carsEntering(entrancePassQueue);
        carsEntering(entranceCarQueue);
    }

    private void handleExit() {
        carsReadyToLeave();
        carsPaying();
        carsLeaving();
    }

    /**
     * Update all views.
     */
    private void updateViews() {
        carTick();
        notifyViews();
    }

    /**
     * Add arriving cars to their correct queue.
     */
    private void carsArriving() {
        int numberOfCars = getNumberOfCars(weekDayArrivals, weekendArrivals);
        addArrivingCars(numberOfCars, AD_HOC);
        numberOfCars = getNumberOfCars(weekDayPassArrivals, weekendPassArrivals);
        addArrivingCars(numberOfCars, PASS);
    }

    /**
     * Get a car from the queue, remove it from the queue and park at the right spot.
     *
     * @param queue The queue the car is in.
     */
    private void carsEntering(CarQueue queue) {
        int i = 0;
        // Remove car from the front of the queue and assign to a parking space.
        while (queue.carsInQueue() > 0 &&
                getNumberOfOpenSpots() > 0 &&
                i < enterSpeed) {
            Car car = queue.removeCar();
            Location freeLocation = getFirstFreeLocation(car);
            setCarAt(freeLocation, car);
            i++;
        }
    }

    /**
     * Removes the car from the parking spot and check wether he has to pay or not..
     */
    private void carsReadyToLeave() {
        // Add leaving cars to the payment queue.
        Car car = getFirstLeavingCar();
        while (car != null) {
            if (car.getHasToPay()) {
                car.setIsPaying(true);
                paymentCarQueue.addCar(car);
            } else {
                carLeavesSpot(car);
            }
            car = getFirstLeavingCar();
        }
    }

    /**
     * Let the cars pay.
     */
    private void carsPaying() {
        // Let cars pay.
        int i = 0;
        while (paymentCarQueue.carsInQueue() > 0 && i < paymentSpeed) {
            Car car = paymentCarQueue.removeCar();
            // TODO Handle payment.
            carLeavesSpot(car);
            i++;
        }
    }

    /**
     * Remove car from the exit queue.
     */
    private void carsLeaving() {
        // Let cars leave.
        int i = 0;
        while (exitCarQueue.carsInQueue() > 0 && i < exitSpeed) {
            exitCarQueue.removeCar();
            i++;
        }
    }

    /**
     * Gets the number of cars that are entering.
     *
     * @param weekDay The average number of arrivals during the week.
     * @param weekend The average number of arrivals during the weekend.
     * @return The amount of cars entering per step/minute.
     */
    private int getNumberOfCars(int weekDay, int weekend) {
        Random random = new Random();

        // Get the average number of cars that arrive per hour.
        int averageNumberOfCarsPerHour = day < 5
                ? weekDay
                : weekend;

        // Calculate the number of cars that arrive this minute.
        double standardDeviation = averageNumberOfCarsPerHour * 0.3;
        double numberOfCarsPerHour = averageNumberOfCarsPerHour + random.nextGaussian() * standardDeviation;
        return (int) Math.round(numberOfCarsPerHour / 60);
    }

    /**
     * Add the car to the right entrance queue.
     *
     * @param numberOfCars Number of cars entering per step/minute.
     * @param type         The type of the car.
     */
    private void addArrivingCars(int numberOfCars, String type) {
        // Add the cars to the back of the queue.
        switch (type) {
            case AD_HOC:
                for (int i = 0; i < numberOfCars; i++) {
                    entranceCarQueue.addCar(new AdHocCar());
                }
                break;
            case PASS:
                for (int i = 0; i < numberOfCars; i++) {
                    entrancePassQueue.addCar(new ParkingPassCar());
                }
                break;
        }
    }

    /**
     * Removes car from the spot and put them in the exit queue.
     *
     * @param car The car that leaves the spot.
     */
    private void carLeavesSpot(Car car) {
        removeCarAt(car.getLocation());
        exitCarQueue.addCar(car);
    }

    // Methods from SimulatorView

    /**
     * Sets the total number of open spots in the car park.
     *
     * @return The total number of open spots in the car park.
     */
    private int setNumberOfOpenSpots() {
        return numberOfOpenSpots = numberOfFloors * numberOfRows * numberOfPlaces;
    }

    /**
     * Gets the number of floors in the car park.
     *
     * @return The number of floors in the car park.
     */
    public int getNumberOfFloors() {
        return numberOfFloors;
    }

    /**
     * Gets the number of rows in the car park on each floor.
     *
     * @return The number of rows in the car park on each floor.
     */
    public int getNumberOfRows() {
        return numberOfRows;
    }

    /**
     * Gets the number of places in the car park on each row.
     *
     * @return The number of places in the car park on each row.
     */
    public int getNumberOfPlaces() {
        return numberOfPlaces;
    }

    /**
     * Gets the number of open spots in the car park.
     *
     * @return The number of open spots in the car park.
     */
    public int getNumberOfOpenSpots() {
        return numberOfOpenSpots;
    }

    /**
     * Gets a car on a specific location.
     *
     * @param location The location of the car.
     * @return The car on the specific location.
     */
    public Car getCarAt(Location location) {
        if (!locationIsValid(location)) {
            return null;
        }
        return cars[location.getFloor()][location.getRow()][location.getPlace()];
    }

    /**
     * Sets a car at a specific location.
     *
     * @param location The location a car has to park.
     * @param car      The car that has to park.
     * @return If the car has parked successful or not.
     */
    public boolean setCarAt(Location location, Car car) {
        if (!locationIsValid(location)) {
            return false;
        }
        Car oldCar = getCarAt(location);
        if (car instanceof ParkingPassCar) {
            if (oldCar instanceof ResCar) {
                removeCarAt(location);
                cars[location.getFloor()][location.getRow()][location.getPlace()] = car;
                car.setLocation(location);
                return true;
            }
        }
        if (oldCar == null) {
            cars[location.getFloor()][location.getRow()][location.getPlace()] = car;
            car.setLocation(location);
            numberOfOpenSpots--;
            return true;
        }
        return false;
    }

    /**
     * Removes a car from the specific location.
     *
     * @param location The location the car has to be removed.
     * @return The car that has been removed.
     */
    public Car removeCarAt(Location location) {
        if (!locationIsValid(location)) {
            return null;
        }
        Car car = getCarAt(location);
        if (car == null) {
            return null;
        }
        boolean passHolder = false;
        for (Location loc : passHolders) {
            if (loc.equals(location)) {
                passHolder = true;
            }
        }
        if (passHolder) {
            cars[location.getFloor()][location.getRow()][location.getPlace()] = null;
            car.setLocation(null);
            setCarAt(location, new ResCar());
            numberOfOpenSpots++;
            return car;
        }
        cars[location.getFloor()][location.getRow()][location.getPlace()] = null;
        car.setLocation(null);
        numberOfOpenSpots++;
        return car;
    }

    /**
     * Gets the first free location a car can park.
     *
     * @param car The car that wants to park.
     * @return The location the car can park.
     */
    public Location getFirstFreeLocation(Car car) {
        for (int floor = 0; floor < getNumberOfFloors(); floor++) {
            for (int row = 0; row < getNumberOfRows(); row++) {
                for (int place = 0; place < getNumberOfPlaces(); place++) {
                    Location location = new Location(floor, row, place);
                    if (car instanceof ParkingPassCar) {
                        if (getCarAt(location) instanceof ResCar || getCarAt(location) == null) {
                            return location;
                        }
                    } else if (getCarAt(location) == null) {
                        return location;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Gets the car that has to leave.
     *
     * @return The car that has to leave.
     */
    public Car getFirstLeavingCar() {
        for (int floor = 0; floor < getNumberOfFloors(); floor++) {
            for (int row = 0; row < getNumberOfRows(); row++) {
                for (int place = 0; place < getNumberOfPlaces(); place++) {
                    Location location = new Location(floor, row, place);
                    Car car = getCarAt(location);
                    if (car != null && car.getMinutesLeft() <= 0 && !car.getIsPaying()) {
                        return car;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Removes a minute from time the cars wants to park.
     */
    public void carTick() {
        for (int floor = 0; floor < getNumberOfFloors(); floor++) {
            for (int row = 0; row < getNumberOfRows(); row++) {
                for (int place = 0; place < getNumberOfPlaces(); place++) {
                    Location location = new Location(floor, row, place);
                    Car car = getCarAt(location);
                    if (car != null) {
                        car.tick();
                    }
                }
            }
        }
    }

    /**
     * Check if the given location is a valid location in the car park.
     *
     * @param location The location to check.
     * @return If it is valid or not. ( true / false )
     */
    private boolean locationIsValid(Location location) {
        int floor = location.getFloor();
        int row = location.getRow();
        int place = location.getPlace();
        if (floor < 0 || floor >= numberOfFloors || row < 0 || row > numberOfRows || place < 0 || place > numberOfPlaces) {
            return false;
        }
        return true;
    }

    /**
     * Gets the amount of steps that the simulation needs to run.
     *
     * @return The amount of steps that the simulation needs to run.
     */
    public String getSteps() {
        return Integer.toString(steps);
    }

    @Override
    /**
     * Starts the simulation.
     */
    public void run() {
        for (int i = 0; i < numOfSteps && run; i++) {
            tick();
            notifyViews();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        run = false;
        numOfSteps = 0;
    }
}
