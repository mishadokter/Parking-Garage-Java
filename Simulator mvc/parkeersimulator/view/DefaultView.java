package parkeersimulator.view;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;

/**
 * Created by Misha on 26-1-2017.
 */
public abstract class DefaultView {

    protected Stage defaultStage;
    private static Scene defaultScene;
        private void initAndShowGUI() throws IOException {
            // This method is invoked on the EDT thread
            JFrame frame = new JFrame("Swing and JavaFX");
            final JFXPanel fxPanel = new JFXPanel();
            frame.add(fxPanel);
            frame.setSize(800, 500);
            frame.setVisible(true);
            Parent root = FXMLLoader.load(getClass().getResource("../includes/designs/" + getLayoutFile()));
            defaultScene = new Scene(root);
            defaultScene.getStylesheets().add(getClass().getResource("../includes/styles/style.css").toString());

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    initFX(fxPanel);
                }
            });
        }

    private void initFX(JFXPanel fxPanel) {
        fxPanel.setScene(defaultScene);
    }
    public void run() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    initAndShowGUI();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public abstract String getLayoutFile();

    public abstract String getTitle();

}

