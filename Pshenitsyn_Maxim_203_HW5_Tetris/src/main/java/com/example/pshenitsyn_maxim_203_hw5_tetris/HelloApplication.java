package com.example.pshenitsyn_maxim_203_hw5_tetris;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;


public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 696, 576);
        stage.setTitle("TETRIS");
        stage.setScene(scene);
        stage.setMinHeight(600);
        stage.setMinWidth(680);
        stage.setOnCloseRequest(
                event -> {
                    Alert alert = new Alert(Alert.AlertType.WARNING, "Press 'Finish game' button" +
                            "to close the game", ButtonType.OK);
                    alert.setTitle("Not available");
                    alert.showAndWait();
                    event.consume();
                }
        );
        stage.show();

        // test
    }

    public static void main(String[] args) {
        launch();
    }
}