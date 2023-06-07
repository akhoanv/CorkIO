package com.risky.evidencevault.fragment.connection;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.risky.evidencevault.R;
import com.risky.evidencevault.dao.Note;
import com.risky.evidencevault.data.ObjectBoxNoteManager;
import com.risky.evidencevault.fragment.adapter.ConnectionSelectableArrayAdapter;
import com.risky.evidencevault.utils.NumberUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class NoteEditConnectionSelectNoteFragment extends Fragment {
    // Database manager
    private ObjectBoxNoteManager noteManager;

    private View view;
    private Note note;
    private ConnectionSelectableArrayAdapter adapter;

    private GridView connectionGrid;
    private EditText filterBox;

    public NoteEditConnectionSelectNoteFragment(Note note) {
        this.note = note;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        noteManager = ObjectBoxNoteManager.get();
        view = inflater.inflate(R.layout.fragment_note_select_new_connection, container, false);

        // Find element
        connectionGrid = view.findViewById(R.id.note_edit_connection_grid);
        filterBox = view.findViewById(R.id.note_edit_connection_filter);

        // Add connections to list
        List<Note> noteList = noteManager.getAll();
        Set<Long> currentLink = note.getLinkedNotes();
        List<Long> availableList = new ArrayList<>();
        for (Note n : noteList) {
            if (!currentLink.contains(n.id) && n.id != note.id) {
                availableList.add(n.id);
            }
        }
        adapter = new ConnectionSelectableArrayAdapter(getContext(),
                R.layout.fragment_edit_note_connection_add_item, new ArrayList<>(availableList),
                note, getParentFragment().getChildFragmentManager());
        connectionGrid.setAdapter(adapter);

        // Filter box listener
        filterBox.setOnFocusChangeListener((view, hasFocus) -> {
            if (!hasFocus) {
                hideKeyboard();
            }
        });

        filterBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().trim().isEmpty()) {
                    adapter.update(new ArrayList<>(availableList));
                    return;
                }

                adapter.update(filter(new ArrayList<>(availableList), editable.toString().trim()));
            }
        });

        return view;
    }

    @Override
    public void onDestroy() {
        // Dereference onClickListener to avoid mem leak
        filterBox.setOnFocusChangeListener(null);

        super.onDestroy();
    }

    private List<Long> filter(List<Long> availableList, String regex) {
        String lowerRegex = regex.toLowerCase();
        List<Long> result = new ArrayList<>();
        for (Long noteId : availableList) {
            if (NumberUtil.convertToDisplayId(noteId).toLowerCase().contains(lowerRegex) ||
                    noteManager.findById(noteId).title.toLowerCase().contains(lowerRegex)) {
                result.add(noteId);
            }
        }

        return result;
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
