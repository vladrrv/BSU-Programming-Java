package sample;

import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.io.File;
import java.util.List;

class Racket extends RectObject implements Solid, EventHandler<KeyEvent> {
    private double moveSpeed, minWidth, maxWidth, initMoveSpeed, initX, initY;
    private AudioClip moveClip, hitClip;

    Racket(double x, double y, double width, double height, Color color, double moveSpeed, double minWidth, double maxWidth) {
        super(x, y, width, height, color);
        initX = x; initY = y; initMoveSpeed = moveSpeed;
        this.moveSpeed = moveSpeed;
        this.minWidth = minWidth;
        this.maxWidth = maxWidth;
        moveClip = new AudioClip(new File("res/racketMove.mp3").toURI().toString());
        hitClip = new AudioClip(new File("res/racketHit.mp3").toURI().toString());
    }

    void reset() {
        Rectangle r = getShape();
        r.setX(initX);
        r.setY(initY);
        moveSpeed = initMoveSpeed;
    }

    private void moveLeft() {
        Rectangle r = getShape();
        double newX = r.getX() - moveSpeed;
        if (newX >= minWidth) r.setX(newX);
    }
    private void moveRight() {
        Rectangle r = getShape();
        double newX = r.getX() + moveSpeed;
        if (newX < maxWidth-r.getWidth()) r.setX(newX);
    }

    @Override
    public void hit(Ball ball) {
        Circle c = ball.getShape();
        Rectangle r = getShape();
        double
                speedX = ball.getSpeedX(), speedY = ball.getSpeedY(),
                ballPosX = c.getCenterX() + speedX,
                ballPosY = c.getCenterY() + speedY,
                ballRadius = c.getRadius(), rs = ballRadius*ballRadius,
                bx1 = ballPosX - ballRadius, by1 = ballPosY - ballRadius,
                bx2 = ballPosX + ballRadius, by2 = ballPosY + ballRadius;
        double
                x1 = r.getX(), y1 = r.getY(),
                x2 = x1+r.getWidth(), y2 = y1+r.getHeight(),
                x1s = (x1-ballPosX)*(x1-ballPosX), y1s = (y1-ballPosY)*(y1-ballPosY),
                x2s = (x2-ballPosX)*(x2-ballPosX), y2s = (y2-ballPosY)*(y2-ballPosY);
        if ( (ballPosY <= y2 && ballPosY >= y1) &&
                (speedX > 0 && x2 > bx2 && x1 <= bx2 ||
                        speedX < 0 && x1 < bx1 && x2 >= bx1) ) {
            ball.setSpeedX(-speedX);
            hitClip.play();
        } else if ( (ballPosX <= x2 && ballPosX >= x1) &&
                (speedY > 0 && y2 > by2 && y1 <= by2 ||
                        speedY < 0 && y1 < by1 && y2 >= by1) ) {
            ball.setSpeedY(-speedY);
            hitClip.play();
        } else if (x1s + y1s <= rs || x2s + y1s <= rs) {
            ball.setSpeedX(-speedX);
            ball.setSpeedY(-speedY);
            hitClip.play();
        }
    }

    @Override
    public void handle(KeyEvent event) {
        switch (event.getCode()) {
            case LEFT: {
                moveLeft();
                moveClip.play();
                break;
            }
            case RIGHT: {
                moveRight();
                moveClip.play();
                break;
            }
        }
    }
}
