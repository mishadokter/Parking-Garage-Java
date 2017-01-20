package Parkeersimulator;

public class SimThread extends Thread {

    Simulator simulator;
    SimulatorView view;

    public SimThread(Simulator simulator, SimulatorView view) {
        this.simulator = simulator;
        this.view = view;
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
        view.getCurrentButton().setEnabled(true);
    }
}
