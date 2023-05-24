package com.cork.io;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.cork.io.dao.Board;
import com.cork.io.dao.Note;
import com.cork.io.data.NoteManager;
import com.cork.io.data.ObjectBoxConnectionManager;
import com.cork.io.data.ObjectBoxNoteManager;
import com.cork.io.fragment.NoteEditFragment;
import com.cork.io.worldobject.BoardFragment;
import com.cork.io.objectbox.ObjectBox;

import java.util.concurrent.CompletableFuture;

import io.objectbox.Box;

public class MainActivity extends FragmentActivity {
    private BoardFragment mainBoard;
    private NoteManager noteManager;
    private int currentApiVersion;

    private SeekBar zoomBar;
    private TextView coordDisplay;
    private ImageButton addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inject variables
        noteManager = ObjectBoxNoteManager.get();

        // Set fullscreen mode
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        // Hide navigation bar
        currentApiVersion = android.os.Build.VERSION.SDK_INT;

        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;


        getWindow().getDecorView().setSystemUiVisibility(flags);

        // Code below is to handle presses of Volume up or Volume down.
        // Without this, after pressing volume buttons, the navigation bar will
        // show up and won't hide
        final View decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(visibility -> {
            if((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0)
            {
                decorView.setSystemUiVisibility(flags);
            }
        });

        deleteAllNotes();

        // Find elements
        zoomBar = findViewById(R.id.zoom_level);
        coordDisplay = findViewById(R.id.xy_position);
        addButton = findViewById(R.id.addButton);

        // Initialization UI
        mainBoard = new BoardFragment(this, 0);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        ((ConstraintLayout) findViewById(R.id.app_view)).addView(mainBoard, lp);

        // Set properties
        LinearLayout boardInfo = findViewById(R.id.boardInfo);
        boardInfo.bringToFront();

        zoomBar.setMax(15);
        zoomBar.setMin(7);
        zoomBar.setOnTouchListener((view, motionEvent) -> true); // Temporary disable drag

        addButton.setOnClickListener(this::addButtonOnClick);
        addButton.bringToFront();
    }

    /**
     * Update Zoom level on UI. Should take values from 7 to 15
     *
     * @param level level to update
     */
    public void updateZoom (int level) {
        zoomBar.setProgress(level);
    }

    /**
     * Update coordinate display on UI
     *
     * @param x x position
     * @param y y position
     */
    public void setCoordDisplay(int x, int y) {
        coordDisplay.setText(x + ":" + y);
    }

    public void deleteAllNotes() {
        Box<Board> boardBox = ObjectBox.get().boxFor(Board.class);
        boardBox.removeAll();

        ObjectBoxConnectionManager.get().removeAllConnection();

        noteManager.removeAllNotes();
    }

    public void addButtonOnClick(View view) {
        CompletableFuture<Note> dbAddFuture = CompletableFuture.supplyAsync(() -> mainBoard.addToDatabase());

        dbAddFuture.handle((newNote, throwable) -> {
            if (throwable != null) {
                Log.d(this.getLocalClassName(), "Failed to add new note.");
            }

            return newNote;
        }).thenAccept(newNote -> runOnUiThread(() -> mainBoard.renderNote(newNote, true)));
    }

    @SuppressLint("NewApi")
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (currentApiVersion >= Build.VERSION_CODES.KITKAT && hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }
}