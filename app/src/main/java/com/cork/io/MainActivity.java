package com.cork.io;

import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.cork.io.dao.Note;
import com.cork.io.fragment.BoardFragment;
import com.cork.io.fragment.NoteFragment;
import com.cork.io.objectbox.ObjectBox;

import java.util.List;

import io.objectbox.Box;

import java.util.Random;

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

        // Initialization
        mainBoard = new BoardFragment(this);
        ((RelativeLayout) findViewById(R.id.app_view)).addView(mainBoard);

        List<Note> notes = retrieveNotes();
        notes.forEach(this::renderNote);

        renderAddButton();
    }

    public void addNoteInternal(String title, String content, int imageResource) {
        Box<Note> noteBox = ObjectBox.get().boxFor(Note.class);
        noteBox.put(new Note(0, title, content, imageResource));
    }

    public List<Note> retrieveNotes() {
        Box<Note> noteBox = ObjectBox.get().boxFor(Note.class);
        return noteBox.getAll();
    }

    public void renderNote(Note note) {
        NoteFragment noteFragment = new NoteFragment(this);

        if (note.title != null) {
            noteFragment.setTitle(note.title);
        }

        if (note.iconId != 0) {
            noteFragment.setIcon(note.iconId);
        }

        mainBoard.addView(noteFragment);
    }

    public void renderAddButton() {
        DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();

        ImageButton addButton = new ImageButton(this);
        addButton.setBackgroundColor(Color.TRANSPARENT);
        addButton.setImageResource(R.drawable.add);
        addButton.setX(displayMetrics.widthPixels - 250);
        addButton.setY(displayMetrics.heightPixels - 150);
        ((RelativeLayout) findViewById(R.id.app_view)).addView(addButton);
        addButton.setOnClickListener(this::addButtonOnClick);
    }

    public void addButtonOnClick(View view) {
        String title = "Title " + (new Random().nextInt(61) + 20);
        String content = "Content " + (new Random().nextInt(61) + 20);
        addNoteInternal( title, content, R.drawable.icon);
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