package com.cork.io;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.cork.io.struct.Point2D;
import com.cork.io.struct.TouchAction;

public class Board extends RelativeLayout {
    private Point2D position = new Point2D(0, 0);
    private float scale = 1f;
    private TouchAction action;

    // Reactive variable
    private Point2D mousePosition = new Point2D(0, 0);
    private float originalDist = 0f;

    public Board(Context context) {
        super(context);
        setOnTouchListener(touchListener);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.board_view, this, true);
    }

    private void moveChild(final Point2D position) {
        for (int i=1; i < getChildCount(); i++) {
            ((Note) getChildAt(i)).move(position);
        }
    }

    private void scaleChild(final float scaling) {
        for (int i=1; i < getChildCount(); i++) {
            ((Note) getChildAt(i)).scale(scaling);
        }
    }

    private OnTouchListener touchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            float newX = motionEvent.getX();
            float newY = motionEvent.getY();

            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    // Store initial mouse position
                    mousePosition.setXY(newX, newY);

                    action = TouchAction.DRAG;
                    break;
                case MotionEvent.ACTION_POINTER_2_DOWN:
                    // Store initial dist between 2 fingers
                    originalDist = getPinchDistance(motionEvent);

                    action = TouchAction.ZOOM;
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_POINTER_2_UP:
                    action = TouchAction.NONE;
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (action == TouchAction.ZOOM) {
                        // Calculate % changes from last pinch
                        float dscale = ((getPinchDistance(motionEvent) * 100) / originalDist);
                        scaleChild(dscale);

                        // Update initial scale and dist for next move
                        scale = (dscale * scale) / 100;
                        originalDist = getPinchDistance(motionEvent);
                    } else if (action == TouchAction.DRAG) {
                        float dx = newX - mousePosition.getX();
                        float dy = newY - mousePosition.getY();

                        moveChild(new Point2D(dx, dy));
                        position = new Point2D(position.getX() + dx, position.getY() + dy);

                        // Update initial mouse position for next move
                        mousePosition.setXY(newX, newY);
                    }
                    break;
            }
            return true;
        }
    };

    private float getPinchDistance(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }
}
