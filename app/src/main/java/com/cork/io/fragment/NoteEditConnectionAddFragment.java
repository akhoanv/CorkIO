package com.cork.io.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.cork.io.R;
import com.cork.io.dao.Connection;
import com.cork.io.dao.Note;
import com.cork.io.data.ConnectionManager;
import com.cork.io.data.NoteManager;
import com.cork.io.data.ObjectBoxConnectionManager;
import com.cork.io.data.ObjectBoxNoteManager;
import com.cork.io.struct.ElementColor;

public class NoteEditConnectionAddFragment extends Fragment {
    private NoteManager noteManager;
    private ConnectionManager connectionManager;

    private View view;
    private Note sourceNote;
    private Note linkedNote;

    private ElementColor selectedColor = ElementColor.BLUE;

    private ImageView blueBox;
    private ImageView greenBox;
    private ImageView orangeBox;
    private ImageView pinkBox;
    private ImageView redBox;
    private ImageView yellowBox;
    private EditText nameBox;

    public NoteEditConnectionAddFragment(Note sourceNote, Note linkedNote) {
        this.sourceNote = sourceNote;
        this.linkedNote = linkedNote;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        noteManager = ObjectBoxNoteManager.get();
        connectionManager = ObjectBoxConnectionManager.get();
        view = inflater.inflate(R.layout.fragment_note_select_add, container, false);

        // Find element
        nameBox = view.findViewById(R.id.note_edit_connection_name_box);

        blueBox = view.findViewById(R.id.note_edit_color_blue);
        greenBox = view.findViewById(R.id.note_edit_color_green);
        orangeBox = view.findViewById(R.id.note_edit_color_orange);
        pinkBox = view.findViewById(R.id.note_edit_color_pink);
        redBox = view.findViewById(R.id.note_edit_color_red);
        yellowBox = view.findViewById(R.id.note_edit_color_yellow);

        ImageView colorCheckmark = view.findViewById(R.id.note_edit_color_selection);

        // Set listeners
        blueBox.setOnClickListener(view1 -> {
            selectedColor = ElementColor.BLUE;

            colorCheckmark.setX(blueBox.getX());
            colorCheckmark.setY(blueBox.getY());
        });

        greenBox.setOnClickListener(view1 -> {
            selectedColor = ElementColor.GREEN;

            colorCheckmark.setX(greenBox.getX());
            colorCheckmark.setY(greenBox.getY());
        });

        orangeBox.setOnClickListener(view1 -> {
            selectedColor = ElementColor.ORANGE;

            colorCheckmark.setX(orangeBox.getX());
            colorCheckmark.setY(orangeBox.getY());
        });

        pinkBox.setOnClickListener(view1 -> {
            selectedColor = ElementColor.PINK;

            colorCheckmark.setX(pinkBox.getX());
            colorCheckmark.setY(pinkBox.getY());
        });

        redBox.setOnClickListener(view1 -> {
            selectedColor = ElementColor.RED;

            colorCheckmark.setX(redBox.getX());
            colorCheckmark.setY(redBox.getY());
        });

        yellowBox.setOnClickListener(view1 -> {
            selectedColor = ElementColor.YELLOW;

            colorCheckmark.setX(yellowBox.getX());
            colorCheckmark.setY(yellowBox.getY());
        });

        nameBox.setOnFocusChangeListener((view1, hasFocus) -> {
            if (!hasFocus) {
                hideKeyboard();
            }
        });

        view.findViewById(R.id.note_edit_connection_confirm_btn).setOnClickListener(view1 -> {
            Connection newConn = new Connection(nameBox.getText().toString(), selectedColor, sourceNote.boardId, sourceNote.id, linkedNote.id);
            newConn = connectionManager.addConnection(newConn);

            sourceNote.connection.add(newConn.id);
            noteManager.updateNote(sourceNote);

            linkedNote.connection.add(newConn.id);
            noteManager.updateNote(linkedNote);

            FragmentTransaction ft = getParentFragment().getChildFragmentManager().beginTransaction();
            ft.replace(R.id.note_edit_content_container, new NoteEditConnectionFragment(sourceNote));
            ft.commit();
        });

        return view;
    }

    @Override
    public void onDestroy() {
        // De-referencing listeners to avoid mem leak
        blueBox.setOnClickListener(null);
        greenBox.setOnClickListener(null);
        orangeBox.setOnClickListener(null);
        pinkBox.setOnClickListener(null);
        redBox.setOnClickListener(null);
        yellowBox.setOnClickListener(null);

        super.onDestroy();
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
