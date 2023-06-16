package com.risky.jotterbox.fragment.notepreset;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.risky.jotterbox.R;
import com.risky.jotterbox.dao.ImageNoteData;
import com.risky.jotterbox.dao.Note;
import com.risky.jotterbox.data.ObjectBoxNoteImageDataManager;
import com.risky.jotterbox.data.ObjectBoxNoteManager;
import com.risky.jotterbox.fragment.adapter.NoteImagePagerAdapter;
import com.risky.jotterbox.utils.IntentRequestCode;

import java.util.List;

public class NoteEditSummaryImageFragment extends Fragment implements INoteEditSummaryFragment {
    // Database manager
    private ObjectBoxNoteManager noteManager;
    private ObjectBoxNoteImageDataManager dataManager;

    private View view;
    private Note note;

    private EditText titleElement;
    private TextView idElement;
    private ViewPager photoPage;
    private TabLayout photoTab;
    private ImageView addBtn;

    private LinearLayout checkmarkBtn;
    private ImageView checkmarkBtnImg;
    private LinearLayout deleteBtn;

    private NoteImagePagerAdapter pagerAdapter;

    public NoteEditSummaryImageFragment(Note note) {
        this.note = note;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        noteManager = ObjectBoxNoteManager.get();
        dataManager = ObjectBoxNoteImageDataManager.get();
        view = inflater.inflate(R.layout.fragment_note_summary_image, container, false);

        // Find elements
        titleElement = view.findViewById(R.id.note_edit_title);
        idElement = view.findViewById(R.id.note_edit_id);
        photoPage = view.findViewById(R.id.note_edit_photos_viewpager);
        photoTab = view.findViewById(R.id.note_edit_photos_tab);
        addBtn = view.findViewById(R.id.note_edit_image_add_btn);

        checkmarkBtn = view.findViewById(R.id.note_edit_image_checkmark);
        checkmarkBtnImg = view.findViewById(R.id.note_edit_image_checkmark_img);
        deleteBtn = view.findViewById(R.id.note_edit_image_delete);

        ImageNoteData data = dataManager.findById(note.dataId);

        // Assign appropriate data
        idElement.setText("Note #" + note.getDisplayId());
        titleElement.setHint(note.type.getInitialTitle());
        titleElement.setText(note.title.equals(note.type.getInitialTitle()) ? "" : note.title);

        List<String> initialDataset = data.list;
        pagerAdapter = new NoteImagePagerAdapter(getContext(), initialDataset);

        photoPage.setAdapter(pagerAdapter);
        photoTab.setupWithViewPager(photoPage, true);

        checkmarkBtnImg.setTag(false);

        if (!initialDataset.isEmpty()) {
            checkmarkBtn.setVisibility(View.VISIBLE);
            deleteBtn.setVisibility(View.VISIBLE);

            if (data.iconIndex == 0) {
                toggleUseCheckbox(true);
            }
        }

        // Set onChangeListener to update the database
        addBtn.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, IntentRequestCode.IMAGE_PICKER.ordinal());
        });

        titleElement.setOnFocusChangeListener((view, hasFocus) -> {
            if (!hasFocus) {
                String enteredTitle = titleElement.getText().toString().trim();
                note.title = enteredTitle.isEmpty() ? note.type.getInitialTitle() : enteredTitle;
                noteManager.update(note);

                hideKeyboard();
            }
        });

        titleElement.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                titleElement.clearFocus();
            }
            return false;
        });

        photoPage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageScrollStateChanged(int state) {}

            @Override
            public void onPageSelected(int position) {
                checkboxUpdate(position);
            }
        });

        checkmarkBtn.setOnClickListener(view -> {
            if (isUseCheckboxChecked()) {
                toggleUseCheckbox(false);

                ImageNoteData updatedData = dataManager.findById(note.dataId);
                updatedData.iconIndex = -1;
                dataManager.update(updatedData);

                note.customIconPath = "";
                noteManager.update(note);

                return;
            }

            toggleUseCheckbox(true);

            ImageNoteData updatedData = dataManager.findById(note.dataId);
            updatedData.iconIndex = photoPage.getCurrentItem();
            dataManager.update(updatedData);

            note.customIconPath = updatedData.list.get(photoPage.getCurrentItem());
            noteManager.update(note);
        });

        deleteBtn.setOnClickListener(view -> removeCurrent());

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null){
            return;
        }

        if (requestCode == IntentRequestCode.IMAGE_PICKER.ordinal()) {
            ImageNoteData updatedData = dataManager.findById(note.dataId);

            updatedData.list.add(data.getData().toString());
            dataManager.update(updatedData);

            pagerAdapter.add(data.getData().toString());
            checkmarkBtn.setVisibility(View.VISIBLE);
            deleteBtn.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroy() {
        // Update content
        String enteredTitle = titleElement.getText().toString().trim();
        note.title = enteredTitle.isEmpty() ? note.type.getInitialTitle() : enteredTitle;

        noteManager.update(note);

        // Set these listener to null, avoid mem leak
        titleElement.setOnFocusChangeListener(null);
        titleElement.setOnEditorActionListener(null);
        addBtn.setOnClickListener(null);

        super.onDestroy();
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private boolean isUseCheckboxChecked() {
        return (Boolean) checkmarkBtnImg.getTag();
    }

    private void toggleUseCheckbox(boolean isChecked) {
        checkmarkBtnImg.setImageResource(isChecked ? R.drawable.checkbox_filled_white : R.drawable.checkbox_empty_white);
        checkmarkBtnImg.setTag(isChecked);
    }

    private void checkboxUpdate(int position) {
        ImageNoteData updatedData = dataManager.findById(note.dataId);

        if (updatedData.iconIndex == position) {
            toggleUseCheckbox(true);

            return;
        }

        toggleUseCheckbox(false);
    }

    private void removeCurrent() {
        ImageNoteData updatedData = dataManager.findById(note.dataId);
        int currentSelected = photoPage.getCurrentItem();
        int currentIcon = updatedData.iconIndex;

        // Change icon index accordingly
        if (currentIcon == currentSelected) {
            note.customIconPath = "";
            noteManager.update(note);

            updatedData.iconIndex = -1;

            // UI update
            photoPage.setCurrentItem(currentSelected - 1);
        } else if (currentIcon > currentSelected) {
            updatedData.iconIndex = updatedData.iconIndex - 1;
        }

        // Update database
        updatedData.list.remove(currentSelected);
        dataManager.update(updatedData);

        // Update UI
        pagerAdapter.remove(currentSelected);
        checkboxUpdate(photoPage.getCurrentItem());

        // Actions when list is empty
        if (updatedData.list.size() == 0) {
            checkmarkBtn.setVisibility(View.GONE);
            deleteBtn.setVisibility(View.GONE);
        }
    }
}
