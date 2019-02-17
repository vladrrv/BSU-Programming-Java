package sample;

import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.io.File;
import java.util.List;

class Bound extends RectObject implements Solid {
    private AudioClip hitClip;

    Bound(double x, double y, double width, double height, Color color) {
        super(x, y, width, height, color);
        hitClip = new AudioClip(new File("res/boundHit.mp3").toURI().toString());
    }

    @Override
    public void hit(Ball ball) {
        Circle c = ball.getShape();
        Rectangle r = getShape();
        double
                speedX = ball.getSpeedX(), speedY = ball.getSpeedY(),
                ballPosX = c.getCenterX() + speedX,
                ballPosY = c.getCenterY() + speedY,
                ballRadius = c.getRadius(), rs = ballRadius * ballRadius,
                bx1 = ballPosX - ballRadius, by1 = ballPosY - ballRadius,
                bx2 = ballPosX + ballRadius, by2 = ballPosY + ballRadius;
        double
                x1 = r.getX(), y1 = r.getY(),
                x2 = x1 + r.getWidth(), y2 = y1 + r.getHeight(),
                x1s = (x1 - ballPosX) * (x1 - ballPosX), y1s = (y1 - ballPosY) * (y1 - ballPosY),
                x2s = (x2 - ballPosX) * (x2 - ballPosX), y2s = (y2 - ballPosY) * (y2 - ballPosY);
        if ((ballPosY <= y2 && ballPosY >= y1) &&
                (speedX > 0 && x2 > bx2 && x1 <= bx2 ||
                        speedX < 0 && x1 < bx1 && x2 >= bx1)) {
            ball.setSpeedX(-speedX);
            hitClip.play();
        } else if ((ballPosX <= x2 && ballPosX >= x1) &&
                (speedY > 0 && y2 > by2 && y1 <= by2 ||
                        speedY < 0 && y1 < by1 && y2 >= by1)) {
            ball.setSpeedY(-speedY);
            hitClip.play();
        } else if (x1s + y1s <= rs || x2s + y1s <= rs || x1s + y2s <= rs || x2s + y2s <= rs) {
            ball.setSpeedX(-speedX);
            ball.setSpeedY(-speedY);
            hitClip.play();
        }
    }
}
