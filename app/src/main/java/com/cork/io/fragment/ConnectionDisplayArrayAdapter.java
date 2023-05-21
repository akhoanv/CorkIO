package com.cork.io.fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cork.io.R;
import com.cork.io.dao.Note;
import com.cork.io.data.NoteManager;
import com.cork.io.data.ObjectBoxNoteManager;

import java.util.List;

public class ConnectionDisplayArrayAdapter extends ArrayAdapter {
    private List<Long> noteList;
    private NoteManager noteManager;
    private Note note;

    public ConnectionDisplayArrayAdapter(@NonNull Context context, int resource, @NonNull List<Long> objects, Note note) {
        super(context, resource, objects);

        this.noteList = objects;
        this.noteManager = ObjectBoxNoteManager.get();
        this.note = note;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            // getting reference to the main layout and initializing
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.fragment_edit_note_connection_item, null);
        }

        // Find element
        ImageView iconView = view.findViewById(R.id.note_edit_connection_icon);
        TextView titleView = view.findViewById(R.id.note_edit_connection_title);
        TextView idView = view.findViewById(R.id.note_edit_connection_id);
        ImageView unlinkButton = view.findViewById(R.id.note_edit_connection_unlink);

        // Set appropriate data
        iconView.setImageResource(noteManager.findNoteById(noteList.get(position)).iconId);
        titleView.setText(noteManager.findNoteById(noteList.get(position)).title);
        idView.setText("ID: " + noteList.get(position));

        // Set remove button
        unlinkButton.setOnClickListener(view1 -> {
            // Remove this data point
            long idToBeRemoved = noteList.get(position);
            note.connection.remove(idToBeRemoved);

            Note relNote = noteManager.findNoteById(idToBeRemoved);
            relNote.connection.remove(note.id);

            // UI update
            noteList.remove(idToBeRemoved);
            remove(idToBeRemoved);

            // Update database
            noteManager.updateNote(note);
            noteManager.updateNote(relNote);
        });

        return view;
    }
}
