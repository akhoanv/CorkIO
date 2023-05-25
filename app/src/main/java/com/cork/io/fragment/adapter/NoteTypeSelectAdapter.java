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
import androidx.fragment.app.DialogFragment;

import com.cork.io.R;
import com.cork.io.struct.NoteType;
import com.cork.io.utils.NewNoteCallback;

import java.util.List;

public class NoteTypeSelectAdapter extends ArrayAdapter {
    private List<NoteType> typeList;
    private NewNoteCallback callback;

    public NoteTypeSelectAdapter(@NonNull Context context, int resource,
                                 @NonNull List<NoteType> objects, NewNoteCallback callback) {
        super(context, resource, objects);

        this.typeList = objects;
        this.callback = callback;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            // getting reference to the main layout and initializing
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.fragment_note_type_select_item, null);
        }

        // Find element
        TextView nameElement = view.findViewById(R.id.note_type_name);
        ImageView iconElement = view.findViewById(R.id.note_type_icon);

        // Assign appropriate data
        nameElement.setText(capitalize(typeList.get(position).name().toLowerCase()) + " Note");
        iconElement.setImageResource(typeList.get(position).getIcon().getId());

        view.setOnClickListener(view1 -> callback.run(typeList.get(position)));

        return view;
    }

    private String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
