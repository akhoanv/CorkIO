package com.cork.io;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cork.io.struct.Point2D;
import com.cork.io.struct.TouchAction;

public class Note extends LinearLayout {
    private TouchAction action;
    private TextView titleView;
    private ImageView iconView;

    public Note(Context context) {
        super(context);
        setOnTouchListener(touchListener);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.small_view_note, this, true);

        iconView = findViewById(R.id.small_view_icon);
        titleView = findViewById(R.id.small_view_title);
    }

    public void setTitle(final String title) {
        titleView.setText(title);
    }

    public void setIcon(final int imageResource) {
        iconView.setImageResource(imageResource);
    }

    public void move(final Point2D position) {
        setX(getX() + position.getX());
        setY(getY() + position.getY());
    }

    private OnTouchListener touchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            float newX = motionEvent.getX();
            float newY = motionEvent.getY();

            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    action = TouchAction.DRAG;
                    break;
                case MotionEvent.ACTION_UP:
                    action = TouchAction.NONE;
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (action == TouchAction.DRAG) {
                        move(new Point2D(newX - (getWidth() / 2), newY - (getHeight() / 2)));
                    }
                    break;
            }
            return true;
        }
    };
}
