package parkeersimulator.objects;

import parkeersimulator.logic.CarParkModel;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class Garage {

    private LinkedHashMap<String, Integer> state;
    private HashMap<String, Car> cars;
    private int numberOfFloors;
    private int numberOfRows;
    private int numberOfPlaces;
    private CarParkModel model;

    public Garage(CarParkModel model) {
        state = new LinkedHashMap<>();
        cars = new HashMap<>();
        this.model = model;
        numberOfFloors = model.getNumberOfFloors();
        numberOfRows = model.getNumberOfRows();
        numberOfPlaces = model.getNumberOfPlaces();
        fillStates();
    }

    public void fillStates() {
        for (int floor = 0; floor < model.getNumberOfFloors(); floor++) {
            for (int row = 0; row < model.getNumberOfRows(); row++) {
                for (int place = 0; place < model.getNumberOfPlaces(); place++) {
                    Location location = new Location(floor, row, place);
                    String loc = location.toString();
                    state.put(loc, 0);
                }
            }
        }
    }

    public LinkedHashMap getHashMap() {
        return state;
    }

    public void setStateAt(Location location, int state) {
        String loc = location.toString();
        this.state.put(loc, state);
    }

    public int getStateAt(Location location) {
        String loc = location.toString();
        return state.get(loc);
    }
}
