package parkeersimulator.view;

import parkeersimulator.logic.CarParkModel;
import parkeersimulator.objects.Car;
import parkeersimulator.objects.Location;

import javax.swing.*;
import java.awt.*;

public class CarParkView extends AbstractView {

    private Dimension size;
    private Image carParkImage;
    private JLabel steps;

    /**
     * Constructor for objects of class CarPark
     */
    public CarParkView(CarParkModel model) {

        super(model);
        size = new Dimension(0, 0);
        steps = new JLabel();

        add(steps);
        steps.setBounds(410, 10, 70, 30);
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
            // g.setColor(Color.DARK_GRAY);
        } else {
            // Rescale the previous image.
            g.drawImage(carParkImage, 0, 0, currentSize.width, currentSize.height, null);
            //g.setColor(Color.DARK_GRAY);
        }
    }

    /*The state of the location mean:
    0 - empty place
    1 - taken place
    2 - place taken by pass holder
    5 - empty place for pass holders*/
    public void updateView() {
        steps.setText(model.getSteps());
        //steps.setText(model.getQueue().toString());
        //model.getQueue();
        // Create a new car park image if the size has changed.
        if (!size.equals(getSize())) {
            size = getSize();
            carParkImage = createImage(size.width, size.height);
        }
        carParkImage = createImage(size.width, size.height);
        Graphics graphics = carParkImage.getGraphics();
        for (int floor = 0; floor < model.getNumberOfFloors(); floor++) {
            for (int row = 0; row < model.getNumberOfRows(); row++) {
                for (int place = 0; place < model.getNumberOfPlaces(); place++) {
                    Location location = new Location(floor, row, place);
                    Car car = model.getCarAt(location);
                    int state = model.getLocInfo(location);
                    Color color;
                    switch (state) {
                        case 0:
                            color = Color.WHITE;
                            break;
                        case 1:
                            color = Color.RED;
                            break;
                        case 2:
                            color = Color.BLUE;
                            break;
                        case 5:
                            color = Color.GREEN;
                            break;
                        case 6:
                            color = Color.DARK_GRAY;
                            break;
                        default:
                            color = Color.WHITE;
                    }
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
        //graphics.setColor(Color.DARK_GRAY);
    }
}