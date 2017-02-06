package parkeersimulator.objects;

import parkeersimulator.logic.CarParkModel;

/**
 * This class handles costs & keeps track of the total payments made.
 * Created by sanderpost on 02-02-17.
 */
public class TicketMachine {
    private int total;
    private int totalPayments;
    private int shortFee = 160;
    private int hourFee = 300;
    private int dayFee = 2300;
    private CarParkModel model;

    /**
     * Constructor of this class.
     * If a new Sim is started, we set our total payments to 0.
     * @param model The model we initiated.
     */
    public TicketMachine(CarParkModel model) {
        this.model = model;
        totalPayments = 0;
    }

    /**
     * Method to set a new price for stays that are 20 minutes or shorter.
     * @param newFee    The new fee
     */
    public void setShortFee(int newFee) {
        shortFee = newFee;
    }

    /**
     * Method to set a new price for the hourly fee.
     * @param newFee    The new fee
     */
    public void setHourFee(int newFee) {
        hourFee = newFee;
    }

    /**
     * Mehod to set a new price for the daily fee.
     * @param newFee    The new fee
     */
    public void setDayFee(int newFee) {
        dayFee = newFee;
    }

    /**
     * Invokes everytime a car has to pay (through te method in our model).
     * This method checks the car's total stayed minutes.
     * If it's shorter or exactly 20 minutes, the car pays the shortFee.
     * If the time is longer then 20 minutes & shorter then 1440 minutes (24 hours),
     * -the car pays per hour (minutes will be rounded up) (hours times the hourFee).
     * If the car parks longer then 1440 minutes or a multiplification of days,
     * -the car pays per day and the hours left (days times dayFee & hours times hourFee).
     * @param totalMinutes  Total minutes the stayed.
     */
    public void normalPay(int totalMinutes) {

        if (totalMinutes <= 20) {
            total = total + shortFee;
        } else if (totalMinutes > 20 && totalMinutes <= 1440) {
            int hours = (int) Math.ceil(totalMinutes / 60);
            total = total + (hours * hourFee);
        } else if (totalMinutes > 1440) {
            int days = (int) Math.ceil(totalMinutes / 1440);
            int hours = (int) Math.ceil((totalMinutes - (1440 * days))/60);
            total = total + (days * dayFee) + (hours * hourFee);
        } totalPayments++;
    }

    /**
     * Method to access the private fields of total collected money.
     * Everytime a payment is made, the total is adjusted.
     * @return  The total money collected.
     */
    public int getTotalMoney() {
        return total / 100;
    }

    /**
     * Method to access the private field of total payments.
     * @return  The total payments made
     */
    public int getTotalPayments() {
        return totalPayments;
    }
}
