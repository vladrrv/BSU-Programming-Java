package sample;

import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;

abstract class KeyObserver implements EventHandler<KeyEvent> {
    protected Controller controller;

    KeyObserver(Controller controller) {
        this.controller = controller;
    }
}

class OnPressed extends KeyObserver {
    OnPressed(Controller controller) {
        super(controller);
    }

    @Override
    public void handle(KeyEvent event) {
        controller.updateLabel(event.getText());
    }
}

class OnReleased extends KeyObserver {
    OnReleased(Controller controller) {
        super(controller);
    }

    @Override
    public void handle(KeyEvent event) {
        controller.updateLog(event.getText());
    }
}
