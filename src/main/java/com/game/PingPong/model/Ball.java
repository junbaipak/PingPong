package com.game.PingPong.model;

public class Ball {

    private double x;
    private double y;
    private double velocityX;
    private double velocityY;
    private double radius;
    private double speed;

    public Ball(double x, double y, double radius, double speed) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.speed = speed;
        this.velocityX = speed;
        this.velocityY = speed;
    }

    public void update(double canvasWidth, double canvasHeight, Paddle leftPaddle, Paddle rightPaddle) {
        x += velocityX;
        y += velocityY;

        if (y <= radius || y >= canvasHeight - radius) {
            velocityY = -velocityY;
            
        }

        if (x <= leftPaddle.getX() + leftPaddle.getWidth()
                && y >= leftPaddle.getY()
                && y <= leftPaddle.getY() + leftPaddle.getHeight()
                && velocityX < 0) {
            velocityX = -velocityX;
            x = leftPaddle.getX() + leftPaddle.getWidth() + radius;
        }

        if (x >= rightPaddle.getX() - radius
                && y >= rightPaddle.getY()
                && y <= rightPaddle.getY() + rightPaddle.getHeight()
                && velocityX > 0) {
            velocityX = -velocityX;
            x = rightPaddle.getX() - radius;
        }
    }

    public boolean isOutOfBounds(double canvasWidth) {
        return x < 0 || x > canvasWidth;
    }

    public void reset(double canvasWidth, double canvasHeight) {
        x = canvasWidth / 2;
        y = canvasHeight / 2;
        velocityX = Math.random() > 0.5 ? speed : -speed;
        velocityY = Math.random() > 0.5 ? speed : -speed;
    }



    // Getters and setters
    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getVelocityX() {
        return velocityX;
    }

    public void setVelocityX(double velocityX) {
        this.velocityX = velocityX;
    }

    public double getVelocityY() {
        return velocityY;
    }

    public void setVelocityY(double velocityY) {
        this.velocityY = velocityY;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }
}
