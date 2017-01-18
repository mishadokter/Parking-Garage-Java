package Parkeersimulator;

public class SimThread extends Thread {

    Simulator simulator;

    public SimThread(Simulator simulator) {
        this.simulator = simulator;
    }

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            simulator.stepOne();
            try {
                Thread.sleep(100);
            } catch (Exception e) {

            }
        }
    }
}
