package sample;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ComboBox;


public class Controller {
    private Game game;
    @FXML private Canvas canvas;
    @FXML private ComboBox<String> comboBox;

    @FXML private void initialize() {
        comboBox.getItems().addAll("Easy", "Normal", "Hard");
        comboBox.getSelectionModel().clearAndSelect(1);
    }

    void close() {
        endGame();
    }

    private void endGame() {
        if (game != null) game.stop();
    }

    @FXML private void newGame() {
        endGame();
        game = new Game(canvas, Game.Difficulty.valueOf(comboBox.getSelectionModel().getSelectedItem().toUpperCase()));
        game.start();
    }

}
