package com.risky.evidencevault.fragment.board;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.risky.evidencevault.R;
import com.risky.evidencevault.dao.Board;
import com.risky.evidencevault.data.ObjectBoxBoardManager;
import com.risky.evidencevault.data.ObjectBoxNoteManager;
import com.risky.evidencevault.fragment.adapter.AllNoteDisplayArrayAdapter;

import java.util.ArrayList;
import java.util.List;

public class BoardEditAllNotesFragment extends Fragment {
    private ObjectBoxBoardManager boardManager;
    private ObjectBoxNoteManager noteManager;
    private View view;
    private Board board;
    private AllNoteDisplayArrayAdapter adapter;

    private CircularProgressIndicator progBar;
    private TextView percentageElement;
    private TextView percentageSubtextElement;
    private LinearLayout filterBar;
    private LinearLayout backBtn;
    private GridView noteGrid;

    public BoardEditAllNotesFragment(Board board) {
        this.board = board;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        boardManager = ObjectBoxBoardManager.get();
        noteManager = ObjectBoxNoteManager.get();
        view = inflater.inflate(R.layout.fragment_board_all_notes, container, false);

        // Find elements
        progBar = view.findViewById(R.id.board_edit_all_note_bar);
        percentageElement = view.findViewById(R.id.board_edit_all_note_percentage);
        percentageSubtextElement = view.findViewById(R.id.board_edit_all_note_percentage_subtext);
        filterBar = view.findViewById(R.id.board_edit_all_filter_bar);
        backBtn = view.findViewById(R.id.board_edit_all_note_back_btn);
        noteGrid = view.findViewById(R.id.board_edit_all_note_grid);

        // Assign data
        int numberOfNote = board.notes.size();
        int maxNumberOfNote = noteManager.getAll().size();

        progBar.setMin(0);
        progBar.setMax(maxNumberOfNote);
        progBar.setProgress(numberOfNote);

        if (maxNumberOfNote == 0) {
            percentageElement.setText("No data found");
            progBar.setVisibility(View.INVISIBLE);
            filterBar.setVisibility(View.INVISIBLE);
            percentageSubtextElement.setVisibility(View.INVISIBLE);
        } else {
            percentageElement.setText(Math.round((numberOfNote * 100) / maxNumberOfNote) + "%");
        }

        List<Long> noteList = new ArrayList<>(board.notes);
        adapter = new AllNoteDisplayArrayAdapter(getContext(), R.layout.fragment_edit_note_connection_add_item, noteList);
        noteGrid.setAdapter(adapter);

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
}
