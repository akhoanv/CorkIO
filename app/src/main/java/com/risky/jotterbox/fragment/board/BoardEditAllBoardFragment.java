package com.risky.jotterbox.fragment.board;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.risky.jotterbox.R;
import com.risky.jotterbox.dao.Board;
import com.risky.jotterbox.data.ObjectBoxBoardManager;
import com.risky.jotterbox.fragment.adapter.BoardDisplayArrayAdapter;
import com.risky.jotterbox.utils.NumberUtil;

import java.util.ArrayList;
import java.util.List;

public class BoardEditAllBoardFragment extends Fragment {
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

        // Assign listener
        filterBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().trim().isEmpty()) {
                    adapter.update(boardManager.getAll());
                    return;
                }

                adapter.update(filter(editable.toString().trim()));
            }
        });

        filterBox.setOnFocusChangeListener((view1, hasFocus) -> {
            if (!hasFocus) {
                hideKeyboard();
            }
        });

        filterBox.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                filterBox.clearFocus();
            }
            return false;
        });

        addBtn.setOnClickListener(view1 -> {
            boardManager.add(new Board());

            filterBox.setText("");
        });

        return view;
    }

    @Override
    public void onDestroy() {
        // De-reference listeners to avoid mem leak
        addBtn.setOnClickListener(null);
        filterBox.setOnFocusChangeListener(null);
        filterBox.setOnEditorActionListener(null);

        super.onDestroy();
    }

    private List<Board> filter(String regex) {
        String lowerRegex = regex.toLowerCase();
        List<Board> result = new ArrayList<>();
        List<Board> updatedList = boardManager.getAll();
        for (Board board : updatedList) {
            if (NumberUtil.convertToDisplayId(board.id).toLowerCase().contains(lowerRegex) ||
                    board.name.toLowerCase().contains(lowerRegex)) {
                result.add(board);
            }
        }

        return result;
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
