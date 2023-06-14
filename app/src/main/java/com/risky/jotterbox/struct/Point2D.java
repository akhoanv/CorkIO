package com.risky.jotterbox.struct;

import androidx.annotation.NonNull;

/**
 * Represent a 2D point, can be treated/used as a vector
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

    public void setXY(Point2D point) {
        this.x = point.getX();
        this.y = point.getY();
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

    @NonNull
    @Override
    public String toString() {
        return this.x + "," + this.y;
    }
}
