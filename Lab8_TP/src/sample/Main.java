package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    private Controller controller;
    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        Parent root = loader.load();
        controller = loader.getController();
        Scene scene = new Scene(root);
        primaryStage.setTitle("Lab 8");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    @Override
    public void stop() {
        controller.close();
        Platform.exit();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
