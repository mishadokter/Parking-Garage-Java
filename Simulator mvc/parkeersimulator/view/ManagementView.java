package parkeersimulator.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by Misha on 26-1-2017.
 */

public class ManagementView extends DefaultView {

    @FXML
    private PieChart piechart;
    @FXML
    private BarChart barChart;

    public ManagementView() throws IOException {
        super();
    }

    @Override
    public String getLayoutFile() {
        return "management.fxml";
    }

    @Override
    public String getTitle() {
        return "Management";
    }

    @FXML
    private void handleButtonBarChart(ActionEvent event) {

        String monday = "Monday";
        String tuesday = "Tuesday";
        String wednesday = "Wednesday";
        String thursday = "Thursday";
        String friday = "Friday";
        String saturday = "Saturday";
        String sunday = "Sunday";

        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        final BarChart<String, Number> bc = new BarChart<>(xAxis, yAxis);
        bc.setTitle("Country Summary");
        xAxis.setLabel("Country");
        yAxis.setLabel("Value");

        XYChart.Series series1 = new XYChart.Series();
        series1.setName("2003");
        series1.getData().add(new XYChart.Data(monday, 25601.34));
        series1.getData().add(new XYChart.Data(tuesday, 20148.82));
        series1.getData().add(new XYChart.Data(wednesday, 10000));
        series1.getData().add(new XYChart.Data(thursday, 35407.15));
        series1.getData().add(new XYChart.Data(friday, 12000));


        bc.getData().addAll(series1);
        barChart.setData(bc.getData());
        barChart.setAnimated(false);
        barChart.setTitle("Daily car entrances");
        barChart.setVisible(true);
        piechart.setVisible(false); // Maak de piechart invisible
    }


    @FXML
    private void handleButton1Action(ActionEvent event) {
        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        new PieChart.Data("Sunday", 230),
                        new PieChart.Data("Monday", 445),
                        new PieChart.Data("Tuesday", 570),
                        new PieChart.Data("Wednesday", 597),
                        new PieChart.Data("Thursday", 1100),
                        new PieChart.Data("Friday", 780),
                        new PieChart.Data("Saturday", 710));

        piechart.setTitle("Daily car entrances");
        piechart.setData(pieChartData);
        piechart.setStyle("-fx-background-color: red, yellow, #e1e1e1;\n" +
                "    -fx-padding: 5 5 5 5;\n" +
                "    -fx-background-insets: 0, 1, 2;");

        piechart.setVisible(true);
        barChart.setVisible(false); // Maak de barchart invisible

        /*final PieChart chart = new PieChart(pieChartData);
        final Label caption = new Label("");
        caption.setTextFill(Color.BLACK);

        for (final PieChart.Data data : piechart.getData()) {
            data.getNode().addEventHandler(MouseEvent.MOUSE_PRESSED,
                    new EventHandler<MouseEvent>() {
                        @Override public void handle(MouseEvent e) {
                            caption.setTranslateX(e.getSceneX());
                            caption.setTranslateY(e.getSceneY());
                            caption.setText(String.valueOf(data.getPieValue())
                                    + "auto's");
                        }
                    });
        }*/

    }
}
