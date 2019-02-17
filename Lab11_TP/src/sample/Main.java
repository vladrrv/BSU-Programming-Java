package sample;

import javafx.application.Application;
import javafx.application.Preloader;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;

public class Main extends Application {

    private static final int COUNT_LIMIT = 100000;
    private Controller controller;

    @Override
    public void init() {
        for (int i = 0; i < COUNT_LIMIT; i++) {
            double progress = (100 * i) / COUNT_LIMIT;
            notifyPreloader(new Preloader.ProgressNotification(progress));
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        Parent root = loader.load();
        controller = loader.getController();
        Scene scene = new Scene(root);
        primaryStage.setMinWidth(scene.getWidth());
        primaryStage.setMinHeight(scene.getHeight());
        primaryStage.setTitle("Lab 11");
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image(new File("res/app_icon_l.png").toURI().toString()));
        primaryStage.show();
    }

    public static void main(String[] args) {
        System.setProperty("javafx.preloader", "sample.MyPreloader");
        launch(args);
    }
}

