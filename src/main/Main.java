package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // 修正資源路徑，改用相對路徑
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("main/resources/calendar.fxml"));
        primaryStage.setTitle("CalendarApp");
        primaryStage.setScene(new Scene(root, 1200, 1000));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}