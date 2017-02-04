package parkeersimulator.objects;

import java.util.LinkedList;
import java.util.Queue;

public class CarQueue {
    private Queue<Car> queue = new LinkedList<>();

    /**
     * Adds a car into the queue.
     *
     * @param car The car that has to be added.
     * @return If it placed the car succesfully in the queue.
     */
    public boolean addCar(Car car) {
        return queue.add(car);
    }

    /**
     * Removes a car from the queue.
     *
     * @return The car that has been removes from the queue.
     */
    public Car removeCar() {
        return queue.poll();
    }

    /**
     * Gets amount of cars in the queue.
     *
     * @return The amount of cars in the queue.
     */
    public int carsInQueue() {
        return queue.size();
    }
}