package com.cork.io.worldobject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.cork.io.MainActivity;
import com.cork.io.R;
import com.cork.io.dao.Board;
import com.cork.io.dao.Note;
import com.cork.io.data.BoardManager;
import com.cork.io.data.ConnectionManager;
import com.cork.io.data.NoteManager;
import com.cork.io.data.ObjectBoxBoardManager;
import com.cork.io.data.ObjectBoxConnectionManager;
import com.cork.io.data.ObjectBoxNoteManager;
import com.cork.io.objectbox.ObjectBox;
import com.cork.io.struct.NoteType;
import com.cork.io.struct.Point2D;
import com.cork.io.struct.TouchAction;

import java.util.ArrayList;
import java.util.List;

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
    private ConnectionManager connectionManager;

    // Stats variable
    private Context context;
    private Point2D onScreenPosition = new Point2D(0, 0);
    private float scale = 1f;
    private Board board;
    private TouchAction action;

    // Reactive variable
    private Point2D mousePosition = new Point2D(0, 0);
    private float originalDist = 0f;

    public BoardFragment(Context context, int boardIndex) {
        super(context);
        this.context = context;

        setWillNotDraw(false);

        noteManager = ObjectBoxNoteManager.get();
        boardManager = ObjectBoxBoardManager.get();
        connectionManager = ObjectBoxConnectionManager.get();

        setBackgroundColor(context.getColor(R.color.board_black));
        setOnTouchListener(touchListener);

        if (boardManager.getAllBoards().size() == 0) {
            board = boardManager.addBoard(new Board());

            // Change UI display stat
            ((MainActivity) context).setCoordDisplay(0, 0);
            ((MainActivity) context).updateZoom(10);
        } else {
            board = boardManager.getAllBoards().get(boardIndex);
            // Render all child
            for (Long id : board.notes) {
                renderNote(noteManager.findNoteById(id), false);
            }

            onScreenPosition.setXY(board.panPositionX, board.panPositionY);
            scale = board.scaleFactor;

            // Move screen position
            moveChildOnScreen(onScreenPosition);

            // Change UI display stat
            ((MainActivity) context).setCoordDisplay((int) -onScreenPosition.getX(), (int) onScreenPosition.getY());
            ((MainActivity) context).updateZoom((int) (scale * 10));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Already visited node, prevent drawing duplication
        List<Long> drawnNote = new ArrayList<>();

        // Compare node to node, draw line if requirements are met
        for (int i=0; i < getChildCount(); i++) {
            NoteFragment noteObject = ((NoteFragment) getChildAt(i));
            noteObject.fetchNote();
            Note n = noteObject.getNote();
            for (int i2=0; i2 < getChildCount(); i2++) {
                NoteFragment linkedObject = ((NoteFragment) getChildAt(i2));

                // Only draw if connection hasn't been drawn and connection exists
                if (drawnNote.contains(linkedObject.getNote().id) || !n.getLinkedNotes().contains(linkedObject.getNote().id)) {
                    continue;
                }

                Paint paint = connectionManager.findConnectionById(
                        n.getConnectionIdFromLinkedNote(linkedObject.getNote().id)).color.getPaint(context);

                Rect startNoteBound = new Rect();
                noteObject.getHitRect(startNoteBound);

                Rect endNoteBound = new Rect();
                linkedObject.getHitRect(endNoteBound);

                float startX = startNoteBound.left + (210 * scale);
                float startY = startNoteBound.top + (40 * scale);

                float endX = endNoteBound.left + (210 * scale);
                float endY = endNoteBound.top + (40 * scale);

                canvas.drawLine(startX, startY, endX, endY, paint);
            }

            drawnNote.add(n.id);
        }
    }

    /**
     * Attempting to add a new {@link Note} entry into the database
     */
    public Note addToDatabase() {
        Note note = new Note(board.id, NoteType.GENERIC, board.panPositionX, board.panPositionY);
        noteManager.addNote(note);
        board.notes.add(note.id);
        boardManager.updateBoard(board);
        return note;
    }

    /**
     * Render note on screen, once the note was successfully added
     */
    public void renderNote(Note note, boolean isNew) {
        NoteFragment noteFragment = new NoteFragment(getContext(), note, isNew);
        addView(noteFragment);
    }

    /**
     * Move all child elements in one direction
     *
     * @param position vector amount to move child elements
     */
    private void moveChildOnScreen(final Point2D position) {
        for (int i=0; i < getChildCount(); i++) {
            ((NoteFragment) getChildAt(i)).move(position);
        }
    }

    /**
     * Scale all child elements
     *
     * @param scaling scale factor
     */
    private void scaleChild(final float scaling) {
        for (int i=0; i < getChildCount(); i++) {
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
                        float scale = (dscale * BoardFragment.this.scale) / 100;

                        if (scale <= 1.5 && scale >= 0.7) {
                            scaleChild(dscale);
                            BoardFragment.this.scale = scale;
                            board.scaleFactor = scale;
                        }

                        // Update initial scale and dist for next move
                        originalDist = getPinchDistance(motionEvent);

                        // Update draw
                        invalidate();

                        // Update screen display
                        ((MainActivity) context).updateZoom((int) (scale * 10));
                    } else if (action == TouchAction.DRAG) {
                        float dx = newX - mousePosition.getX();
                        float dy = newY - mousePosition.getY();

                        moveChildOnScreen(new Point2D(dx, dy));
                        onScreenPosition.setXY(onScreenPosition.getX() + dx, onScreenPosition.getY() + dy);
                        board.panPositionX = onScreenPosition.getX();
                        board.panPositionY = onScreenPosition.getY();

                        // Update initial mouse position for next move
                        mousePosition.setXY(newX, newY);

                        // Update draw
                        invalidate();

                        // Update screen display
                        ((MainActivity) context).setCoordDisplay((int) -onScreenPosition.getX(), (int) onScreenPosition.getY());
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
