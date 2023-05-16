package com.cork.io.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.cork.io.R;
import com.cork.io.dao.Note;
import com.cork.io.data.NoteManager;
import com.cork.io.data.ObjectBoxNoteManager;
import com.cork.io.utils.NoteCallback;

/**
 * Fragment for editing note
 *
 * @author knguyen
 */
public class NoteEditFragment extends DialogFragment {
    private NoteManager noteManager;
    private View view;
    private Note note;
    private NoteCallback callback;
    private boolean doDelete = false;

    // Elements
    private EditText titleElement;
    private EditText contentElement;
    private ImageView iconElement;
    private ImageView unpinBtn;

    public NoteEditFragment(Note note) {
        this.note = note;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        noteManager = ObjectBoxNoteManager.get();
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        view = inflater.inflate(R.layout.fragment_note_dialog, container, false);

        // Find elements
        titleElement = view.findViewById(R.id.note_edit_title);
        contentElement = view.findViewById(R.id.note_edit_content);
        iconElement = view.findViewById(R.id.note_edit_icon);
        unpinBtn = view.findViewById(R.id.note_edit_unpin);

        // Assign appropriate data
        titleElement.setText(note.title);
        contentElement.setText(note.content);
        iconElement.setImageResource(note.iconId);

        // Set onChangeListener to update the database
        titleElement.setOnFocusChangeListener((view, hasFocus) -> {
            if (!hasFocus) {
                note.title = titleElement.getText().toString();
                noteManager.updateNote(note);
            }
        });

        contentElement.setOnFocusChangeListener((view, hasFocus) -> {
            if (!hasFocus) {
                note.content = contentElement.getText().toString();
                noteManager.updateNote(note);
            }
        });

        // Unpin button
        unpinBtn.setOnClickListener(view -> {
            doDelete = true;
            this.dismiss();
        });

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean setFullScreen = false;
        if (getArguments() != null) {
            setFullScreen = getArguments().getBoolean("fullScreen");
        }

        if (setFullScreen)
            setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
    }

    public void setCallback(NoteCallback callback) {
        this.callback = callback;
    }

    @Override
    public void onDestroy() {
        // Update content
        note.title = titleElement.getText().toString();
        note.content = contentElement.getText().toString();
        noteManager.updateNote(note);

        // Run callback to update data on board
        callback.run(doDelete);

        // Set these listener to null, avoid mem leak
        titleElement.setOnFocusChangeListener(null);
        contentElement.setOnFocusChangeListener(null);
        unpinBtn.setOnClickListener(null);

        super.onDestroy();
    }
}
