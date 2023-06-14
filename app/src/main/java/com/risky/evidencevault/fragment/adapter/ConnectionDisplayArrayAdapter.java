package com.risky.evidencevault.fragment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.risky.evidencevault.R;
import com.risky.evidencevault.dao.Connection;
import com.risky.evidencevault.dao.Note;
import com.risky.evidencevault.data.ObjectBoxConnectionManager;
import com.risky.evidencevault.data.ObjectBoxNoteManager;

import java.util.List;

/**
 * Adapter for showing {@link Connection} from one {@link Note} to another, given {@link List} of
 * IDs of which from the database. Button to remove that connection is included.
 *
 * @author Khoa Nguyen
 */
public class ConnectionDisplayArrayAdapter extends ArrayAdapter {
    // Database manager
    private ObjectBoxNoteManager noteManager;
    private ObjectBoxConnectionManager connectionManager;

    // Adapter properties
    private Note note;

    public ConnectionDisplayArrayAdapter(@NonNull Context context, int resource, @NonNull List<Long> objects, Note note) {
        super(context, resource, objects);

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
        Connection conn = connectionManager.findById((Long) getItem(position));
        Note linkedNote = noteManager.findById(conn.getLinkedNoteId(note.id));

        // Set appropriate data
        iconView.setImageResource(linkedNote.type.getIcon().getId());
        titleView.setText(conn.name);
        idView.setText("#" + linkedNote.getDisplayId() + " " + linkedNote.title);

        // Set remove button
        unlinkButton.setOnClickListener(view1 -> {
            // Remove this data point
            note.connection.remove(conn.id);
            linkedNote.connection.remove(conn.id);

            // UI update
            remove(conn.id);

            // Update database
            noteManager.update(note);
            noteManager.update(linkedNote);
        });

        return view;
    }

    public void update(List<Long> updatedList) {
        clear();
        addAll(updatedList);
        notifyDataSetChanged();
    }
}
