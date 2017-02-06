package parkeersimulator.objects;

import parkeersimulator.logic.CarParkModel;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class Garage {

    private LinkedHashMap<String, Integer> state;
    private int numberOfFloors;
    private int numberOfRows;
    private int numberOfPlaces;

    public Garage(CarParkModel model) {
        state = new LinkedHashMap<>();
        numberOfFloors = model.getNumberOfFloors();
        numberOfRows = model.getNumberOfRows();
        numberOfPlaces = model.getNumberOfPlaces();
        fillStates();
    }

    /**
     * Fills all location in a map and make all the states 0, "empty".
     */
    private void fillStates() {
        for (int floor = 0; floor < numberOfFloors; floor++) {
            for (int row = 0; row < numberOfRows; row++) {
                for (int place = 0; place < numberOfPlaces; place++) {
                    Location location = new Location(floor, row, place);
                    String loc = location.toString();
                    state.put(loc, 0);
                }
            }
        }
    }

    /**
     * Sets the state of a location.
     *
     * @param location The location a state needs to be set.
     * @param state    The state that need to be set at the location.
     */
    public void setStateAt(Location location, int state) {
        String loc = location.toString();
        this.state.put(loc, state);
    }

    /**
     * Gets the state of a location.
     *
     * @param location The location a state needs to return.
     * @return The state of a location.
     */
    public int getStateAt(Location location) {
        String loc = location.toString();
        return state.get(loc);
    }

    public int getAdhoc() {
        int adhoc = 0;
        Iterator it = state.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            if ((int) pair.getValue() == 1) {
                adhoc++;
            }
        }
        return adhoc;
    }

    public int getPass() {
        int pass = 0;
        Iterator it = state.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            if ((int) pair.getValue() == 2) {
                pass++;
            }
        }
        return pass;
    }

    public int getEmpty() {
        int empty = 0;
        Iterator it = state.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            if ((int) pair.getValue() == 0) {
                empty++;
            }
        }
        return empty;
    }

    public int getPassPlace() {
        int passPlace = 0;
        Iterator it = state.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            if ((int) pair.getValue() == 5) {
                passPlace++;
            }
        }
        return passPlace;
    }

    public int getBadParker() {
        int badParker = 0;
        Iterator it = state.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            if ((int) pair.getValue() == 6) {
                badParker++;
            }
        }
        return badParker;
    }
}
