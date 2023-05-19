package com.cork.io.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cork.io.R;
import com.cork.io.dao.Note;
import com.cork.io.data.NoteManager;
import com.cork.io.data.ObjectBoxNoteManager;

public class NoteEditSummaryFragment extends Fragment {
    private NoteManager noteManager;
    private View view;
    private Note note;

    private EditText titleElement;
    private EditText contentElement;
    private TextView idElement;
    private ImageView iconElement;

    public NoteEditSummaryFragment(Note note) {
        this.note = note;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        noteManager = ObjectBoxNoteManager.get();
        view = inflater.inflate(R.layout.fragment_note_summary, container, false);

        // Find elements
        titleElement = view.findViewById(R.id.note_edit_title);
        idElement = view.findViewById(R.id.note_edit_id);
        contentElement = view.findViewById(R.id.note_edit_content);
        iconElement = view.findViewById(R.id.note_edit_icon);

        // Assign appropriate data
        titleElement.setText(note.title);
        idElement.setText("ID: " + note.id);
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

        return view;
    }

    @Override
    public void onDestroy() {
        // Update content
        note.title = titleElement.getText().toString();
        note.content = contentElement.getText().toString();
        noteManager.updateNote(note);

        // Set these listener to null, avoid mem leak
        titleElement.setOnFocusChangeListener(null);
        contentElement.setOnFocusChangeListener(null);

        super.onDestroy();
    }
}
