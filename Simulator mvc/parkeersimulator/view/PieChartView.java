package parkeersimulator.view;

/**
 * Created by Misha on 31-1-2017.
 */
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

                /*              !!
                Wordt momenteel niet gebruikt -> Staat in de ManagementView
                                !!                                       */
public class PieChartView extends Application {

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(new Group());
        stage.setTitle("Cars");
        stage.setWidth(500);
        stage.setHeight(500);


        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        new PieChart.Data("Sunday", 230),
                        new PieChart.Data("Monday", 445),
                        new PieChart.Data("Tuesday", 570),
                        new PieChart.Data("Wednesday", 597),
                        new PieChart.Data("Thursday", 1100),
                        new PieChart.Data("Friday", 780),
                        new PieChart.Data("Saturday", 710));

        final PieChart chart = new PieChart(pieChartData);
        chart.setTitle("Number of Cars");
        final Label caption = new Label("");
        caption.setTextFill(Color.DARKORANGE);
        caption.setStyle("-fx-font: 24 arial;");

        for (final PieChart.Data data : chart.getData()) {
            data.getNode().addEventHandler(MouseEvent.MOUSE_PRESSED,
                    new EventHandler<MouseEvent>() {
                        @Override public void handle(MouseEvent e) {
                            caption.setTranslateX(e.getSceneX());
                            caption.setTranslateY(e.getSceneY());
                            caption.setText(String.valueOf(data.getPieValue())
                                    + "%");
                        }
                    });
        }

        ((Group) scene.getRoot()).getChildren().addAll(chart, caption);
        stage.setScene(scene);
        //scene.getStylesheets().add("piechartsample/Chart.css");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}