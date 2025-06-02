package com.game.PingPong.model;

public class GameState {

    private Ball ball;
    private Paddle leftPaddle;
    private Paddle rightPaddle;
    private int leftScore;
    private int rightScore;
    private boolean gameRunning;
    private final double CANVAS_WIDTH = 800;
    private final double CANVAS_HEIGHT = 400;

    public GameState() {
        initializeGame();
    }

    private void initializeGame() {
        ball = new Ball(CANVAS_WIDTH / 2, CANVAS_HEIGHT / 2, 10, 3);
        leftPaddle = new Paddle(10, CANVAS_HEIGHT / 2 - 50, 10, 50, 25);
        rightPaddle = new Paddle(CANVAS_WIDTH - 20, CANVAS_HEIGHT / 2 - 50, 10, 50, 25);
        leftScore = 0;
        rightScore = 0;
        gameRunning = true;
    }

    public void update() {
        if (!gameRunning) {
            return;
        }

        ball.update(CANVAS_WIDTH, CANVAS_HEIGHT, leftPaddle, rightPaddle);

        // Check for scoring
        if (ball.isOutOfBounds(CANVAS_WIDTH)) {
            if (ball.getX() < 0) {
                rightScore++;
            } else {
                leftScore++;
            }
            ball.reset(CANVAS_WIDTH, CANVAS_HEIGHT);
        }
    }

    public void moveLeftPaddleUp() {
        leftPaddle.moveUp();
    }

    public void moveLeftPaddleDown() {
        leftPaddle.moveDown(CANVAS_HEIGHT);
    }

    public void moveRightPaddleUp() {
        rightPaddle.moveUp();
    }

    public void moveRightPaddleDown() {
        rightPaddle.moveDown(CANVAS_HEIGHT);
    }

    public Ball getBall() {
        return ball;
    }

    public void setBall(Ball ball) {
        this.ball = ball;
    }

    public Paddle getLeftPaddle() {
        return leftPaddle;
    }

    public void setLeftPaddle(Paddle leftPaddle) {
        this.leftPaddle = leftPaddle;
    }

    public Paddle getRightPaddle() {
        return rightPaddle;
    }

    public void setRightPaddle(Paddle rightPaddle) {
        this.rightPaddle = rightPaddle;
    }

    public int getLeftScore() {
        return leftScore;
    }

    public void setLeftScore(int leftScore) {
        this.leftScore = leftScore;
    }

    public int getRightScore() {
        return rightScore;
    }

    public void setRightScore(int rightScore) {
        this.rightScore = rightScore;
    }

    public boolean isGameRunning() {
        return gameRunning;
    }

    public void setGameRunning(boolean gameRunning) {
        this.gameRunning = gameRunning;
    }

    public double getCanvasWidth() {
        return CANVAS_WIDTH;
    }

    public double getCanvasHeight() {
        return CANVAS_HEIGHT;
    }
}
