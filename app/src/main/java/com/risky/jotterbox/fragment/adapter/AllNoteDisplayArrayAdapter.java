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
import com.risky.jotterbox.dao.Note;
import com.risky.jotterbox.data.ObjectBoxNoteManager;
import com.risky.jotterbox.struct.Point2D;
import com.risky.jotterbox.utils.DeviceProperties;
import com.risky.jotterbox.utils.NumberUtil;

import java.util.List;

/**
 * Adapter for displaying {@link Note}, given a {@link List} of IDs belong to those in database
 *
 * @author Khoa Nguyen
 */
public class AllNoteDisplayArrayAdapter extends ArrayAdapter {
    private ObjectBoxNoteManager noteManager;

    private FragmentManager fragmentManager;

    public AllNoteDisplayArrayAdapter(@NonNull Context context, int resource,
                                      @NonNull List<Long> objects, FragmentManager fragmentManager) {
        super(context, resource, objects);

        this.noteManager = ObjectBoxNoteManager.get();
        this.fragmentManager = fragmentManager;
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

        view.setOnClickListener(view1 -> {
            // Move to note, offset 1/3 of screen size
            Point2D positionToMove = new Point2D(
                    currentNote.position.getX() - (DeviceProperties.getScreenWidth() / 3),
                        currentNote.position.getY() - (DeviceProperties.getScreenHeight() / 3));
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

    public void update(List<Long> list) {
        clear();
        addAll(list);
        notifyDataSetChanged();
    }
}
