package com.risky.evidencevault.worldobject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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

import com.risky.evidencevault.R;
import com.risky.evidencevault.dao.Board;
import com.risky.evidencevault.dao.Connection;
import com.risky.evidencevault.dao.Note;
import com.risky.evidencevault.data.ObjectBoxBoardManager;
import com.risky.evidencevault.data.ObjectBoxConnectionManager;
import com.risky.evidencevault.data.ObjectBoxNoteManager;
import com.risky.evidencevault.fragment.dialog.NoteEditDialogFragment;
import com.risky.evidencevault.struct.Point2D;
import com.risky.evidencevault.struct.TouchAction;
import com.risky.evidencevault.utils.DeviceProperties;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Note object on {@link BoardFragment}
 *
 * @author knguyen
 */
public class NoteFragment extends RelativeLayout {
    // Database manager
    private ObjectBoxNoteManager noteManager;
    private ObjectBoxBoardManager boardManager;
    private ObjectBoxConnectionManager connectionManager;

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

    public NoteFragment(Context context, final Note note, final boolean isNew) {
        super(context);

        noteManager = ObjectBoxNoteManager.get();
        boardManager = ObjectBoxBoardManager.get();
        connectionManager = ObjectBoxConnectionManager.get();
        setOnTouchListener(touchListener);
        setOnLongClickListener(longClickListener);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.small_view_note, this, true);

        iconView = findViewById(R.id.small_view_icon);
        titleView = findViewById(R.id.small_view_title);

        findViewById(R.id.note_content).setBackgroundResource(R.drawable.note_background);

        this.note = note;

        if (note.title != null) {
            titleView.setText(note.title);
        }

        if (note.customIconPath.isEmpty()) {
            iconView.setImageResource(note.type.getIcon().getId());
        } else {
            try {
                InputStream inputStream = getContext().getContentResolver().openInputStream(Uri.parse(note.customIconPath));
                Bitmap importedImg = BitmapFactory.decodeStream(new BufferedInputStream(inputStream));
                iconView.setImageBitmap(importedImg);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        setX(isNew ? (DeviceProperties.getScreenWidth() / 3) : note.positionX);
        setY(isNew ? (DeviceProperties.getScreenHeight() / 3) : note.positionY);

        scale(boardManager.findById(note.boardId).scaleFactor * 100, false);
    }

    /**
     * Force {@link Note} object associated with this object ot fetch updated data from database
     */
    public void fetchNote() {
        this.note = noteManager.findById(note.id);
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

    /**
     * Remove self from UI and database
     */
    public void remove() {
        // Remove all connection from other nodes
        for (Long connId : note.connection) {
            Connection conn = connectionManager.findById(connId);

            // Remove connection from linked note
            Note n = noteManager.findById(conn.getLinkedNoteId(note.id));
            n.connection.remove(connId);
            noteManager.update(n);

            // Remove connection object
            connectionManager.remove(connId);
        }

        boolean isRemoved = noteManager.remove(note.id);
        if (isRemoved) {
            // Remove UI
            ((ViewGroup)getParent()).removeView(this);
        } else {
            Log.d(NoteFragment.class.getName(), "Failed to remove note.");
        }
    }

    /**
     * Get note object associates with this object
     *
     * @return {@link Note} object
     */
    public Note getNote() {
        return note;
    }

    private OnLongClickListener longClickListener = new OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
            action = TouchAction.DRAG;
            findViewById(R.id.note_content).setBackgroundResource(R.drawable.note_background_hold);

            return true;
        }
    };

    private OnTouchListener touchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            float newX = motionEvent.getX();
            float newY = motionEvent.getY();

            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    // Bring to front of other notes, UI and database
                    bringToFront();
                    Board board = boardManager.findById(note.boardId);
                    board.notes.remove(note.id);
                    board.notes.add(note.id);
                    boardManager.update(board);

                    // Set action for later stages
                    action = TouchAction.CLICK;
                    mousePosition.setXY(newX, newY);

                    return false;
                case MotionEvent.ACTION_UP:
                    if (action == TouchAction.CLICK) {
                        // Close any dialog fragment
                        FragmentTransaction ft = ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction();
                        Fragment prev = ((FragmentActivity) getContext()).getSupportFragmentManager().findFragmentByTag("dialog");
                        if (prev != null) {
                            ft.remove(prev);
                        }

                        // Show edit fragment
                        NoteEditDialogFragment fragment = new NoteEditDialogFragment(note);
                        fragment.setEditCallback((doDelete) -> {
                            if (doDelete) {
                                remove();
                                return;
                            }

                            // Update current note object and data on screen
                            note = noteManager.findById(note.id);

                            if (note.title != null) {
                                titleView.setText(note.title);
                            }

                            if (note.customIconPath.isEmpty()) {
                                iconView.setImageResource(note.type.getIcon().getId());
                            } else {
                                try {
                                    InputStream inputStream = getContext().getContentResolver().openInputStream(Uri.parse(note.customIconPath));
                                    Bitmap importedImg = BitmapFactory.decodeStream(new BufferedInputStream(inputStream));
                                    iconView.setImageBitmap(importedImg);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                            }

                            ((ViewGroup) getParent()).invalidate();
                        });
                        ft.addToBackStack(null);
                        fragment.show(ft, "dialog");
                    } else if (action == TouchAction.DRAG) {
                        action = TouchAction.NONE;
                        note.positionX = getX() - boardManager.findById(note.boardId).panPositionX;
                        note.positionY = getY() - boardManager.findById(note.boardId).panPositionY;
                    }

                    boolean isUpdated = noteManager.update(note);
                    if (isUpdated) {
                        holdHandler.removeCallbacks(holdRunnable);
                        findViewById(R.id.note_content).setBackgroundResource(R.drawable.note_background);
                    }

                    break;
                case MotionEvent.ACTION_MOVE:
                    if (action == TouchAction.DRAG) {
                        move(new Point2D(newX - mousePosition.getX(), newY - mousePosition.getY()));

                        ((ViewGroup) getParent()).invalidate();
                    } else {
                        holdHandler.removeCallbacks(holdRunnable);
                    }
                    break;
            }
            return true;
        }
    };
}
