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
import com.risky.evidencevault.dao.Board;
import com.risky.evidencevault.dao.Note;
import com.risky.evidencevault.data.ObjectBoxNoteManager;
import com.risky.evidencevault.utils.NumberUtil;

import java.util.List;

public class AllNoteDisplayArrayAdapter extends ArrayAdapter {
    private ObjectBoxNoteManager noteManager;

    public AllNoteDisplayArrayAdapter(@NonNull Context context, int resource, @NonNull List<Long> objects) {
        super(context, resource, objects);

        this.noteManager = ObjectBoxNoteManager.get();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            // getting reference to the main layout and initializing
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.fragment_edit_note_connection_add_item, null);
        }

        // Find element
        ImageView iconView = view.findViewById(R.id.note_edit_connection_icon);
        TextView titleView = view.findViewById(R.id.note_edit_connection_title);
        TextView idView = view.findViewById(R.id.note_edit_connection_id);

        Note currentNote = noteManager.findById(((long) getItem(position)));

        // Assign data
        iconView.setImageResource(currentNote.type.getIcon().getId());
        titleView.setText(currentNote.title);
        idView.setText("#" + NumberUtil.convertToDisplayId(currentNote.id));

        return view;
    }

    public void update(List<Long> list) {
        clear();
        addAll(list);
        notifyDataSetChanged();
    }
}
