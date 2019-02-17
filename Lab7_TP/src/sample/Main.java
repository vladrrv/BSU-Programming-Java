package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();
        primaryStage.setTitle("Lab 7");
        Scene scene = new Scene(root);
        //scene.setOnKeyPressed(new OnPressed(controller)); // not working
        scene.addEventFilter(KeyEvent.KEY_PRESSED, new OnPressed(controller)); // working
        scene.setOnKeyReleased(new OnReleased(controller));
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
