package com.cork.io;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.cork.io.struct.Point2D;
import com.cork.io.struct.TouchAction;

public class Board extends RelativeLayout {
    private Point2D position = new Point2D(0, 0);
    private Point2D mousePosition = new Point2D(0, 0);
    private TouchAction action;

    public Board(Context context) {
        super(context);
        setOnTouchListener(touchListener);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.board_view, this, true);
    }

    public void moveChild(final Point2D position) {
        for (int i=1; i < getChildCount(); i++) {
            ((Note) getChildAt(i)).move(position);
        }
    }

    private OnTouchListener touchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            float newX = motionEvent.getX();
            float newY = motionEvent.getY();

            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mousePosition = new Point2D(newX, newY);
                    action = TouchAction.DRAG;
                    break;
                case MotionEvent.ACTION_UP:
                    action = TouchAction.NONE;
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (action == TouchAction.DRAG) {
                        float dx = newX - mousePosition.getX();
                        float dy = newY - mousePosition.getY();
                        moveChild(new Point2D(dx, dy));
                        position = new Point2D(position.getX() + dx, position.getY() + dy);
                        mousePosition = new Point2D(newX, newY);
                    }
                    break;
            }
            return true;
        }
    };
}
