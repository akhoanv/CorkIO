package com.cork.io.worldobject;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.cork.io.R;
import com.cork.io.dao.Board;
import com.cork.io.dao.Note;
import com.cork.io.data.BoardManager;
import com.cork.io.data.NoteManager;
import com.cork.io.data.ObjectBoxBoardManager;
import com.cork.io.data.ObjectBoxNoteManager;
import com.cork.io.fragment.NoteEditFragment;
import com.cork.io.objectbox.ObjectBox;
import com.cork.io.struct.Point2D;
import com.cork.io.struct.TouchAction;

/**
 * Note object on {@link BoardFragment}
 *
 * @author knguyen
 */
public class NoteFragment extends RelativeLayout {
    // Database manager
    private NoteManager noteManager;
    private BoardManager boardManager;

    // Stats variable
    private TouchAction action;
    private TextView titleView;
    private ImageView iconView;
    private Note note;

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

    public NoteFragment(Context context) {
        super(context);
        noteManager = ObjectBoxNoteManager.get();
        boardManager = ObjectBoxBoardManager.get();
        setOnTouchListener(touchListener);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.small_view_note, this, true);

        iconView = findViewById(R.id.small_view_icon);
        titleView = findViewById(R.id.small_view_title);

        findViewById(R.id.note_content).setBackgroundResource(R.drawable.note_background);
    }

    public void setNote(final Note note, final boolean isNew) {
        this.note = note;

        if (note.title != null) {
            titleView.setText(note.title);
        }

        if (note.iconId != 0) {
            iconView.setImageResource(note.iconId);
        }

        setX(isNew ? 0 : note.positionX);
        setY(isNew ? 0 : note.positionY);

        scale(boardManager.findBoardById(note.boardId).scaleFactor * 100, false);
    }

    /**
     * Move this object, incremental from its current position
     *
     * @param position amount to move
     */
    public void move(final Point2D position) {
        setX(getX() + position.getX());
        setY(getY() + position.getY());
    }

    /**
     * Scale this object
     *
     * @param dscale percentage
     */
    public void scale(final float dscale, final boolean changePosition) {
        setScaleX((getScaleX() * dscale) / 100);
        setScaleY((getScaleY() * dscale) / 100);

        if (changePosition) {
            setX((getX() * dscale) / 100);
            setY((getY() * dscale) / 100);
        }
    }

    public void remove() {
        boolean isRemoved = noteManager.removeNote(note.id);
        if (isRemoved) {
            ((ViewGroup)getParent()).removeView(this);
        } else {
            Log.d(NoteFragment.class.getName(), "Failed to remove note.");
        }
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
                    holdHandler.postDelayed(holdRunnable, 200);
                    mousePosition.setXY(newX, newY);
                    break;
                case MotionEvent.ACTION_UP:
                    if (action == TouchAction.CLICK) {
                        // Close any dialog fragment
                        FragmentTransaction ft = ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction();
                        Fragment prev = ((FragmentActivity) getContext()).getSupportFragmentManager().findFragmentByTag("dialog");
                        if (prev != null) {
                            ft.remove(prev);
                        }

                        // Show edit fragment
                        NoteEditFragment fragment = new NoteEditFragment(note);
                        fragment.setCallback((doDelete) -> {
                            if (doDelete) {
                                remove();
                                return;
                            }

                            // Update current note object and data on screen
                            note = noteManager.findNoteById(note.id);

                            if (note.title != null) {
                                titleView.setText(note.title);
                            }

                            if (note.iconId != 0) {
                                iconView.setImageResource(note.iconId);
                            }
                        });
                        ft.addToBackStack(null);
                        fragment.show(ft, "dialog");
                    } else if (action == TouchAction.DRAG) {
                        action = TouchAction.NONE;
                        note.positionX = getX() - boardManager.findBoardById(note.boardId).panPositionX;
                        note.positionY = getY() - boardManager.findBoardById(note.boardId).panPositionY;
                    }

                    boolean isUpdated = noteManager.updateNote(note);
                    if (isUpdated) {
                        holdHandler.removeCallbacks(holdRunnable);
                        findViewById(R.id.note_content).setBackgroundResource(R.drawable.note_background);
                    }

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
