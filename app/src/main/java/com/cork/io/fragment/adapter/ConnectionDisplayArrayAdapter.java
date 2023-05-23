package com.cork.io.fragment.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cork.io.R;
import com.cork.io.dao.Connection;
import com.cork.io.dao.Note;
import com.cork.io.data.ConnectionManager;
import com.cork.io.data.NoteManager;
import com.cork.io.data.ObjectBoxConnectionManager;
import com.cork.io.data.ObjectBoxNoteManager;

import java.util.List;

public class ConnectionDisplayArrayAdapter extends ArrayAdapter {
    // Database manager
    private NoteManager noteManager;
    private ConnectionManager connectionManager;

    // Adapter properties
    private List<Long> connectionList;
    private Note note;

    public ConnectionDisplayArrayAdapter(@NonNull Context context, int resource, @NonNull List<Long> objects, Note note) {
        super(context, resource, objects);

        this.connectionList = objects;
        this.noteManager = ObjectBoxNoteManager.get();
        this.connectionManager = ObjectBoxConnectionManager.get();
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

        // Find Connection
        Connection conn = connectionManager.findConnectionById(connectionList.get(position));
        Note linkedNote = noteManager.findNoteById(conn.getLinkedNoteId(note.id));

        // Set appropriate data
        iconView.setImageResource(linkedNote.iconId);
        titleView.setText(conn.name);
        idView.setText("#" + linkedNote.id + " " + linkedNote.title);

        // Set remove button
        unlinkButton.setOnClickListener(view1 -> {
            // Remove this data point
            note.connection.remove(conn.id);
            linkedNote.connection.remove(conn.id);

            // UI update
            connectionList.remove(conn.id);
            remove(conn.id);

            // Update database
            noteManager.updateNote(note);
            noteManager.updateNote(linkedNote);
        });

        return view;
    }
}
