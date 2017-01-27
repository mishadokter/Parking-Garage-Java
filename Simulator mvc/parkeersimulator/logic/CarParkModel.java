package parkeersimulator.logic;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import parkeersimulator.main.CarParkSim;
import parkeersimulator.objects.*;

/**
 * The main model initializing.
 */
public class CarParkModel extends AbstractModel implements Runnable {

    private static final String AD_HOC = "1";
    private static final String PASS = "2";
    private static final String BAD = "3";
    int weekDayArrivals = 100; // average number of arriving cars per hour
    int weekendArrivals = 200; // average number of arriving cars per hour
    int weekDayBadArrivals = 18;
    int weekendBadArrivals = 35;
    int weekDayPassArrivals = 50; // average number of arriving cars per hour
    int weekendPassArrivals = 5; // average number of arriving cars per hour
    int numberOfPasses = 68;
    int enterSpeed = 1; // number of cars that can enter per minute
    int paymentSpeed = 7; // number of cars that can pay per minute
    int exitSpeed = 5; // number of cars that can leave per minute
    private CarQueue entranceCarQueue;
    private CarQueue entrancePassQueue;
    private CarQueue paymentCarQueue;
    private CarQueue exitCarQueue;
    private Garage garage;
    private int day = 0;
    private int hour = 0;
    private int minute = 0;
    private int tickPause = 10;
    private int steps = 0;

    private ArrayList<Location> spots;
    private ArrayList<Location> passHolders;
    private Map<String, String> optionFields;
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
        getAllSpots();
        garage = new Garage(this);
        setPassSpot();
    }

    public CarParkModel(TreeMap<String,String> optionFields) {
        this.optionFields = optionFields;
        weekDayArrivals =  setOption("weekDayArrivals"); // average number of arriving cars per hour
        weekendArrivals = setOption("weekendArrivals"); // average number of arriving cars per hour
        weekDayBadArrivals = setOption("weekDayBadArrivals");
        weekendBadArrivals = setOption("weekendBadArrivals");
        weekDayPassArrivals = setOption("weekDayPassArrivals"); // average number of arriving cars per hour
        weekendPassArrivals = setOption("weekendPassArrivals"); // average number of arriving cars per hour
        numberOfPasses = setOption("wumberOfPasses");
        enterSpeed = setOption("enterSpeed"); // number of cars that can enter per minute
        paymentSpeed = setOption("paymentSpeed"); // number of cars that can pay per minute
        exitSpeed = setOption("exitSpeed"); // number of cars that can leave per minute
        day = setOption("day");
        hour = setOption("hour");
        minute = setOption("minute");
        tickPause = setOption("tickPause");
        steps = setOption("steps");
        entranceCarQueue = new CarQueue();
        entrancePassQueue = new CarQueue();
        paymentCarQueue = new CarQueue();
        exitCarQueue = new CarQueue();
        setNumberOfOpenSpots();
        this.numberOfFloors = setOption("numberOfFloors");
        this.numberOfRows = setOption("numberOfRows");
        this.numberOfPlaces = setOption("numberOfPlaces");
        run = false;
        this.numberOfOpenSpots = numberOfFloors * numberOfRows * numberOfPlaces;
        cars = new Car[numberOfFloors][numberOfRows][numberOfPlaces];
        spots = new ArrayList<>();
        passHolders = new ArrayList<>();
        getAllSpots();
        garage = new Garage(this);
        setPassSpot();
    }


// Return de dag
    public String getDay() {
        String dayName = null;
        switch (day) {
            case 0:
                dayName = "maandag";
                break;
            case 1:
                dayName = "dinsdag";
                break;
            case 2:
                dayName = "woensdag";
                break;
            case 3:
                dayName = "donderdag";
                break;
            case 4:
                dayName = "vrijdag";
                break;
            case 5:
                dayName = "zaterdag";
                break;
            case 6:
                dayName = "zondag";
                break;
        }
        return dayName;
    }

    public String getHour() {
        String hourString = Integer.toString(hour);
        if (hour < 10) {
            return hourString = "0" + hourString;
        } else {
            return hourString;
        }
    }

    public String getMinute() {
        String minuteString = Integer.toString(minute);
        if (minute < 10) {
            return minuteString = "0" + minuteString;
        } else {
            return minuteString;
        }
    }

    public int getLocInfo(Location location) {
        return garage.getStateAt(location);
    }

    /**
     * Gets all available spots and put them into a list.
     */
    public void getAllSpots() {
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
            garage.setStateAt(spots.get(i), 5);
            passHolders.add(spots.get(i));
        }
    }

    public int setOption(String varName){
        return Integer.parseInt(optionFields.get(varName).toString());
    }

    public boolean getPassSpot(int floor, int row, int place) {
        for (Location loc : passHolders) {
            if (loc.getFloor() == floor && loc.getRow() == row && loc.getPlace() == place) {
                return true;
            }
        }
        return false;
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
        numberOfCars = getNumberOfCars(weekDayBadArrivals, weekendBadArrivals);
        addArrivingCars(numberOfCars, BAD);
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
            case BAD:
                for (int i = 0; i < numberOfCars; i++) {
                    entranceCarQueue.addCar(new BadParkerCar());
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
    /*The state of the location mean:
    0 - empty place
    1 - taken place
    2 - place taken by pass holder
    5 - empty place for pass holders
    6 - taken by a bad parker*/
    public boolean setCarAt(Location location, Car car) {
        if (!locationIsValid(location)) {
            return false;
        }
        Car oldCar = getCarAt(location);
        if (car instanceof ParkingPassCar) {
            if (garage.getStateAt(location) == 5 || garage.getStateAt(location) == 0) {
                cars[location.getFloor()][location.getRow()][location.getPlace()] = car;
                car.setLocation(location);
                garage.setCarAt(location, car, car.getState());
                return true;
            }
        }
        if (oldCar == null || garage.getStateAt(location) == 0) {
            if (car instanceof BadParkerCar) {
                cars[location.getFloor()][location.getRow()][location.getPlace()] = car;
                car.setLocation(location);
                garage.setCarAt(location, car, car.getState());
                garage.setCarAt(((BadParkerCar) car).getSecondLocation(), car, car.getState());
                numberOfOpenSpots -= 2;
                return true;
            }
            cars[location.getFloor()][location.getRow()][location.getPlace()] = car;
            car.setLocation(location);
            garage.setCarAt(location, car, car.getState());
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
            garage.setStateAt(location, 5);
            numberOfOpenSpots++;
            return car;
        }
        if (car instanceof BadParkerCar) {
            Location loc2 = ((BadParkerCar) car).getSecondLocation();
            cars[location.getFloor()][location.getRow()][location.getPlace()] = null;
            car.setLocation(null);
            garage.setStateAt(location, 0);
            garage.setStateAt(loc2, 0);
            numberOfOpenSpots += 2;
            return car;
        }
        cars[location.getFloor()][location.getRow()][location.getPlace()] = null;
        car.setLocation(null);
        garage.setStateAt(location, 0);
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
                        if (garage.getStateAt(location) == 5 || garage.getStateAt(location) == 0) {
                            return location;
                        }
                    } else if (car instanceof BadParkerCar) {
                        Location location2 = new Location(floor, row, place - 1);
                        Location location3 = new Location(floor, row, place + 1);

                        if (garage.getStateAt(location) == 0) {
                            if (locationIsValid(location2)) {
                                if (garage.getStateAt(location2) == 0) {
                                    ((BadParkerCar) car).setSecondLocation(location2);
                                    return location;
                                }
                            } else if (locationIsValid(location3)) {
                                if (garage.getStateAt(location3) == 0) {
                                    ((BadParkerCar) car).setSecondLocation(location3);
                                    return location;
                                }
                            } else {
                                return null;
                            }
                        }

                    } else if (garage.getStateAt(location) == 0) {
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
        if (location == null) {
            return false;
        }
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

    public String getQueue() {
        return "[GET TESTER] entranceCarQueue: "+ entranceCarQueue.carsInQueue() +
                " entrancePassQueue: " +entrancePassQueue.carsInQueue() +
                " paymentCarQueue: " + paymentCarQueue.carsInQueue() +
                " exitCarQueue: " +exitCarQueue.carsInQueue();
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
                Thread.sleep(tickPause);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        run = false;
        numOfSteps = 0;
    }
}
