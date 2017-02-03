package parkeersimulator.view;

import parkeersimulator.logic.CarParkModel;
import parkeersimulator.objects.BadParkerCar;
import parkeersimulator.objects.Car;
import parkeersimulator.objects.Location;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class CarParkView extends AbstractView {

    private Dimension size;
    private Image carParkImage;
    private BufferedImage adhoc;
    private BufferedImage pass;
    private BufferedImage empty;
    private BufferedImage passPlace;
    private BufferedImage badParker;
    private BufferedImage badParker2;
    private BufferedImage resCar;
    private BufferedImage image;
    private String imageString;

    /**
     * Constructor for objects of class CarPark
     */
    public CarParkView(CarParkModel model) {
        super(model);
        size = new Dimension(0, 0);
        loadImages();
        image = empty;
        imageString = "empty";

    }

    public static BufferedImage rotate(BufferedImage image, double angle) {
        double sin = Math.abs(Math.sin(angle)), cos = Math.abs(Math.cos(angle));
        int w = image.getWidth(), h = image.getHeight();
        int neww = (int) Math.floor(w * cos + h * sin), newh = (int) Math.floor(h * cos + w * sin);
        GraphicsConfiguration gc = getDefaultConfiguration();
        BufferedImage result = gc.createCompatibleImage(neww, newh, Transparency.TRANSLUCENT);
        Graphics2D g = result.createGraphics();
        g.translate((neww - w) / 2, (newh - h) / 2);
        g.rotate(angle, w / 2, h / 2);
        g.drawRenderedImage(image, null);
        g.dispose();
        return result;
    }

    private static GraphicsConfiguration getDefaultConfiguration() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        return gd.getDefaultConfiguration();
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
                    int state = model.getLocInfo(location);
                    switch (state) {
                        case 0:
                            image = empty;
                            imageString = "empty";
                            break;
                        case 1:
                            image = adhoc;
                            imageString = "adhoc";
                            break;
                        case 2:
                            image = pass;
                            imageString = "pass";
                            break;
                        case 5:
                            image = passPlace;
                            imageString = "passPlace";
                            break;
                        case 6:
                            image = badParker;
                            imageString = "badParker";
                            break;
                        case 7:
                            image = badParker2;
                            imageString = "badParker2";
                            break;
                        case 8:
                            image = resCar;
                            imageString = "resCar";
                            break;
                        default:
                            image = empty;
                            imageString = "empty";
                    }
                    doDrawing(graphics, location);
                }
            }
        }
        repaint();
    }

    /**
     * Paint a place on this car park view in a specific image.
     **/
    private void doDrawing(Graphics g, Location location) {
        if ((location.getRow() + 2) % 2 == 0) {
            if (imageString.equals("badParker2")) {
                image = badParker;
            }
            if (imageString.equals("badParker")) {
                image = badParker2;
            }
            AffineTransform tx = AffineTransform.getScaleInstance(-1, -1);
            tx.translate(-image.getWidth(null), -image.getHeight(null));
            AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
            image = op.filter(image, null);
        }

        g.drawImage(image, location.getFloor() * 260 + (1 + (int) Math.floor(location.getRow() * 0.5)) * 75 + (location.getRow() % 2) * 26, 60 + location.getPlace() * 13, 26, 13, Color.decode("#5c5c5c"), null);
    }

    private void loadImages() {
        try {
            adhoc = ImageIO.read(getClass().getResource("resources/RedCar.png"));
            pass = ImageIO.read(getClass().getResource("resources/BlueCar.png"));
            empty = ImageIO.read(getClass().getResource("resources/Empty.png"));
            passPlace = ImageIO.read(getClass().getResource("resources/PassPlace.png"));
            badParker = ImageIO.read(getClass().getResource("resources/BadParker.png"));
            badParker2 = ImageIO.read(getClass().getResource("resources/BadParker2.png"));
            resCar = ImageIO.read(getClass().getResource("resources/ResCar.png"));
        } catch (IOException ex) {
        }
    }
}