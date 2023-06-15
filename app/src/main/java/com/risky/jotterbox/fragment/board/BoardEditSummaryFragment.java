package com.risky.jotterbox.fragment.board;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.flexbox.FlexboxLayout;
import com.risky.jotterbox.MainActivity;
import com.risky.jotterbox.R;
import com.risky.jotterbox.dao.Board;
import com.risky.jotterbox.dao.Note;
import com.risky.jotterbox.dao.Tag;
import com.risky.jotterbox.data.ObjectBoxBoardManager;
import com.risky.jotterbox.data.ObjectBoxConnectionManager;
import com.risky.jotterbox.data.ObjectBoxNoteManager;
import com.risky.jotterbox.data.ObjectBoxSettingManager;
import com.risky.jotterbox.data.ObjectBoxTagManager;
import com.risky.jotterbox.struct.ElementColor;
import com.risky.jotterbox.utils.NumberUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Set;

public class BoardEditSummaryFragment extends Fragment {
    private ObjectBoxBoardManager boardManager;
    private ObjectBoxNoteManager noteManager;
    private ObjectBoxConnectionManager connectionManager;
    private ObjectBoxTagManager tagManager;
    private ObjectBoxSettingManager settingManager;

    private View view;
    private Board board;
    private boolean doDelete = false;

    private ImageView colorElement;
    private TextView titleElement;
    private TextView idElement;
    private LinearLayout changeBtn;
    private TextView noteNumElement;
    private LinearLayout deleteBtn;
    private TextView createdDateElement;
    private FlexboxLayout tagBox;

    public BoardEditSummaryFragment(Board board) {
        this.board = board;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        boardManager = ObjectBoxBoardManager.get();
        noteManager = ObjectBoxNoteManager.get();
        connectionManager = ObjectBoxConnectionManager.get();
        tagManager = ObjectBoxTagManager.get();
        settingManager = ObjectBoxSettingManager.get();
        view = inflater.inflate(R.layout.fragment_board_summary, container, false);

        // Find elements
        colorElement = view.findViewById(R.id.board_edit_color);
        titleElement = view.findViewById(R.id.board_edit_title);
        idElement = view.findViewById(R.id.board_edit_id);
        changeBtn = view.findViewById(R.id.board_edit_change_btn);
        noteNumElement = view.findViewById(R.id.board_edit_note_num);
        createdDateElement = view.findViewById(R.id.board_edit_created_date);
        tagBox = view.findViewById(R.id.board_edit_tag_item_box);

        Calendar storedDateTime = Calendar.getInstance();
        storedDateTime.setTimeInMillis(board.createdDate);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");

        // Assign data
        colorElement.setImageResource(board.color.getRoundId());
        titleElement.setText(board.name);
        idElement.setText("Board #" + NumberUtil.convertToDisplayId(board.id));
        noteNumElement.setText(board.notes.size() + "");
        createdDateElement.setText(dateFormat.format(storedDateTime.getTime()));

        drawList(board.tags);

        // Find elements
        deleteBtn = view.findViewById(R.id.board_edit_delete_btn);

        // Assign listener
        colorElement.setOnClickListener(view1 -> {
            ElementColor nextColor = board.color.next();
            colorElement.setImageResource(nextColor.getRoundId());
            board.color = nextColor;
            boardManager.update(board);
        });

        titleElement.setOnFocusChangeListener((view1, hasFocus) -> {
            if (!hasFocus) {
                hideKeyboard();

                String enteredTitle = titleElement.getText().toString().trim();
                board.name = enteredTitle.isEmpty() ? "Untitled board" : enteredTitle;
                boardManager.update(board);
            }
        });

        titleElement.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                titleElement.clearFocus();
            }
            return false;
        });

        changeBtn.setOnClickListener(view1 -> {
            FragmentTransaction ft = getParentFragment().getChildFragmentManager().beginTransaction();
            ft.replace(R.id.board_edit_content_container, new BoardEditAllBoardFragment());
            ft.commit();
        });

        noteNumElement.setOnClickListener(view1 -> {
            FragmentTransaction ft = getParentFragment().getChildFragmentManager().beginTransaction();
            ft.replace(R.id.board_edit_content_container, new BoardEditAllNoteFragment(board));
            ft.commit();
        });

        deleteBtn.setOnClickListener(view1 -> {
            for (long noteId : board.notes) {
                Note currentNote = noteManager.findById(noteId);
                for (long connId : currentNote.connection) {
                    if (connectionManager.contains(connId)) {
                        connectionManager.remove(connId);
                    }
                }

                noteManager.remove(currentNote.id);
            }

            for (long tagId : board.tags) {
                tagManager.remove(tagId);
            }

            boardManager.remove(board.id);

            ((MainActivity) getContext()).initializeBoard(
                    boardManager.getAll().size() == 0 ? -1 : boardManager.getAll().get(0).id);
            doDelete = true;
            ((DialogFragment) getParentFragment()).dismiss();
        });

        return view;
    }

    @Override
    public void onDestroy() {
        // De-reference listeners to avoid mem leak
        titleElement.setOnFocusChangeListener(null);
        titleElement.setOnEditorActionListener(null);
        changeBtn.setOnClickListener(null);
        colorElement.setOnClickListener(null);
        deleteBtn.setOnClickListener(null);
        noteNumElement.setOnClickListener(null);

        if (!doDelete) {
            String enteredTitle = titleElement.getText().toString().trim();
            board.name = enteredTitle.isEmpty() ? "Untitled board" : enteredTitle;
            boardManager.update(board);

            ((MainActivity) getContext()).initializeBoard(board.id);
            settingManager.setLastVisitedBoard(board.id);
        }

        super.onDestroy();
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
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
