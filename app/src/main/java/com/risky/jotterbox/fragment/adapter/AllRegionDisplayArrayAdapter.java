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
import com.risky.jotterbox.dao.Board;
import com.risky.jotterbox.dao.Note;
import com.risky.jotterbox.dao.Region;
import com.risky.jotterbox.data.ObjectBoxBoardManager;
import com.risky.jotterbox.data.ObjectBoxNoteManager;
import com.risky.jotterbox.data.ObjectBoxRegionManager;
import com.risky.jotterbox.struct.Point2D;
import com.risky.jotterbox.utils.DeviceProperties;
import com.risky.jotterbox.utils.NumberUtil;

import java.util.List;

public class AllRegionDisplayArrayAdapter extends ArrayAdapter {
    private ObjectBoxBoardManager boardManager;
    private ObjectBoxRegionManager regionManager;

    private FragmentManager fragmentManager;

    private Board board;

    public AllRegionDisplayArrayAdapter(@NonNull Context context, int resource,
                                        @NonNull List<Long> objects, Board board, FragmentManager fragmentManager) {
        super(context, resource, objects);

        this.boardManager = ObjectBoxBoardManager.get();
        this.regionManager = ObjectBoxRegionManager.get();
        this.fragmentManager = fragmentManager;
        this.board = board;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            // getting reference to the main layout and initializing
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.fragment_edit_board_roi_item, null);
        }

        // Find element
        ImageView iconView = view.findViewById(R.id.board_edit_list_icon);
        TextView titleView = view.findViewById(R.id.board_edit_list_title);
        TextView contentView = view.findViewById(R.id.board_edit_list_content);
        ImageView deleteBtn = view.findViewById(R.id.board_edit_list_delete_btn);

        Region currentRoi = regionManager.findById(((long) getItem(position)));
        Point2D pos = currentRoi.position;

        // Assign data
        iconView.setImageResource(currentRoi.color.getRoundId());
        titleView.setText(currentRoi.name);
        contentView.setText("X: " + (-Math.round(pos.getX())) + ", Y: " + Math.round(pos.getY()));

        view.setOnClickListener(view1 -> {
            // Move to coord
            Point2D positionToMove = new Point2D(-pos.getX(), -pos.getY());
            ((MainActivity) getContext()).moveBoardTo(positionToMove);

            // Close any dialog fragment
            FragmentTransaction ft = fragmentManager.beginTransaction();
            Fragment prev = fragmentManager.findFragmentByTag("dialog");
            if (prev != null) {
                ((DialogFragment) prev).dismiss();
                ft.remove(prev);
            }
        });

        deleteBtn.setOnClickListener(view1 -> {
            board.roi.remove(currentRoi.id);
            boardManager.update(board);

            // Update UI
            remove(currentRoi.id);
        });

        return view;
    }

    public void update(List<Long> list) {
        clear();
        addAll(list);
        notifyDataSetChanged();
    }
}
