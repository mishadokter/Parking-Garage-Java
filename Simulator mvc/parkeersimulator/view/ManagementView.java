package parkeersimulator.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import parkeersimulator.logic.CarParkModel;

/**
 * Created by Misha on 26-1-2017.
 */

public class ManagementView extends DefaultView {

    private CarParkModel model;
    @FXML
    private PieChart piechart;
    @FXML
    private BarChart barChart;

    public ManagementView(){
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

        XYChart.Series series1 = new XYChart.Series();
        series1.setName("Number of Cars");
        series1.getData().add(new XYChart.Data(monday, 546));
        series1.getData().add(new XYChart.Data(tuesday, 380));
        series1.getData().add(new XYChart.Data(wednesday, 562));
        series1.getData().add(new XYChart.Data(thursday, 985));
        series1.getData().add(new XYChart.Data(friday, 1130));
        series1.getData().add(new XYChart.Data(saturday, 1934));
        series1.getData().add(new XYChart.Data(sunday, 820));


        bc.getData().addAll(series1);
        barChart.setData(bc.getData());
        barChart.setAnimated(false); // Important
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



        final Label caption = new Label("");
        caption.setTextFill(Color.DARKORANGE);
        caption.setStyle("-fx-font: 24 arial;");

        for (final PieChart.Data data : piechart.getData()) {
            data.getNode().addEventHandler(MouseEvent.MOUSE_PRESSED,
                    e -> {
                        double total = 0;
                        for (PieChart.Data d : piechart.getData()) {
                            total += d.getPieValue();
                        }
                        caption.setTranslateX(e.getSceneX());
                        caption.setTranslateY(e.getSceneY());
                        String text = String.format("%.1f%%", 100*data.getPieValue()/total);
                        String textAmount = (String.valueOf(data.getPieValue()) + "");
                        caption.setText(text);
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Information Dialog");
                        alert.setHeaderText(null);
                        alert.setContentText(" Total number of cars of this day: " + textAmount + "\n This is " + text + " of 100% from this week" );

                        alert.showAndWait();
                    }
            );
        }

        piechart.setVisible(true);
        barChart.setVisible(false); // Maak de barchart invisible


    }
}
