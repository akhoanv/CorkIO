package com.cork.io.fragment.connection;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.cork.io.R;
import com.cork.io.dao.Note;
import com.cork.io.data.NoteManager;
import com.cork.io.data.ObjectBoxNoteManager;
import com.cork.io.fragment.adapter.ConnectionDisplayArrayAdapter;

import java.util.ArrayList;
import java.util.List;

public class NoteEditConnectionFragment extends Fragment {
    // Database manager
    private NoteManager noteManager;

    private View view;
    private Note note;

    private GridView connectionGrid;
    private LinearLayout addButton;

    public NoteEditConnectionFragment(Note note) {
        this.note = note;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        noteManager = ObjectBoxNoteManager.get();
        view = inflater.inflate(R.layout.fragment_note_connection, container, false);

        // Find element
        connectionGrid = view.findViewById(R.id.note_edit_connection_grid);
        addButton = view.findViewById(R.id.note_edit_connection_add_btn);

        // Add connections to list
        List<Long> noteList = new ArrayList<>();
        noteList.addAll(note.connection);
        connectionGrid.setAdapter(new ConnectionDisplayArrayAdapter(getContext(),
                R.layout.fragment_edit_note_connection_item, noteList, note));

        // Map add button
        addButton.setOnClickListener(view -> {
            FragmentTransaction ft = getParentFragment().getChildFragmentManager().beginTransaction();
            ft.replace(R.id.note_edit_content_container, new NoteEditConnectionSelectNoteFragment(note));
            ft.commit();
        });

        return view;
    }

    @Override
    public void onDestroy() {
        // Dereference onClickListener to avoid mem leak
        addButton.setOnClickListener(null);

        super.onDestroy();
    }
}
