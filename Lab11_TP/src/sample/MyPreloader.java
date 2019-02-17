package sample;

import javafx.application.Preloader;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import static javafx.application.Preloader.StateChangeNotification.Type.BEFORE_START;

public class MyPreloader extends Preloader {
    private Label progress;
    private ProgressBar progressBar;
    private Stage preloaderStage, hiddenStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("preloader.fxml"));
        Parent root = loader.load();
        PreloaderController controller = loader.getController();
        progress = controller.getProgress();
        progressBar = controller.getProgressBar();
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        primaryStage.setScene(scene);
        hiddenStage = primaryStage;
        primaryStage.initStyle(StageStyle.UTILITY);
        primaryStage.setWidth(0);
        primaryStage.setHeight(0);
        primaryStage.setOpacity(0);
        primaryStage.show();
        Stage mainStage = new Stage(StageStyle.TRANSPARENT);
        mainStage.initOwner(primaryStage);
        preloaderStage = mainStage;
        preloaderStage.setScene(scene);
        preloaderStage.show();
    }

    @Override
    public void handleApplicationNotification(PreloaderNotification info) {
        if (progress != null && info instanceof ProgressNotification) {
            double p = ((ProgressNotification) info).getProgress();
            progress.setText((int)p + "%");
            if (p > 50) progress.setTextFill(Color.WHITE);
            progressBar.setProgress(p/100);
        }
    }

    @Override
    public void handleStateChangeNotification(StateChangeNotification info) {
        StateChangeNotification.Type type = info.getType();
        if (type == BEFORE_START) {
            preloaderStage.hide();
            hiddenStage.hide();
        }
    }
}
