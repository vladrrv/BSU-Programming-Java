package sample;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;

import java.util.ArrayDeque;
import java.util.Deque;

class Ball extends GameObject {
    private double speedX, speedY, initSpeedX, initSpeedY, initX, initY;
    private Deque<Point2D> trace;
    private int traceLen = 15, counter = 0;
    private Color color;

    Ball(double x, double y, double radius, Color color, double speedX, double speedY) {
        super(new Circle(x, y, radius));
        initX = x; initY = y;
        initSpeedX = speedX; initSpeedY = speedY;
        Circle c = getShape();
        c.setFill(new RadialGradient(0, 0.1, 0.3, 0.3, 0.4, true,
                CycleMethod.NO_CYCLE,
                new Stop(0, color.deriveColor(10, 0.5, 20, 1)), new Stop(1, color)));
        this.color = color;
        this.speedX = speedX;
        this.speedY = speedY;
        trace = new ArrayDeque<>();
    }

    double getSpeedX() {
        return speedX;
    }
    double getSpeedY() {
        return speedY;
    }
    void setSpeedX(double speedX) {
        this.speedX = speedX;
    }
    void setSpeedY(double speedY) {
        this.speedY = speedY;
    }

    void updatePos() {
        Circle c = getShape();
        double x = c.getCenterX(), y = c.getCenterY();
        if (++counter == 5) {
            if (trace.size() == traceLen) {
                trace.poll();
            }
            trace.add(new Point2D(x, y));
            counter = 0;
        }
        c.setCenterX(x + speedX);
        c.setCenterY(y + speedY);
    }

    void reset() {
        Circle c = getShape();
        c.setCenterX(initX);
        c.setCenterY(initY);
        speedX = initSpeedX;
        speedY = initSpeedY;
    }

    @Override
    Circle getShape() {
        return (Circle) super.getShape();
    }
    @Override
    void paintSelf(GraphicsContext gc) {
        Circle c = getShape();
        double radius = c.getRadius();
        double d = 1.0 / traceLen, s = 0;
        for (Point2D p : trace) {
            s += d;
            gc.setFill(color.deriveColor(0, 0.7, 0.4, s));
            gc.fillOval(p.getX() - radius*0.75, p.getY() - radius*0.75, radius*1.5, radius*1.5);
        }
        gc.setFill(c.getFill());
        gc.fillOval(c.getCenterX() - radius, c.getCenterY() - radius, radius*2, radius*2);
    }
}
