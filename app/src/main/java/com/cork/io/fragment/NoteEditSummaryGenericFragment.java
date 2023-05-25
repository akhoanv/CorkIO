package com.cork.io.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cork.io.R;
import com.cork.io.dao.BaseNoteData;
import com.cork.io.dao.ContactNoteData;
import com.cork.io.dao.Note;
import com.cork.io.data.NoteManager;
import com.cork.io.data.ObjectBoxNoteGenericDataManager;
import com.cork.io.data.ObjectBoxNoteManager;
import com.cork.io.utils.IntentRequestCode;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class NoteEditSummaryGenericFragment extends Fragment implements INoteEditSummaryFragment{
    // Database manager
    private NoteManager noteManager;
    private ObjectBoxNoteGenericDataManager dataManager;

    private View view;
    private Note note;

    private EditText titleElement;
    private EditText contentElement;
    private TextView idElement;
    private ImageView iconElement;

    public NoteEditSummaryGenericFragment(Note note) {
        this.note = note;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        noteManager = ObjectBoxNoteManager.get();
        dataManager = ObjectBoxNoteGenericDataManager.get();

        view = inflater.inflate(R.layout.fragment_note_summary_generic, container, false);

        // Find elements
        titleElement = view.findViewById(R.id.note_edit_title);
        idElement = view.findViewById(R.id.note_edit_id);
        contentElement = view.findViewById(R.id.note_edit_content);
        iconElement = view.findViewById(R.id.note_edit_icon);

        BaseNoteData data = dataManager.findById(note.dataId);

        // Assign appropriate data
        titleElement.setText(note.title);
        idElement.setText("Note #" + note.id);
        contentElement.setText(data.content);

        if (note.customIconPath.isEmpty()) {
            iconElement.setImageResource(note.type.getIcon().getId());
        } else {
            try {
                InputStream inputStream = getContext().getContentResolver().openInputStream(Uri.parse(note.customIconPath));
                Bitmap importedImg = BitmapFactory.decodeStream(new BufferedInputStream(inputStream));
                iconElement.setImageBitmap(importedImg);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        // Set onChangeListener to update the database
        titleElement.setOnFocusChangeListener((view, hasFocus) -> {
            if (!hasFocus) {
                String enteredTitle = titleElement.getText().toString().trim();
                note.title = enteredTitle.isEmpty() ? note.type.getInitialTitle() : enteredTitle;
                noteManager.updateNote(note);
            }
        });

        contentElement.setOnFocusChangeListener((view, hasFocus) -> {
            if (!hasFocus) {
                data.content = contentElement.getText().toString();
                dataManager.update(data);
            }
        });

        iconElement.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, IntentRequestCode.IMAGE_PICKER.ordinal());
        });

        iconElement.setOnLongClickListener(view -> {
            iconElement.setImageResource(note.type.getIcon().getId());
            note.customIconPath = "";
            noteManager.updateNote(note);

            return true;
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null){
            return;
        }

        if (requestCode == IntentRequestCode.IMAGE_PICKER.ordinal()) {
            try {
                InputStream inputStream = getContext().getContentResolver().openInputStream(data.getData());
                Bitmap importedImg = BitmapFactory.decodeStream(new BufferedInputStream(inputStream));
                iconElement.setImageBitmap(importedImg);

                note.customIconPath = data.getData().toString();
                noteManager.updateNote(note);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        BaseNoteData data = dataManager.findById(note.dataId);

        // Update content
        String enteredTitle = titleElement.getText().toString().trim();
        note.title = enteredTitle.isEmpty() ? note.type.getInitialTitle() : enteredTitle;
        data.content = contentElement.getText().toString();

        noteManager.updateNote(note);
        dataManager.update(data);

        // Set these listener to null, avoid mem leak
        titleElement.setOnFocusChangeListener(null);
        contentElement.setOnFocusChangeListener(null);
        iconElement.setOnClickListener(null);
        iconElement.setOnLongClickListener(null);

        super.onDestroy();
    }
}
