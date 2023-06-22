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
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.risky.jotterbox.MainActivity;
import com.risky.jotterbox.R;
import com.risky.jotterbox.dao.Board;
import com.risky.jotterbox.dao.Connection;
import com.risky.jotterbox.dao.Note;
import com.risky.jotterbox.dao.Region;
import com.risky.jotterbox.data.ObjectBoxBoardManager;
import com.risky.jotterbox.data.ObjectBoxRegionManager;
import com.risky.jotterbox.fragment.adapter.AllRegionDisplayArrayAdapter;
import com.risky.jotterbox.struct.Point2D;
import com.risky.jotterbox.utils.NumberUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BoardEditAllRegionFragment extends Fragment {
    private ObjectBoxBoardManager boardManager;
    private ObjectBoxRegionManager regionManager;

    private View view;
    private Board board;

    private LinearLayout backBtn;
    private GridView regionList;
    private LinearLayout addBtn;
    private EditText filterBox;

    private AllRegionDisplayArrayAdapter adapter;

    public BoardEditAllRegionFragment(Board board) {
        this.board = board;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        boardManager = ObjectBoxBoardManager.get();
        regionManager = ObjectBoxRegionManager.get();
        view = inflater.inflate(R.layout.fragment_board_all_region, container, false);

        // Find elements
        backBtn = view.findViewById(R.id.board_edit_back_btn);
        regionList = view.findViewById(R.id.board_edit_all_region_grid);
        addBtn = view.findViewById(R.id.board_edit_all_add_btn);
        filterBox = view.findViewById(R.id.board_edit_all_region_filter);

        // Assign data
        adapter = new AllRegionDisplayArrayAdapter(getContext(),
                R.layout.fragment_edit_board_roi_item, new ArrayList<>(board.roi), board,
                ((FragmentActivity) getContext()).getSupportFragmentManager());
        regionList.setAdapter(adapter);

        // Assign listener
        backBtn.setOnClickListener(view1 -> {
            FragmentTransaction ft = getParentFragment().getChildFragmentManager().beginTransaction();
            ft.replace(R.id.board_edit_content_container, new BoardEditSummaryFragment(board));
            ft.commit();
        });

        addBtn.setOnClickListener(view1 -> {
            FragmentTransaction ft = getParentFragment().getChildFragmentManager().beginTransaction();
            ft.replace(R.id.board_edit_content_container, new BoardEditRegionAddFragment(board));
            ft.commit();
        });

        // Filter box listener
        filterBox.setOnFocusChangeListener((view, hasFocus) -> {
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

        filterBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().trim().isEmpty()) {
                    List<Long> updatedList = new ArrayList<>();
                    updatedList.addAll(board.roi);
                    adapter.update(updatedList);
                    return;
                }

                adapter.update(filter(editable.toString().trim()));
            }
        });

        return view;
    }

    @Override
    public void onDestroy() {
        // De-reference listener to avoid mem leak
        backBtn.setOnClickListener(null);
        addBtn.setOnClickListener(null);

        ((MainActivity) getContext()).initializeBoard(board.id);

        super.onDestroy();
    }

    private List<Long> filter(String regex) {
        String lowerRegex = regex.toLowerCase();
        List<Long> result = new ArrayList<>();
        Set<Long> updatedList = boardManager.findById(board.id).roi;
        for (Long roiId : updatedList) {
            Region roi = regionManager.findById(roiId);
            Point2D pos = roi.position;

            if (roi.name.toLowerCase().contains(lowerRegex) || Integer.toString(-Math.round(pos.getX())).contains(lowerRegex)
                    || Integer.toString(Math.round(pos.getY())).contains(lowerRegex)) {
                result.add(roiId);
            }
        }

        return result;
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
