package com.risky.evidencevault.fragment.notepreset;

import android.content.Context;
import android.os.Bundle;
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

import com.risky.evidencevault.R;
import com.risky.evidencevault.dao.ChecklistNoteData;
import com.risky.evidencevault.dao.Note;
import com.risky.evidencevault.data.ObjectBoxNoteChecklistDataManager;
import com.risky.evidencevault.data.ObjectBoxNoteManager;
import com.risky.evidencevault.struct.ChecklistItem;
import com.risky.evidencevault.struct.ElementColor;

public class NoteEditSummaryChecklistAddFragment extends Fragment {
    private ObjectBoxNoteManager noteManager;
    private ObjectBoxNoteChecklistDataManager dataManager;

    private View view;
    private Note note;
    private ChecklistNoteData data;

    private ElementColor selectedColor = ElementColor.BLUE;

    private ImageView blueBox;
    private ImageView greenBox;
    private ImageView orangeBox;
    private ImageView pinkBox;
    private ImageView redBox;
    private ImageView yellowBox;
    private EditText nameBox;
    private TextView confirmBtn;

    public NoteEditSummaryChecklistAddFragment(Note note) {
        this.noteManager = ObjectBoxNoteManager.get();
        this.dataManager = ObjectBoxNoteChecklistDataManager.get();

        this.note = note;
        this.data = dataManager.findById(note.dataId);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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

        confirmBtn.setOnClickListener(view1 -> {
            if (nameBox.getText() == null || nameBox.getText().toString().trim().isEmpty()) {
                return;
            }

            data.lastOrder++;
            data.list.add(new ChecklistItem(data.lastOrder, nameBox.getText().toString().trim(), selectedColor, false));

            dataManager.update(data);

            FragmentTransaction ft = getParentFragment().getChildFragmentManager().beginTransaction();
            ft.replace(R.id.note_edit_content_container, new NoteEditSummaryChecklistFragment(note));
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

        super.onDestroy();
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
