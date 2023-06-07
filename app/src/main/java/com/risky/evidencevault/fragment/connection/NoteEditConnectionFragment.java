package com.risky.evidencevault.fragment.connection;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.risky.evidencevault.R;
import com.risky.evidencevault.dao.Connection;
import com.risky.evidencevault.dao.Note;
import com.risky.evidencevault.data.ObjectBoxConnectionManager;
import com.risky.evidencevault.data.ObjectBoxNoteManager;
import com.risky.evidencevault.fragment.adapter.ConnectionDisplayArrayAdapter;
import com.risky.evidencevault.utils.NumberUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class NoteEditConnectionFragment extends Fragment {
    // Database manager
    private ObjectBoxNoteManager noteManager;
    private ObjectBoxConnectionManager connectionManager;

    private View view;
    private Note note;
    private ConnectionDisplayArrayAdapter adapter;

    private GridView connectionGrid;
    private LinearLayout addButton;
    private EditText filterBox;

    public NoteEditConnectionFragment(Note note) {
        this.note = note;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        noteManager = ObjectBoxNoteManager.get();
        connectionManager = ObjectBoxConnectionManager.get();
        view = inflater.inflate(R.layout.fragment_note_connection, container, false);

        // Find element
        connectionGrid = view.findViewById(R.id.note_edit_connection_grid);
        addButton = view.findViewById(R.id.note_edit_connection_add_btn);
        filterBox = view.findViewById(R.id.note_edit_connection_filter);

        // Add connections to list
        List<Long> noteList = new ArrayList<>();
        noteList.addAll(note.connection);
        adapter = new ConnectionDisplayArrayAdapter(getContext(),
                R.layout.fragment_edit_note_connection_item, noteList, note);
        connectionGrid.setAdapter(adapter);

        // Map add button
        addButton.setOnClickListener(view -> {
            FragmentTransaction ft = getParentFragment().getChildFragmentManager().beginTransaction();
            ft.replace(R.id.note_edit_content_container, new NoteEditConnectionSelectNoteFragment(note));
            ft.commit();
        });

        // Filter box listener
        filterBox.setOnFocusChangeListener((view, hasFocus) -> {
            if (!hasFocus) {
                hideKeyboard();
            }
        });

        filterBox.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                filterBox.clearFocus();
            }
            return false;
        });

        filterBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().trim().isEmpty()) {
                    List<Long> updatedList = new ArrayList<>();
                    updatedList.addAll(note.connection);
                    adapter.update(updatedList);
                    return;
                }

                adapter.update(filter(editable.toString().trim()));
            }
        });

        return view;
    }

    @Override
    public void onDestroy() {
        // Dereference onClickListener to avoid mem leak
        addButton.setOnClickListener(null);
        filterBox.setOnFocusChangeListener(null);
        filterBox.setOnEditorActionListener(null);

        super.onDestroy();
    }

    private List<Long> filter(String regex) {
        String lowerRegex = regex.toLowerCase();
        List<Long> result = new ArrayList<>();
        Set<Long> updatedList = noteManager.findById(note.id).connection;
        for (Long connId : updatedList) {
            Connection conn = connectionManager.findById(connId);
            Note linkedNote = noteManager.findById(conn.getLinkedNoteId(note.id));

            if (NumberUtil.convertToDisplayId(linkedNote.id).toLowerCase().contains(lowerRegex) ||
                conn.name.toLowerCase().contains(lowerRegex) || linkedNote.title.toLowerCase().contains(lowerRegex)) {
                result.add(connId);
            }
        }

        return result;
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
