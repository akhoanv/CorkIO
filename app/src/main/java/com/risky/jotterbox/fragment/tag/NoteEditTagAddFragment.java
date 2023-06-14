package com.risky.jotterbox.fragment.tag;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.risky.jotterbox.R;
import com.risky.jotterbox.dao.Board;
import com.risky.jotterbox.dao.Note;
import com.risky.jotterbox.dao.Tag;
import com.risky.jotterbox.data.ObjectBoxBoardManager;
import com.risky.jotterbox.data.ObjectBoxNoteManager;
import com.risky.jotterbox.data.ObjectBoxTagManager;
import com.risky.jotterbox.struct.ElementColor;

public class NoteEditTagAddFragment extends Fragment {
    private ObjectBoxNoteManager noteManager;
    private ObjectBoxTagManager tagManager;
    private ObjectBoxBoardManager boardManager;

    private View view;
    private Note note;
    private ElementColor selectedColor = ElementColor.BLUE;

    private ImageView blueBox;
    private ImageView greenBox;
    private ImageView orangeBox;
    private ImageView pinkBox;
    private ImageView redBox;
    private ImageView yellowBox;
    private EditText nameBox;
    private TextView confirmBtn;

    public NoteEditTagAddFragment(Note note) {
        this.note = note;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        noteManager = ObjectBoxNoteManager.get();
        tagManager = ObjectBoxTagManager.get();
        boardManager = ObjectBoxBoardManager.get();
        view = inflater.inflate(R.layout.fragment_note_edit_properties, container, false);

        // Find element
        nameBox = view.findViewById(R.id.note_edit_connection_name_box);

        blueBox = view.findViewById(R.id.note_edit_color_blue);
        greenBox = view.findViewById(R.id.note_edit_color_green);
        orangeBox = view.findViewById(R.id.note_edit_color_orange);
        pinkBox = view.findViewById(R.id.note_edit_color_pink);
        redBox = view.findViewById(R.id.note_edit_color_red);
        yellowBox = view.findViewById(R.id.note_edit_color_yellow);
        confirmBtn = view.findViewById(R.id.note_edit_connection_confirm_btn);

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

        nameBox.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                nameBox.clearFocus();
            }
            return false;
        });

        confirmBtn.setOnClickListener(view1 -> {
            long existingId = tagManager.findByProperties(note.boardId,
                    nameBox.getText().toString().trim(), selectedColor);
            if (existingId == -1) {
                Tag newTag = new Tag(note.boardId, nameBox.getText().toString().trim(), selectedColor);
                newTag = tagManager.add(newTag);

                Board currentBoard = boardManager.findById(note.boardId);
                currentBoard.tags.add(newTag.id);
                boardManager.update(currentBoard);

                existingId = newTag.id;
            }

            if (!note.tag.contains(existingId)) {
                note.tag.add(existingId);
                noteManager.update(note);

                Tag currentTag = tagManager.findById(existingId);
                currentTag.relatedNotes.add(note.id);
                tagManager.update(currentTag);
            }

            FragmentTransaction ft = getParentFragment().getChildFragmentManager().beginTransaction();
            ft.replace(R.id.note_edit_content_container, new NoteEditTagFragment(note));
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
        confirmBtn.setOnClickListener(null);
        nameBox.setOnFocusChangeListener(null);
        nameBox.setOnEditorActionListener(null);

        super.onDestroy();
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
