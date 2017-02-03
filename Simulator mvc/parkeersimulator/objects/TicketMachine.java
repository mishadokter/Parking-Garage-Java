package parkeersimulator.objects;

import parkeersimulator.logic.CarParkModel;

/**
 * Created by sanderpost on 02-02-17.
 */
public class TicketMachine {
    private int total;
    private int totalPayments;
    private CarParkModel model;

    public TicketMachine(CarParkModel model) {
        this.model = model;
        totalPayments = 0;
    }

    public void normalPay() {
        int hourPay = 10;
        total = total + hourPay;
        totalPayments++;
    }

    public int getTotalMoney() {
        return total;
    }

    public int getTotalPayments() {
        return totalPayments;
    }
}
