package com.cork.io;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
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

import com.cork.io.dao.Note;
import com.cork.io.data.NoteManager;
import com.cork.io.data.ObjectBoxNoteManager;
import com.cork.io.fragment.dialog.SelectNoteTypeDialogFragment;
import com.cork.io.utils.DeviceProperties;
import com.cork.io.worldobject.BoardFragment;
import com.cork.io.objectbox.ObjectBox;

import java.util.concurrent.CompletableFuture;

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
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        View decorView = getWindow().getDecorView();
        // Hide both the navigation bar and the status bar.
        // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
        // a general rule, you should design your app to hide the status bar whenever you
        // hide the navigation bar.
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);

        // Inject variables
        noteManager = ObjectBoxNoteManager.get();

        setContentView(R.layout.activity_main);

        deleteAllNotes();

        // Find elements
        zoomBar = findViewById(R.id.zoom_level);
        coordDisplay = findViewById(R.id.xy_position);
        addButton = findViewById(R.id.addButton);

        // Initialization UI
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        DeviceProperties.setScreenSize(displayMetrics.widthPixels, displayMetrics.heightPixels);

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
        ObjectBox.get().removeAllObjects();
    }

    public void addButtonOnClick(View view) {

        // Close any dialog fragment
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }

        // Show note type select dialog fragment
        SelectNoteTypeDialogFragment fragment = new SelectNoteTypeDialogFragment(type -> {
            CompletableFuture<Note> dbAddFuture = CompletableFuture.supplyAsync(() -> mainBoard.addToDatabase(type));

            dbAddFuture.handle((newNote, throwable) -> {
                if (throwable != null) {
                    Log.d(this.getLocalClassName(), "Failed to add new note.");
                    throwable.printStackTrace();
                }

                return newNote;
            }).thenAccept(newNote -> runOnUiThread(() -> mainBoard.renderNote(newNote, true)));
        });
        ft.addToBackStack(null);
        fragment.show(ft, "dialog");
    }
}