package com.cork.io.fragment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.cork.io.R;
import com.cork.io.dao.Note;
import com.cork.io.data.ConnectionManager;
import com.cork.io.data.NoteManager;
import com.cork.io.data.ObjectBoxConnectionManager;
import com.cork.io.data.ObjectBoxNoteManager;
import com.cork.io.fragment.NoteEditConnectionAddFragment;

import java.util.List;

public class ConnectionSelectableArrayAdapter extends ArrayAdapter {
    // Database manager
    private NoteManager noteManager;
    private ConnectionManager connectionManager;

    // Adapter properties
    private List<Long> noteList;
    private Note note;
    private FragmentManager fragmentManager;

    public ConnectionSelectableArrayAdapter(@NonNull Context context, int resource, @NonNull List<Long> objects,
                                            Note note, FragmentManager fragmentManager) {
        super(context, resource, objects);

        this.noteManager = ObjectBoxNoteManager.get();
        this.connectionManager = ObjectBoxConnectionManager.get();

        this.noteList = objects;
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
            view = inflater.inflate(R.layout.fragment_edit_note_connection_add_item, null);
        }

        // Find element
        ImageView iconView = view.findViewById(R.id.note_edit_connection_icon);
        TextView titleView = view.findViewById(R.id.note_edit_connection_title);
        TextView idView = view.findViewById(R.id.note_edit_connection_id);

        // Set appropriate data
        iconView.setImageResource(noteManager.findNoteById(noteList.get(position)).type.getIcon().getId());
        titleView.setText(noteManager.findNoteById(noteList.get(position)).title);
        idView.setText("ID: " + noteList.get(position));

        // Set onClickAction
        view.setOnClickListener(view1 -> {
            Note linkedNote = noteManager.findNoteById(noteList.get(position));

            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.note_edit_content_container, new NoteEditConnectionAddFragment(note, linkedNote));
            ft.commit();
        });

        return view;
    }
}
