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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.risky.evidencevault.R;
import com.risky.evidencevault.dao.Note;
import com.risky.evidencevault.data.ObjectBoxConnectionManager;
import com.risky.evidencevault.data.ObjectBoxNoteManager;
import com.risky.evidencevault.fragment.connection.NoteEditConnectionAddFragment;
import com.risky.evidencevault.utils.NumberUtil;

import java.util.List;

/**
 * Adapter for showing available {@link Note} to select from to form a {@link com.risky.evidencevault.dao.Connection}.
 * On element click will replace note edit screen with edit properties screen
 *
 * @author Khoa Nguyen
 */
public class ConnectionSelectableArrayAdapter extends ArrayAdapter {
    // Database manager
    private ObjectBoxNoteManager noteManager;
    private ObjectBoxConnectionManager connectionManager;

    // Adapter properties
    private Note note;
    private FragmentManager fragmentManager;

    public ConnectionSelectableArrayAdapter(@NonNull Context context, int resource, @NonNull List<Long> objects,
                                            Note note, FragmentManager fragmentManager) {
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
            view = inflater.inflate(R.layout.fragment_edit_note_connection_add_item, null);
        }

        // Find element
        ImageView iconView = view.findViewById(R.id.note_edit_connection_icon);
        TextView titleView = view.findViewById(R.id.note_edit_connection_title);
        TextView idView = view.findViewById(R.id.note_edit_connection_id);

        // Set appropriate data
        iconView.setImageResource(noteManager.findById((Long) getItem(position)).type.getIcon().getId());
        titleView.setText(noteManager.findById((Long) getItem(position)).title);
        idView.setText("#" + NumberUtil.convertToDisplayId((Long) getItem(position)));

        // Set onClickAction
        view.setOnClickListener(view1 -> {
            Note linkedNote = noteManager.findById((Long) getItem(position));

            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.note_edit_content_container, new NoteEditConnectionAddFragment(note, linkedNote));
            ft.commit();
        });

        return view;
    }

    public void update(List<Long> updatedList) {
        clear();
        addAll(updatedList);
        notifyDataSetChanged();
    }
}
