package com.cork.io;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.cork.io.dao.Board;
import com.cork.io.dao.Note;
import com.cork.io.worldobject.BoardFragment;
import com.cork.io.objectbox.ObjectBox;

import io.objectbox.Box;

import java.util.Random;
import java.util.concurrent.CompletableFuture;

public class MainActivity extends FragmentActivity {
    private BoardFragment mainBoard;
    private int currentApiVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        //deleteAllNotes();

        // Initialization UI
        mainBoard = new BoardFragment(this);
        ((ConstraintLayout) findViewById(R.id.app_view)).addView(mainBoard);

        LinearLayout addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(this::addButtonOnClick);
        addButton.bringToFront();
    }

    public void deleteAllNotes() {
        Box<Board> boardBox = ObjectBox.get().boxFor(Board.class);
        boardBox.removeAll();

        Box<Note> noteBox = ObjectBox.get().boxFor(Note.class);
        noteBox.removeAll();
    }

    public void addButtonOnClick(View view) {
        CompletableFuture<Note> dbAddFuture = CompletableFuture.supplyAsync(() -> {
            String title = "Title " + (new Random().nextInt(61) + 20);
            String content = "Content " + (new Random().nextInt(61) + 20);
            return mainBoard.addToDatabase(title, content, R.drawable.icon);
        });

        dbAddFuture.handle((newNote, throwable) -> {
            if (throwable != null) {
                Log.d(this.getLocalClassName(), "Failed to add new note.");
            }

            return newNote;
        }).thenAccept(newNote -> runOnUiThread(() -> mainBoard.renderNote(newNote)));
    }

    @SuppressLint("NewApi")
    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);
        if(currentApiVersion >= Build.VERSION_CODES.KITKAT && hasFocus)
        {
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