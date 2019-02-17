package sample;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

abstract class GameObject {
    private Shape shape;
    private boolean visible;

    GameObject(Shape shape) {
        this.shape = shape;
        visible = true;
    }

    Shape getShape() {
        return shape;
    }

    boolean isVisible() {
        return visible;
    }
    void setVisible(boolean visible) {
        this.visible = visible;
    }

    abstract void paintSelf(GraphicsContext gc);
}

abstract class RectObject extends GameObject {
    RectObject(double x, double y, double width, double height, Color color) {
        super(new Rectangle(x, y, width, height));
        Rectangle r = getShape();
        r.setFill(new LinearGradient(0, 1, 1, 0, true,
                CycleMethod.NO_CYCLE,
                new Stop(0, color.deriveColor(-50, 0.7, 1.8, 1)),
                new Stop(0.7, color), new Stop(1, color.brighter())));
        r.setStroke(color.darker());
    }

    @Override
    Rectangle getShape() {
        return (Rectangle) super.getShape();
    }
    @Override
    void paintSelf(GraphicsContext gc) {
        if (isVisible()) {
            Rectangle r = getShape();
            double
                    x1 = r.getX(), w = r.getWidth(),
                    y1 = r.getY(), h = r.getHeight();
            gc.setFill(r.getFill());
            gc.fillRect(x1, y1, w, h);
            gc.setStroke(r.getStroke());
            gc.strokeRect(x1, y1, w, h);
        }
    }
}


