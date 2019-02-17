package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

public class PreloaderController {
    @FXML private Label progress;
    @FXML private ProgressBar progressBar;

    Label getProgress() {
        return progress;
    }

    ProgressBar getProgressBar() {
        return progressBar;
    }
}
