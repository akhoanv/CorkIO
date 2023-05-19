package com.cork.io.worldobject;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.cork.io.R;
import com.cork.io.dao.Board;
import com.cork.io.dao.Note;
import com.cork.io.data.BoardManager;
import com.cork.io.data.NoteManager;
import com.cork.io.data.ObjectBoxBoardManager;
import com.cork.io.data.ObjectBoxNoteManager;
import com.cork.io.objectbox.ObjectBox;
import com.cork.io.struct.Point2D;
import com.cork.io.struct.TouchAction;

import io.objectbox.Box;

/**
 * Pannable/Zoomable board for main app
 *
 * @author knguyen
 */
public class BoardFragment extends RelativeLayout {
    // Database manager
    private NoteManager noteManager;
    private BoardManager boardManager;

    // Stats variable
    private Point2D onScreenPosition = new Point2D(0, 0);
    private float scale = 1f;
    private Board board;
    private TouchAction action;

    // Reactive variable
    private Point2D mousePosition = new Point2D(0, 0);
    private float originalDist = 0f;

    public BoardFragment(Context context, int boardIndex) {
        super(context);
        noteManager = ObjectBoxNoteManager.get();
        boardManager = ObjectBoxBoardManager.get();
        setOnTouchListener(touchListener);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.board_view, this, true);

        if (boardManager.getAllBoards().size() == 0) {
            board = boardManager.addBoard(new Board());
        } else {
            board = boardManager.getAllBoards().get(boardIndex);
            // Render all child
            for (Note n : board.notes) {
                renderNote(n, false);
            }

            onScreenPosition.setXY(board.panPositionX, board.panPositionY);
            scale = board.scaleFactor;

            // Move screen position
            moveChildOnScreen(onScreenPosition);
        }
    }

    /**
     * Attempting to add a new {@link Note} entry into the database
     */
    public Note addToDatabase(String title, String content, int imageResource) {
        Note note = new Note(board.id, title, content, imageResource, board.panPositionX, board.panPositionY);
        noteManager.addNote(note);
        board.notes.add(note);
        boardManager.updateBoard(board);
        return note;
    }

    /**
     * Render note on screen, once the note was successfully added
     */
    public void renderNote(Note note, boolean isNew) {
        NoteFragment noteFragment = new NoteFragment(getContext());
        noteFragment.setNote(note, isNew);
        addView(noteFragment);
    }

    /**
     * Move all child elements in one direction
     *
     * @param position vector amount to move child elements
     */
    private void moveChildOnScreen(final Point2D position) {
        for (int i=1; i < getChildCount(); i++) {
            ((NoteFragment) getChildAt(i)).move(position);
        }
    }

    /**
     * Scale all child elements
     *
     * @param scaling scale factor
     */
    private void scaleChild(final float scaling) {
        for (int i=1; i < getChildCount(); i++) {
            ((NoteFragment) getChildAt(i)).scale(scaling, true);
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
                    if (action == TouchAction.ZOOM) {
                        board.scaleFactor = scale;
                    } else if (action == TouchAction.DRAG) {
                        board.panPositionX = onScreenPosition.getX();
                        board.panPositionY = onScreenPosition.getY();
                    }

                    action = TouchAction.NONE;

                    boardManager.updateBoard(board);
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (action == TouchAction.ZOOM) {
                        // Calculate % changes from last pinch
                        float dscale = ((getPinchDistance(motionEvent) * 100) / originalDist);
                        scaleChild(dscale);

                        // Update initial scale and dist for next move
                        scale = (dscale * scale) / 100;
                        board.scaleFactor = scale;
                        originalDist = getPinchDistance(motionEvent);
                    } else if (action == TouchAction.DRAG) {
                        float dx = newX - mousePosition.getX();
                        float dy = newY - mousePosition.getY();

                        moveChildOnScreen(new Point2D(dx, dy));
                        onScreenPosition.setXY(onScreenPosition.getX() + dx, onScreenPosition.getY() + dy);
                        board.panPositionX = onScreenPosition.getX();
                        board.panPositionY = onScreenPosition.getY();

                        // Update initial mouse position for next move
                        mousePosition.setXY(newX, newY);
                    }
                    break;
            }
            return true;
        }
    };

    /**
     * Calculate distance between two fingers while zooming
     *
     * @param event motion event
     * @return distance between two fingers
     */
    private float getPinchDistance(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }
}
