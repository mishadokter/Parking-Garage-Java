package Parkeersimulator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SimulatorView extends JFrame implements ActionListener {
    private CarParkView carParkView;
    private int numberOfFloors;
    private int numberOfRows;
    private int numberOfPlaces;
    private int numberOfOpenSpots;
    private Simulator simulator;
    private Car[][][] cars;

    private JButton stepOne;
    private JPopupMenu textTester;
    private JButton stepHundred;

    private JButton lastClicked;

    public SimulatorView(int numberOfFloors, int numberOfRows, int numberOfPlaces, Simulator simulator) {
        this.numberOfOpenSpots = numberOfFloors * numberOfRows * numberOfPlaces;
        this.simulator = simulator;
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        cars = new Car[numberOfFloors][numberOfRows][numberOfPlaces];
        stepOne = new JButton("+1");
        stepOne.addActionListener(this);
        textTester = new JPopupMenu("Testor");
        stepHundred = new JButton("+100");
        stepHundred.addActionListener(this);
        carParkView = new CarParkView();

        Container contentPane = getContentPane();
        contentPane.add(carParkView, BorderLayout.CENTER);
        carParkView.setLayout(null);
        carParkView.add(stepOne);
        carParkView.add(textTester);
        carParkView.add(stepHundred);
        stepOne.setBounds(50, 10, 70, 30);
        stepHundred.setBounds(140, 10, 70, 30);
        textTester.setBounds(210, 10, 70, 30);
        pack();
        setVisible(true);

        updateView();
    }

    public void updateView() {
        carParkView.updateView();
    }

    public int getNumberOfFloors() {
        return numberOfFloors;
    }

    public int getNumberOfRows() {
        return numberOfRows;
    }

    public int getNumberOfPlaces() {
        return numberOfPlaces;
    }

    public int getNumberOfOpenSpots() {
        return numberOfOpenSpots;
    }

    public Car getCarAt(Location location) {
        if (!locationIsValid(location)) {
            return null;
        }
        return cars[location.getFloor()][location.getRow()][location.getPlace()];
    }

    public boolean setCarAt(Location location, Car car) {
        if (!locationIsValid(location)) {
            return false;
        }
        Car oldCar = getCarAt(location);
        if (car instanceof ParkingPassCar) {
            if (oldCar instanceof ReservationCar) {
                removeCarAt(location);
                cars[location.getFloor()][location.getRow()][location.getPlace()] = car;
                car.setLocation(location);
                numberOfOpenSpots--;
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

    public Car removeCarAt(Location location) {
        if (!locationIsValid(location)) {
            return null;
        }
        Car car = getCarAt(location);
        if (car == null) {
            return null;
        }
        cars[location.getFloor()][location.getRow()][location.getPlace()] = null;
        if (location.getFloor() == 0 && location.getRow() == 0 && location.getPlace() >= 0 && location.getPlace() < 30) {
            cars[location.getFloor()][location.getRow()][location.getPlace()] = null;
            car.setLocation(null);
            setCarAt(location, new ReservationCar());
            return car;
        }
        car.setLocation(null);
        numberOfOpenSpots++;
        return car;
    }

    public Location getFirstFreeLocation(Car car) {
        for (int floor = 0; floor < getNumberOfFloors(); floor++) {
            for (int row = 0; row < getNumberOfRows(); row++) {
                for (int place = 0; place < getNumberOfPlaces(); place++) {
                    Location location = new Location(floor, row, place);
                    if (car instanceof ParkingPassCar) {
                        if (getCarAt(location) instanceof ReservationCar || getCarAt(location) == null) {
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

    public void tick() {
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

    private boolean locationIsValid(Location location) {
        int floor = location.getFloor();
        int row = location.getRow();
        int place = location.getPlace();
        if (floor < 0 || floor >= numberOfFloors || row < 0 || row > numberOfRows || place < 0 || place > numberOfPlaces) {
            return false;
        }
        return true;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == stepOne) {
            try {
                simulator.stepOne();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        if (e.getSource() == stepHundred) {
            lastClicked = stepHundred;
            lastClicked.setEnabled(false);
            try {
                simulator.stepHundred();
            } catch (Exception ex) {
                ex.printStackTrace();
                lastClicked.setEnabled(true);
            }
        }
    }

    public JButton getCurrentButton() {
        return lastClicked;
    }

    private class CarParkView extends JPanel {

        private Dimension size;
        private Image carParkImage;

        /**
         * Constructor for objects of class CarPark
         */
        public CarParkView() {
            size = new Dimension(0, 0);
        }

        /**
         * Overridden. Tell the GUI manager how big we would like to be.
         */
        public Dimension getPreferredSize() {
            return new Dimension(800, 500);
        }

        /**
         * Overriden. The car park view component needs to be redisplayed. Copy the
         * internal image to screen.
         */
        public void paintComponent(Graphics g) {
            if (carParkImage == null) {
                return;
            }

            Dimension currentSize = getSize();
            if (size.equals(currentSize)) {
                g.drawImage(carParkImage, 0, 0, null);
            } else {
                // Rescale the previous image.
                g.drawImage(carParkImage, 0, 0, currentSize.width, currentSize.height, null);
            }
        }

        public void updateView() {
            // Create a new car park image if the size has changed.
            if (!size.equals(getSize())) {
                size = getSize();
                carParkImage = createImage(size.width, size.height);
            }
            Graphics graphics = carParkImage.getGraphics();
            for (int floor = 0; floor < getNumberOfFloors(); floor++) {
                for (int row = 0; row < getNumberOfRows(); row++) {
                    for (int place = 0; place < getNumberOfPlaces(); place++) {
                        Location location = new Location(floor, row, place);
                        Car car = getCarAt(location);
                        Color color = car == null ? Color.white : car.getColor();
                        drawPlace(graphics, location, color);
                    }
                }
            }
            repaint();
        }

        /**
         * Paint a place on this car park view in a given color.
         */
        private void drawPlace(Graphics graphics, Location location, Color color) {
            graphics.setColor(color);
            graphics.fillRect(
                    location.getFloor() * 260 + (1 + (int) Math.floor(location.getRow() * 0.5)) * 75 + (location.getRow() % 2) * 20,
                    60 + location.getPlace() * 10,
                    20 - 1,
                    10 - 1); // TODO use dynamic size or constants
        }
    }
}
