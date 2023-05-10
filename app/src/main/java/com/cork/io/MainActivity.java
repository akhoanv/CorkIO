package com.cork.io;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {
    private Board mainBoard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set fullscreen mode
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        mainBoard = new Board(this);
        ((LinearLayout) findViewById(R.id.app_view)).addView(mainBoard);

        addNote("Note", R.drawable.note);
        addNote("Note2", R.drawable.note);
    }

    public void addNote(final String title, final int imageResource) {
        Note l = new Note(this);

        if (title != null) {
            l.setTitle(title);
        }

        if (imageResource != 0) {
            l.setIcon(imageResource);
        }

        mainBoard.addView(l);
    }
}