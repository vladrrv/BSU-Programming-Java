package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class Controller {
    private String log;
    @FXML private TextArea logArea;
    @FXML private Label label;
    @FXML private void initialize() {
        label.setText("");
        log = "";
        logArea.setText(log);
    }
    void updateLog(String s) {
        log += "pressed '"+s+"'\n";
        logArea.setText(log);
        label.setText("");
    }
    void updateLabel(String s) {
        label.setText(s);
    }
}
