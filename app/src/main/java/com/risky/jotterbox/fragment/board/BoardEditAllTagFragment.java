package com.risky.jotterbox.fragment.board;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.flexbox.FlexboxLayout;
import com.risky.jotterbox.R;
import com.risky.jotterbox.dao.Board;
import com.risky.jotterbox.dao.Tag;
import com.risky.jotterbox.data.ObjectBoxBoardManager;
import com.risky.jotterbox.data.ObjectBoxTagManager;

import java.util.Set;

public class BoardEditAllTagFragment extends Fragment {
    private ObjectBoxBoardManager boardManager;
    private ObjectBoxTagManager tagManager;
    private View view;
    private Board board;
    private LinearLayout backBtn;
    private FlexboxLayout tagBox;

    public BoardEditAllTagFragment(Board board) {
        this.board = board;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        boardManager = ObjectBoxBoardManager.get();
        tagManager = ObjectBoxTagManager.get();
        view = inflater.inflate(R.layout.fragment_board_all_tag, container, false);

        // Find elements
        backBtn = view.findViewById(R.id.board_edit_all_note_back_btn);
        tagBox = view.findViewById(R.id.board_edit_all_tag_box);

        // Assign data
        drawList(board.tags);

        // Assign listener
        backBtn.setOnClickListener(view1 -> {
            FragmentTransaction ft = getParentFragment().getChildFragmentManager().beginTransaction();
            ft.replace(R.id.board_edit_content_container, new BoardEditSummaryFragment(board));
            ft.commit();
        });

        return view;
    }

    @Override
    public void onDestroy() {
        // De-reference listener to avoid mem leak
        backBtn.setOnClickListener(null);

        super.onDestroy();
    }

    private void drawList(Set<Long> items) {
        float scale = getResources().getDisplayMetrics().density;

        int paddingVert = (int) (5*scale + 0.5f);
        int paddingHorz = (int) (8*scale + 0.5f);
        int margin = (int) (3*scale + 0.5f);

        for (Long tagId : items) {
            Tag item = tagManager.findById(tagId);

            TextView listItem = new TextView(getContext());
            listItem.setText(item.name);
            listItem.setTextColor(getContext().getColor(R.color.paper_white));
            listItem.setBackgroundResource(item.color.getBackgroundId());
            listItem.setPadding(paddingHorz, paddingVert, paddingHorz, paddingVert);

            tagBox.addView(listItem);

            // Add spacing between items
            ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) listItem.getLayoutParams();
            lp.bottomMargin = margin;
            lp.rightMargin = margin;
        }
    }
}
