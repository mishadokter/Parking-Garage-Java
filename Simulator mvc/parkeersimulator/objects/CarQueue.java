package parkeersimulator.objects;

import java.util.LinkedList;
import java.util.Queue;

public class CarQueue {
    private Queue<Car> queue = new LinkedList<>();

    public boolean addCar(Car car) {
        return queue.add(car);
    }

    public Car removeCar() {
        return queue.poll();
    }

    public Car getCar() {
        return queue.element();
    }

    public void deleteCar() {
    }

    public int carsInQueue() {
        return queue.size();
    }
}