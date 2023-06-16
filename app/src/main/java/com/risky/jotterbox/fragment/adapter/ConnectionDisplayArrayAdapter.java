package com.risky.jotterbox.fragment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.risky.jotterbox.MainActivity;
import com.risky.jotterbox.R;
import com.risky.jotterbox.dao.Connection;
import com.risky.jotterbox.dao.Note;
import com.risky.jotterbox.data.ObjectBoxConnectionManager;
import com.risky.jotterbox.data.ObjectBoxNoteManager;
import com.risky.jotterbox.struct.Point2D;
import com.risky.jotterbox.utils.DeviceProperties;

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

    private FragmentManager fragmentManager;

    // Adapter properties
    private Note note;

    public ConnectionDisplayArrayAdapter(@NonNull Context context, int resource,
                                         @NonNull List<Long> objects, Note note, FragmentManager fragmentManager) {
        super(context, resource, objects);

        this.noteManager = ObjectBoxNoteManager.get();
        this.connectionManager = ObjectBoxConnectionManager.get();
        this.note = note;
        this.fragmentManager = fragmentManager;
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

        // Set listener
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

        view.setOnClickListener(view1 -> {
            // Move to note, offset 1/3 of screen size
            Point2D positionToMove = new Point2D(
                    linkedNote.position.getX() - (DeviceProperties.getScreenWidth() / 3),
                    linkedNote.position.getY() - (DeviceProperties.getScreenHeight() / 3));
            ((MainActivity) getContext()).moveBoardTo(positionToMove);

            // Close any dialog fragment
            FragmentTransaction ft = fragmentManager.beginTransaction();
            Fragment prev = fragmentManager.findFragmentByTag("dialog");
            if (prev != null) {
                ((DialogFragment) prev).dismiss();
                ft.remove(prev);
            }
        });

        return view;
    }

    public void update(List<Long> updatedList) {
        clear();
        addAll(updatedList);
        notifyDataSetChanged();
    }
}
