package com.cork.io;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.cork.io.struct.Point2D;
import com.cork.io.struct.TouchAction;

public class Note extends RelativeLayout {
    private TouchAction action;
    private TextView titleView;
    private ImageView iconView;

    // Reactive variable
    private Point2D mousePosition = new Point2D(0 ,0);
    private Handler holdHandler = new Handler();
    private Runnable holdRunnable = new Runnable() {
        @Override
        public void run() {
            action = TouchAction.DRAG;
            findViewById(R.id.note_content).setBackgroundResource(R.drawable.note_background_hold);
        }
    };

    public Note(Context context) {
        super(context);
        setOnTouchListener(touchListener);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.small_view_note, this, true);

        iconView = findViewById(R.id.small_view_icon);
        titleView = findViewById(R.id.small_view_title);

        findViewById(R.id.note_content).setBackgroundResource(R.drawable.note_background);
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

    public void scale(final float dscale) {
        setScaleX((getScaleX() * dscale) / 100);
        setScaleY((getScaleY() * dscale) / 100);

        setX((getX() * dscale) / 100);
        setY((getY() * dscale) / 100);
    }

    private OnTouchListener touchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            float newX = motionEvent.getX();
            float newY = motionEvent.getY();

            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    bringToFront();
                    action = TouchAction.CLICK;
                    holdHandler.postDelayed(holdRunnable, 300);
                    mousePosition.setXY(newX, newY);
                    break;
                case MotionEvent.ACTION_UP:
                    if (action == TouchAction.CLICK) {
                        NoteEditFragment fragment = new NoteEditFragment();
                        FragmentTransaction ft = ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction();
                        Fragment prev = ((FragmentActivity) getContext()).getSupportFragmentManager().findFragmentByTag("dialog");
                        if (prev != null) {
                            ft.remove(prev);
                        }
                        ft.addToBackStack(null);
                        fragment.show(ft, "dialog");
                    } else if (action == TouchAction.DRAG) {
                        action = TouchAction.NONE;
                    }

                    holdHandler.removeCallbacks(holdRunnable);

                    findViewById(R.id.note_content).setBackgroundResource(R.drawable.note_background);

                    break;
                case MotionEvent.ACTION_MOVE:
                    if (action == TouchAction.DRAG) {
                        move(new Point2D(newX - mousePosition.getX(), newY - mousePosition.getY()));
                    } else {
                        holdHandler.removeCallbacks(holdRunnable);
                    }
                    break;
            }
            return true;
        }
    };
}
