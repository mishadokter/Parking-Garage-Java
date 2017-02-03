package parkeersimulator.logic;

import parkeersimulator.objects.*;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.*;

/**
 * The main model initializing.
 */
public class CarParkModel extends AbstractModel implements Runnable {

    private static final String AD_HOC = "1";
    private static final String PASS = "2";
    private static final String BAD = "3";
    private static final String RES = "4";
    private int weekDayArrivals = 100; // average number of arriving cars per hour
    private int weekendArrivals = 200; // average number of arriving cars per hour
    private int weekDayResArrivals = 5; // average number of arriving cars per hour
    private int weekendResArrivals = 15; // average number of arriving cars per hour
    private int weekDayBadArrivals = 4;
    private int weekendBadArrivals = 10;
    private int weekDayPassArrivals = 50; // average number of arriving cars per hour
    private int weekendPassArrivals = 5; // average number of arriving cars per hour
    private int numberOfPasses = 15;
    private int enterSpeed = 3; // number of cars that can enter per minute
    private int paymentSpeed = 7; // number of cars that can pay per minute
    private int exitSpeed = 5; // number of cars that can leave per minute

    private CarQueue entranceCarQueue;
    private CarQueue entrancePassQueue;
    private CarQueue paymentCarQueue;
    private CarQueue exitCarQueue;
    private Garage garage;
    private TicketMachine ticketMachine;
    private PropertyChangeSupport changes = new PropertyChangeSupport(this);
    private String[] colors;
    private int day = 0;
    private int hour = 0;
    private int minute = 0;
    private int tickPause = 10;
    private int steps = 0;

    private ArrayList<Location> spots;
    private ArrayList<Location> passHolders;
    private Map<String, Integer> modelSettings;
    private int minutesToRun;
    private TreeMap<String, String> modelStats;
    private boolean run;

    // Fields from SimulatorView
    private int numberOfFloors = 3;
    private int numberOfRows = 6;
    private int numberOfPlaces = 30;
    private int numberOfOpenSpots;
    private Car[][][] cars;

    /**
     * Creates the car park.
     */
    public CarParkModel() {
        modelSettings = new HashMap<>();
        modelStats = new TreeMap<>();
        entranceCarQueue = new CarQueue();
        entrancePassQueue = new CarQueue();
        paymentCarQueue = new CarQueue();
        exitCarQueue = new CarQueue();
        ticketMachine = new TicketMachine(this);
        setNumberOfOpenSpots();
        run = false;
        numberOfOpenSpots = numberOfFloors * numberOfRows * numberOfPlaces;
        cars = new Car[numberOfFloors][numberOfRows][numberOfPlaces];
        spots = new ArrayList<>();
        passHolders = new ArrayList<>();
        getAllSpots();
        garage = new Garage(this);
        setPassSpot();
        setDefaults();
        fillColors();
    }

    /*
    Stop alle default waarden in de hashMap.
     */
    private void setDefaults() {
        modelSettings.put("numberOfFloors", numberOfFloors);
        modelSettings.put("numberOfRows", numberOfRows);
        modelSettings.put("numberOfPlaces", numberOfPlaces);
        modelSettings.put("weekDayArrivals", weekDayArrivals);
        modelSettings.put("weekendArrivals", weekendArrivals);
        modelSettings.put("weekDayBadArrivals", weekDayBadArrivals);
        modelSettings.put("weekendBadArrivals", weekendBadArrivals);
        modelSettings.put("weekDayPassArrivals", weekDayPassArrivals);
        modelSettings.put("weekendPassArrivals", weekendPassArrivals);
        modelSettings.put("numberOfPasses", numberOfPasses);
        modelSettings.put("enterSpeed", enterSpeed);
        modelSettings.put("paymentSpeed", paymentSpeed);
        modelSettings.put("exitSpeed", exitSpeed);
        modelSettings.put("tickPause", tickPause);
        setReferences();
    }

    /*
    Deze functie set de referenties naar die van de hashmap.
     */
    public void setReferences() {
        numberOfFloors = modelSettings.get("numberOfFloors");
        numberOfRows = modelSettings.get("numberOfRows");
        numberOfPlaces = modelSettings.get("numberOfPlaces");
        weekDayArrivals = modelSettings.get("weekDayArrivals");
        weekendArrivals = modelSettings.get("weekendArrivals");
        weekDayBadArrivals = modelSettings.get("weekDayBadArrivals");
        weekendBadArrivals = modelSettings.get("weekendBadArrivals");
        weekDayPassArrivals = modelSettings.get("weekDayPassArrivals");
        weekendPassArrivals = modelSettings.get("weekendPassArrivals");
        numberOfPasses = modelSettings.get("numberOfPasses");
        enterSpeed = modelSettings.get("enterSpeed");
        paymentSpeed = modelSettings.get("paymentSpeed");
        exitSpeed = modelSettings.get("exitSpeed");
        tickPause = modelSettings.get("tickPause");
    }

    public Map<String, Integer> getOptions() {
        return modelSettings;
    }

    public int getOption(String ref) {
        return modelSettings.get(ref);
    }

    public void setOption(String ref, int val) {
        modelSettings.put(ref, val);
    }

    public Map<String, String> getStats() {
        return modelStats;
    }

    /**
     * Gets the day of the week
     *
     * @return The day of the week
     */
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

    /**
     * A method to fill an array of strings.
     * In this case each key represents the current hour in the sim.
     * TODO make the switch between dark and light more even.
     */
    private void fillColors() {
        colors = new String[24];
        colors[0] = "#999999";
        colors[1] = "#a6a6a6";
        colors[2] = "#b3b3b3";
        colors[3] = "#bfbfbf";
        colors[4] = "#cccccc";
        colors[5] = "#d9d9d9";
        colors[6] = "#e6e6e6";
        colors[7] = "#f2f2f2";
        // Here it's light!
        colors[8] = "#ffffff";
        colors[9] = "#ffffff";
        colors[10] = "#ffffff";
        colors[11] = "#ffffff";
        colors[12] = "#ffffff";
        colors[13] = "#ffffff";
        colors[14] = "#ffffff";
        colors[15] = "#ffffff";
        colors[16] = "#ffffff";
        // It's going dark again.
        colors[17] = "#f2f2f2";
        colors[18] = "#e6e6e6";
        colors[19] = "#d9d9d9";
        colors[20] = "#cccccc";
        colors[21] = "#bfbfbf";
        colors[22] = "#b3b3b3";
        colors[23] = "#a6a6a6";
    }

    public String getColor() {
        String newColor = colors[getHour()];
        return newColor;
    }

    public int getHour() {
        return hour;
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

    private void updatePassSpot() {
        passHolders.clear();
        for (int i = 0; i < numberOfPasses; i++) {
            passHolders.add(spots.get(i));
        }
    }

    /**
     * Starts the simulation.
     *
     * @param minutesToRun The amount of minutes the simulation has to run.
     */
    public void start(int minutesToRun) {
        this.minutesToRun = minutesToRun;
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
     * And added an bean fire method. If the variable change a bean will be fired.
     */
    private void advanceTime() {
        // Advance the time by one minute.
        minute++;
        while (minute > 59) {
            minute -= 60;
            // This is the firing method.
            hour++;
            changes.firePropertyChange("Hour change", null, hour);
        }
        while (hour > 23) {
            hour -= 24;
            day++;
        }
        while (day > 6) {
            day -= 7;
        }
    }

    /**
     * We want to listen to our changed property.
     *
     * @param l The property change listener
     */
    public void addPropertyListener(PropertyChangeListener l) {
        changes.addPropertyChangeListener(l);
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

    private void updateStats() {
        modelStats.put("Time", String.format("%02d", hour) + ":" + String.format("%02d", minute) + ":00");
        modelStats.put("Day", getDay());
        modelStats.put("Entrance Queue", String.valueOf(entranceCarQueue.carsInQueue()));
        modelStats.put("Pass Queue", String.valueOf(entrancePassQueue.carsInQueue()));
        modelStats.put("Payment Queue", String.valueOf(paymentCarQueue.carsInQueue()));
        modelStats.put("Exit Queue", String.valueOf(exitCarQueue.carsInQueue()));
        modelStats.put("Open Spots", String.valueOf(getNumberOfOpenSpots()));
    }

    /**
     * Update all views.
     */
    private void updateViews() {
        updateStats();
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
        numberOfCars = getNumberOfCars(weekDayResArrivals, weekendResArrivals);
        addArrivingCars(numberOfCars, RES);
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
            setStateAt(freeLocation, car);
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
                System.out.println("## Time left: " + car.getTotalMinutes() + " ## ");

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
            int ml = car.getMinutesLeft();
            System.out.print("A Car has paid");
            ticketMachine.normalPay();

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

    private boolean isTheather() {
        String friday = "vrijdag";
        String saturday = "zaterdag";
        String sunday = "zondag";
        if (getDay().equals(friday) || getDay().equals(saturday)) {
            if (hour >= 18 && hour <= 20) {
                return true;
            } else {
                return false;
            }
        } else if (getDay().equals(sunday)) {
            return hour >= 14 && hour <= 18;
        }
        return false;
    }

    private boolean isShopNight() {
        if (getDay().equals("donderdag")) {
            if (hour >= 17 && hour <= 22) {
                return true;
            }
        }
        return false;
    }

    private boolean isNight() {
        return hour > 20 || hour < 7;
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


        if (isTheather()) {
            averageNumberOfCarsPerHour = (int) Math.ceil(averageNumberOfCarsPerHour * 3);
        } else if (isNight()) {
            averageNumberOfCarsPerHour = (int) Math.ceil(averageNumberOfCarsPerHour / 3);
        }

        if (isShopNight()) {
            averageNumberOfCarsPerHour = (int) Math.ceil(averageNumberOfCarsPerHour * 1.5);
        } else if (isNight()) {
            averageNumberOfCarsPerHour = (int) Math.ceil(averageNumberOfCarsPerHour / 3);
        }

        averageNumberOfCarsPerHour -= 10 * (entranceCarQueue.carsInQueue());

        // Calculate the number of cars that arrive this minute.
        double standardDeviation = 15;
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
            case RES:
                for (int i = 0; i < numberOfCars; i++) {
                    Car car = new ResCar();
                    setStateAt(getFirstFreeLocation(car), car);
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
        if (car instanceof ResCar) {
            removeCarAt(car.getLocation());
            setStateAt(car.getLocation(), new AdHocCar());
            return;
        }
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
    public boolean setStateAt(Location location, Car car) {
        if (!locationIsValid(location)) {
            return false;
        }
        Car oldCar = getCarAt(location);
        if (car instanceof ParkingPassCar) {
            if (garage.getStateAt(location) == 5 || garage.getStateAt(location) == 0) {
                if ((isTheather() && (hour >= 18 && hour <= 19)) || (isTheather() && (hour >= 13 && hour <= 14))) {
                    car.setMinutesLeft(240);
                }
                cars[location.getFloor()][location.getRow()][location.getPlace()] = car;
                car.setLocation(location);
                garage.setStateAt(location, car.getState());
                numberOfOpenSpots--;
                return true;
            }
        }
        if (oldCar == null || garage.getStateAt(location) == 0) {
            if (car instanceof BadParkerCar) {
                if ((isTheather() && (hour >= 18 && hour <= 19)) || (isTheather() && (hour >= 13 && hour <= 14))) {
                    car.setMinutesLeft(240);
                }
                cars[location.getFloor()][location.getRow()][location.getPlace()] = car;
                car.setLocation(location);
                if (((BadParkerCar) car).getSecondLocation().getPlace() < car.getLocation().getPlace()) {
                    garage.setStateAt(location, ((BadParkerCar) car).getState2());
                    garage.setStateAt(((BadParkerCar) car).getSecondLocation(), car.getState());
                } else {
                    garage.setStateAt(location, car.getState());
                    garage.setStateAt(((BadParkerCar) car).getSecondLocation(), ((BadParkerCar) car).getState2());
                }
                numberOfOpenSpots -= 2;
                return true;
            }
            if ((isTheather() && (hour >= 18 && hour <= 19)) || (isTheather() && (hour >= 13 && hour <= 14))) {
                car.setMinutesLeft(240);
            }
            cars[location.getFloor()][location.getRow()][location.getPlace()] = car;
            car.setLocation(location);
            garage.setStateAt(location, car.getState());
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
            cars[loc2.getFloor()][loc2.getRow()][loc2.getPlace()] = null;
            car.setLocation(null);
            ((BadParkerCar) car).setSecondLocation(null);
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


    @Override
    /**
     * Starts the simulation.
     */
    public void run() {
        for (int i = 0; i < minutesToRun && run; i++) {
            tick();
            notifyViews();
            try {
                Thread.sleep(tickPause);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        run = false;

        minutesToRun = 0;
    }
}
