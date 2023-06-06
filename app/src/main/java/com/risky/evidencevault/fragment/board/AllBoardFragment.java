package com.risky.evidencevault.fragment.board;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.risky.evidencevault.R;
import com.risky.evidencevault.dao.Board;
import com.risky.evidencevault.data.ObjectBoxBoardManager;
import com.risky.evidencevault.fragment.adapter.BoardDisplayArrayAdapter;
import com.risky.evidencevault.utils.BoardSelectCallback;

import java.util.ArrayList;
import java.util.List;

public class AllBoardFragment extends Fragment {
    private ObjectBoxBoardManager boardManager;

    private View view;

    private EditText filterBox;
    private GridView boardList;
    private LinearLayout addBtn;

    private BoardDisplayArrayAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        boardManager = ObjectBoxBoardManager.get();
        view = inflater.inflate(R.layout.fragment_all_board, container, false);

        // Find elements
        filterBox = view.findViewById(R.id.board_edit_all_filter);
        boardList = view.findViewById(R.id.board_edit_all_grid);
        addBtn = view.findViewById(R.id.board_edit_all_add_btn);

        // Assign data
        List<Board> boardData = boardManager.getAll();
        adapter = new BoardDisplayArrayAdapter(getContext(), R.layout.fragment_edit_note_connection_add_item,
                boardData, board -> {
                    FragmentTransaction ft = getParentFragment().getChildFragmentManager().beginTransaction();
                    ft.replace(R.id.board_edit_content_container, new BoardEditSummaryFragment(board));
                    ft.commit();
                });

        boardList.setAdapter(adapter);

        addBtn.setOnClickListener(view1 -> {
            boardManager.add(new Board());

            adapter.update();
        });

        //TODO: ADD FILTER FUNCTION FOR FILTER BOX

        return view;
    }

    @Override
    public void onDestroy() {
        // De-reference listeners to avoid mem leak
        addBtn.setOnClickListener(null);

        super.onDestroy();
    }
}
