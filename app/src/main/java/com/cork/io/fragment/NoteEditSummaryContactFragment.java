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
import com.cork.io.dao.Note;
import com.cork.io.data.NoteManager;
import com.cork.io.data.ObjectBoxNoteManager;
import com.cork.io.utils.IntentRequestCode;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class NoteEditSummaryContactFragment  extends Fragment implements INoteEditSummaryFragment {

    // Database manager
    private NoteManager noteManager;

    private View view;
    private Note note;

    private TextView idElement;
    private EditText firstNameElement;
    private EditText lastNameElement;
    private EditText emailElement;
    private EditText phoneElement;
    private EditText commentElement;
    private ImageView iconElement;

    public NoteEditSummaryContactFragment(Note note) {
        this.note = note;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        noteManager = ObjectBoxNoteManager.get();
        view = inflater.inflate(R.layout.fragment_note_summary_contact, container, false);

        // Find elements
        idElement = view.findViewById(R.id.note_edit_id);
        firstNameElement = view.findViewById(R.id.note_edit_first_name);
        lastNameElement = view.findViewById(R.id.note_edit_last_name);
        emailElement = view.findViewById(R.id.note_edit_email);
        phoneElement = view.findViewById(R.id.note_edit_phone_number);
        commentElement = view.findViewById(R.id.note_edit_content);
        iconElement = view.findViewById(R.id.note_edit_icon);

        // Assign appropriate data
        firstNameElement.setText(note.firstName);
        lastNameElement.setText(note.lastName);
        emailElement.setText(note.emailAddress);
        phoneElement.setText(note.phoneNumber);
        idElement.setText("Note #" + note.id);
        commentElement.setText(note.content);

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
        firstNameElement.setOnFocusChangeListener((view, hasFocus) -> {
            if (!hasFocus) {
                note.firstName = firstNameElement.getText().toString();
                String fullName = (note.firstName + " " + note.lastName).trim();
                note.title = fullName.isEmpty() ? note.type.getInitialTitle() : fullName;
                noteManager.updateNote(note);
            }
        });

        lastNameElement.setOnFocusChangeListener((view, hasFocus) -> {
            if (!hasFocus) {
                note.lastName = lastNameElement.getText().toString();
                String fullName = (note.firstName + " " + note.lastName).trim();
                note.title = fullName.isEmpty() ? note.type.getInitialTitle() : fullName;
                noteManager.updateNote(note);
            }
        });

        emailElement.setOnFocusChangeListener((view, hasFocus) -> {
            if (!hasFocus) {
                note.emailAddress = emailElement.getText().toString();
                noteManager.updateNote(note);
            }
        });

        phoneElement.setOnFocusChangeListener((view, hasFocus) -> {
            if (!hasFocus) {
                note.phoneNumber= phoneElement.getText().toString();
                noteManager.updateNote(note);
            }
        });

        commentElement.setOnFocusChangeListener((view, hasFocus) -> {
            if (!hasFocus) {
                note.content = commentElement.getText().toString();
                noteManager.updateNote(note);
            }
        });

        iconElement.setOnClickListener(view -> {
            Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
            getIntent.setType("image/*");

            Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickIntent.setType("image/*");

            Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

            startActivityForResult(chooserIntent, IntentRequestCode.IMAGE_PICKER.ordinal());
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
        // Update content
        note.firstName = firstNameElement.getText().toString();
        note.lastName = lastNameElement.getText().toString();
        note.emailAddress = emailElement.getText().toString();
        note.phoneNumber = phoneElement.getText().toString();
        note.content = commentElement.getText().toString();

        String fullName = (note.firstName + " " + note.lastName).trim();
        note.title = fullName.isEmpty() ? note.type.getInitialTitle() : fullName;

        noteManager.updateNote(note);

        // Set these listener to null, avoid mem leak
        firstNameElement.setOnFocusChangeListener(null);
        lastNameElement.setOnFocusChangeListener(null);
        emailElement.setOnFocusChangeListener(null);
        phoneElement.setOnFocusChangeListener(null);
        commentElement.setOnFocusChangeListener(null);
        iconElement.setOnClickListener(null);
        iconElement.setOnLongClickListener(null);

        super.onDestroy();
    }
}
