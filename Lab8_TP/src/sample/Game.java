package sample;

import javafx.animation.AnimationTimer;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Game {
    enum Difficulty {EASY,NORMAL,HARD}
    private List<GameLevel> levels;
    private int currentLevelIdx;
    private double width, height;
    private boolean isOver, isLevel;
    private List<GameObject> gameObjects;
    private Field field;
    private List<Bound> bounds;
    private Ball ball;
    private Racket racket;
    private List<Block> blocks;
    private List<Solid> solids;
    private GameGUI gui;
    private Timer timer;
    private AnimationTimer animationTimer;
    private Scene scene;
    private AudioClip backgroundClip, gameOverClip;

    private final double BOUND_THICKNESS = 5;

    Game(Canvas canvas, Difficulty difficulty) {
        timer = new Timer();
        double
            racketWidth = 60, racketHeight = 10, racketSpeed = 4,
            ballRadius = 7, ballSpeedX = 0.5, ballSpeedY = 1;
        switch (difficulty) {
            case EASY: {
                racketWidth *= 1.2;
                racketSpeed *= 2;
                ballSpeedX *= 0.8;
                ballSpeedY *= 0.8;
                break;
            }
            case HARD: {
                racketWidth *= 0.9;
                racketSpeed *= 0.9;
                ballSpeedX *= 1.1;
                ballSpeedY *= 1.1;
                break;
            }
        }
        currentLevelIdx = -1;
        isOver = false; isLevel = false;
        width = canvas.getWidth();
        height = canvas.getHeight();
        field = new Field(width, height, Color.BLACK);
        ball = new Ball(width/2, height-racketHeight-ballRadius, ballRadius,
                Color.RED, ballSpeedX, ballSpeedY);
        racket = new Racket((width-racketWidth)/2,height-racketHeight,
                racketWidth, racketHeight,
                Color.MAGENTA, racketSpeed, BOUND_THICKNESS, width-BOUND_THICKNESS);
        generateBounds();
        blocks = new ArrayList<>();
        solids = new ArrayList<>();
        solids.addAll(bounds);
        solids.add(racket);
        gameObjects = new ArrayList<>();
        gameObjects.add(field);
        gameObjects.addAll(bounds);
        gameObjects.add(ball);
        gameObjects.add(racket);
        gui = new GameGUI(canvas, gameObjects);
        scene = canvas.getScene();
        scene.addEventFilter(KeyEvent.KEY_PRESSED, racket);
        backgroundClip = new AudioClip(new File("res/backgroundTrack.mp3").toURI().toString());
        gameOverClip = new AudioClip(new File("res/gameOver.mp3").toURI().toString());
        generateLevels();
    }

    private void generateLevels() {
        levels = new ArrayList<>();
        try {
            levels.add(new GameLevel(new File("res/level1.txt")));
            levels.add(new GameLevel(new File("res/level2.txt")));
        }
        catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void generateBounds() {
        bounds = new ArrayList<>();
        bounds.add(new Bound(0, 0, width, 5, Color.DARKGRAY));
        bounds.add(new Bound(0, 0, 5, height, Color.DARKGRAY));
        bounds.add(new Bound(width-5, 0, 5, height, Color.DARKGRAY));
    }

    private void setNextLevel() {
        ++currentLevelIdx;
        if (currentLevelIdx == levels.size()) {
            isOver = true;
            return;
        }
        isLevel = true;
        gameObjects.removeAll(blocks);
        blocks = levels.get(currentLevelIdx).getBlocks(width, height, BOUND_THICKNESS);
        gameObjects.addAll(blocks);
        solids.addAll(blocks);
        ball.reset();
        racket.reset();
        Timer levelTimer = new Timer();
        levelTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                isLevel = false;
                startGameTimer();
            }
        }, 1000);
    }

    private void startGameTimer() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                update();
                if (isOver) {
                    cancel();
                    backgroundClip.stop();
                    gameOverClip.play();
                }
            }
        }, 10, 10);
    }

    void start() {
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                gui.repaint();
                if (isOver) {
                    gui.showGameOver();
                    //stop();
                }
                if (isLevel) {
                    gui.showLevel(currentLevelIdx);
                }
            }
        };
        animationTimer.start();
        backgroundClip.setCycleCount(Integer.MAX_VALUE);
        backgroundClip.play();
        setNextLevel();
    }

    void stop() {
        if (timer != null) timer.cancel();
        if (animationTimer != null) animationTimer.stop();
        scene.removeEventFilter(KeyEvent.KEY_PRESSED, racket);
        backgroundClip.stop();
    }

    private void update() {
        ball.updatePos();
        if (ball.getShape().getCenterY() > height + ball.getShape().getRadius()) {
            isOver = true;
            return;
        }
        for (Solid solid : solids) {
            solid.hit(ball);
        }
        boolean noMoreBlocks = true;
        for (Block block : blocks) {
            if (block.isVisible()) {
                noMoreBlocks = false;
                break;
            }
        }
        if (noMoreBlocks) {
            timer.cancel();
            timer = new Timer();
            setNextLevel();
        }
    }
}

class GameGUI {
    private GraphicsContext gc;
    private List<GameObject> gameObjects;
    private Canvas canvas;

    GameGUI(Canvas canvas, List<GameObject> gameObjects) {
        this.canvas = canvas;
        this.gameObjects = gameObjects;
        gc = canvas.getGraphicsContext2D();
    }

    void repaint() {
        for (GameObject gameObject : gameObjects) {
            gameObject.paintSelf(gc);
        }
    }

    void showLevel(int levelIdx) {
        gc.setFill(new Color(0, 0, 0, 0.5));
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFont(new Font(60));
        gc.setTextBaseline(VPos.CENTER);
        gc.setFill(new LinearGradient(0.5, 0, 0.5, 1, true,
                CycleMethod.NO_CYCLE,
                new Stop(0, Color.WHITE), new Stop(1, Color.MAGENTA)));
        gc.fillText("Level "+levelIdx, gc.getCanvas().getWidth()/2, gc.getCanvas().getHeight()/2);
        gc.setStroke(Color.MAGENTA.darker());
        gc.setLineWidth(2);
        gc.strokeText("Level "+levelIdx, gc.getCanvas().getWidth()/2, gc.getCanvas().getHeight()/2);
    }
    void showGameOver() {
        gc.setFill(new Color(0, 0, 0, 0.5));
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFont(new Font(60));
        gc.setTextBaseline(VPos.CENTER);
        gc.setFill(new LinearGradient(0.5, 0, 0.5, 1, true,
                CycleMethod.NO_CYCLE,
                new Stop(0, Color.WHITE), new Stop(1, Color.MAGENTA)));
        gc.fillText("Game Over!", gc.getCanvas().getWidth()/2, gc.getCanvas().getHeight()/2);
        gc.setStroke(Color.MAGENTA.darker());
        gc.setLineWidth(2);
        gc.strokeText("Game Over!", gc.getCanvas().getWidth()/2, gc.getCanvas().getHeight()/2);
    }
}