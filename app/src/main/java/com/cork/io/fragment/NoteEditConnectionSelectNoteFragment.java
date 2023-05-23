package com.cork.io.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cork.io.R;
import com.cork.io.dao.Note;
import com.cork.io.data.NoteManager;
import com.cork.io.data.ObjectBoxNoteManager;
import com.cork.io.fragment.adapter.ConnectionSelectableArrayAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class NoteEditConnectionSelectNoteFragment extends Fragment {
    // Database manager
    private NoteManager noteManager;

    private View view;
    private Note note;

    private GridView connectionGrid;

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

        // Add connections to list
        List<Note> noteList = noteManager.getAllNotes();
        Set<Long> currentLink = note.getLinkedNotes();
        List<Long> availableList = new ArrayList<>();
        for (Note n : noteList) {
            if (!currentLink.contains(n.id) && n.id != note.id) {
                availableList.add(n.id);
            }
        }
        connectionGrid.setAdapter(new ConnectionSelectableArrayAdapter(getContext(),
                R.layout.fragment_edit_note_connection_add_item, availableList,
                note, getParentFragment().getChildFragmentManager()));

        return view;
    }
}
