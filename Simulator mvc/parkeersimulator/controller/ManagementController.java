package parkeersimulator.controller;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import parkeersimulator.view.ManagementView;

import javax.swing.*;
import java.io.IOException;

/**
 * Created by Misha on 26-1-2017.
 */
public class ManagementController {

        public static void initAndShowGUI() throws IOException {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    ManagementView mv = null;
                    try {
                        mv = new ManagementView();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        private static void initFX(JFXPanel fxPanel) {
            // This method is invoked on the JavaFX thread
            Scene scene = createScene();
            fxPanel.setScene(scene);
        }

        private static Scene createScene() {
            Group root  =  new  Group();
            Scene  scene  =  new  Scene(root, Color.ALICEBLUE);
            Text text  =  new  Text();

            text.setX(40);
            text.setY(100);
            text.setFont(new Font(25));
            text.setText("Welcome JavaFX!");

            root.getChildren().add(text);

            return (scene);
        }

        public static void main(String[] args) {
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
    }

