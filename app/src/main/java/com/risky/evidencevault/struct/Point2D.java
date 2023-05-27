package com.risky.evidencevault.struct;

/**
 * Represent 2D point
 *
 * @author knguyen
 */
public class Point2D {
    private float x;
    private float y;

    public Point2D(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setXY(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void moveX(float amount) {
        this.x = this.x + amount;
    }

    public void moveY(float amount) {
        this.y = this.y + amount;
    }
}
