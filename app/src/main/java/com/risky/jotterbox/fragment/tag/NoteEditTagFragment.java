package com.risky.jotterbox.fragment.tag;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.flexbox.FlexboxLayout;
import com.risky.jotterbox.R;
import com.risky.jotterbox.dao.Board;
import com.risky.jotterbox.dao.Note;
import com.risky.jotterbox.dao.Tag;
import com.risky.jotterbox.data.ObjectBoxBoardManager;
import com.risky.jotterbox.data.ObjectBoxNoteManager;
import com.risky.jotterbox.data.ObjectBoxTagManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class NoteEditTagFragment extends Fragment {
    // Database manager
    private ObjectBoxNoteManager noteManager;
    private ObjectBoxTagManager tagManager;
    private ObjectBoxBoardManager boardManager;

    private View view;
    private Note note;

    private FlexboxLayout listElement;

    public NoteEditTagFragment(Note note) {
        this.note = note;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        noteManager = ObjectBoxNoteManager.get();
        tagManager = ObjectBoxTagManager.get();
        boardManager = ObjectBoxBoardManager.get();
        view = inflater.inflate(R.layout.fragment_note_tag, container, false);

        // Find elements
        listElement = view.findViewById(R.id.note_edit_tag_item_box);

        Set<Long> tagSet = note.tag;
        drawList(tagSet);

        return view;
    }

    private void drawList(Set<Long> items) {
        listElement.removeAllViews();
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

            listItem.setOnLongClickListener(view -> {
                int position = listElement.indexOfChild(listItem);
                List<Long> list = new ArrayList<>(note.tag);

                long currentTagId = list.get(position);

                Tag currentTag = tagManager.findById(currentTagId);
                if (currentTag.relatedNotes.size() == 1) {
                    tagManager.remove(currentTagId);

                    Board board = boardManager.findById(currentTag.boardId);
                    board.tags.remove(currentTagId);
                    boardManager.update(board);
                } else {
                    currentTag.relatedNotes.remove(note.id);
                    tagManager.update(currentTag);
                }

                note.tag.remove(currentTagId);
                noteManager.update(note);

                listElement.removeView(listItem);

                return true;
            });

            listElement.addView(listItem);

            // Add spacing between items
            ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) listItem.getLayoutParams();
            lp.bottomMargin = margin;
            lp.rightMargin = margin;
        }

        // Add button
        LinearLayout addBtn = new LinearLayout(getContext());

        ImageView addImg = new ImageView(getContext());
        addImg.setImageResource(R.drawable.add_gray);
        addImg.setLayoutParams(new LinearLayout.LayoutParams((int) (17*scale + 0.5f), (int) (17*scale + 0.5f)));
        addBtn.addView(addImg);

        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) addImg.getLayoutParams();
        lp.rightMargin = 5;

        TextView addTxt = new TextView(getContext());
        addTxt.setText("Add New");
        addTxt.setTextColor(getContext().getColor(R.color.text_gray));
        addTxt.setTextSize(13f);
        addTxt.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        addBtn.addView(addTxt);

        addBtn.setBackgroundResource(R.drawable.note_id_background_dotted);
        addBtn.setGravity(Gravity.CENTER);
        addBtn.setPadding(paddingHorz, paddingVert, paddingHorz, paddingVert);

        addBtn.setOnClickListener(view1 -> {
            FragmentTransaction ft = getParentFragment().getChildFragmentManager().beginTransaction();
            ft.replace(R.id.note_edit_content_container, new NoteEditTagAddFragment(note));
            ft.commit();
        });

        listElement.addView(addBtn);

        // Add spacing between items
        lp = (ViewGroup.MarginLayoutParams) addBtn.getLayoutParams();
        lp.bottomMargin = margin;
        lp.rightMargin = margin;
    }
}
