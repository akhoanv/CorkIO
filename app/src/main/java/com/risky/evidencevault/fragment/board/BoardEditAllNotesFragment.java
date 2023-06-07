package com.risky.evidencevault.fragment.board;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import com.risky.evidencevault.dao.Note;
import com.risky.evidencevault.data.ObjectBoxBoardManager;
import com.risky.evidencevault.data.ObjectBoxNoteManager;
import com.risky.evidencevault.fragment.adapter.AllNoteDisplayArrayAdapter;
import com.risky.evidencevault.utils.NumberUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
    private EditText filterBox;

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
        filterBox = view.findViewById(R.id.board_edit_all_note_filter);

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
        filterBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().trim().isEmpty()) {
                    List<Long> updatedList = new ArrayList<>();
                    updatedList.addAll(board.notes);
                    adapter.update(updatedList);
                    return;
                }

                adapter.update(filter(editable.toString().trim()));
            }
        });

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

    private List<Long> filter(String regex) {
        String lowerRegex = regex.toLowerCase();
        List<Long> result = new ArrayList<>();
        Set<Long> updatedList = board.notes;
        for (Long noteId : updatedList) {
            Note currentNode = noteManager.findById(noteId);
            if (NumberUtil.convertToDisplayId(noteId).toLowerCase().contains(lowerRegex) ||
                    currentNode.title.toLowerCase().contains(lowerRegex)) {
                result.add(noteId);
            }
        }

        return result;
    }
}
