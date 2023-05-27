package com.risky.evidencevault.fragment.notepreset;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
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

import com.risky.evidencevault.R;
import com.risky.evidencevault.dao.LocationNoteData;
import com.risky.evidencevault.dao.Note;
import com.risky.evidencevault.data.NoteManager;
import com.risky.evidencevault.data.ObjectBoxNoteLocationDataManager;
import com.risky.evidencevault.data.ObjectBoxNoteManager;
import com.risky.evidencevault.utils.IntentRequestCode;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class NoteEditSummaryLocationFragment extends Fragment implements INoteEditSummaryFragment {

    // Database manager
    private NoteManager noteManager;
    private ObjectBoxNoteLocationDataManager dataManager;

    private View view;
    private Note note;

    private TextView idElement;
    private EditText titleElement;
    private EditText addressElement;
    private ImageView iconElement;
    private LinearLayout findOnMapBtn;

    public NoteEditSummaryLocationFragment(Note note) {
        this.note = note;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        noteManager = ObjectBoxNoteManager.get();
        dataManager = ObjectBoxNoteLocationDataManager.get();
        view = inflater.inflate(R.layout.fragment_note_summary_location, container, false);

        // Find elements
        idElement = view.findViewById(R.id.note_edit_id);
        titleElement = view.findViewById(R.id.note_edit_title);
        addressElement = view.findViewById(R.id.note_edit_address);
        iconElement = view.findViewById(R.id.note_edit_icon);
        findOnMapBtn = view.findViewById(R.id.note_edit_find_map_btn);

        LocationNoteData data = dataManager.findById(note.dataId);

        // Assign appropriate data
        idElement.setText("Note #" + note.getDisplayId());
        titleElement.setText(note.title);
        addressElement.setText(data.address);

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
            String enteredTitle = titleElement.getText().toString().trim();
            note.title = enteredTitle.isEmpty() ? note.type.getInitialTitle() : enteredTitle;
            noteManager.updateNote(note);

            hideKeyboard();
        });

        addressElement.setOnFocusChangeListener((view, hasFocus) -> {
            if (!hasFocus) {
                data.address = addressElement.getText().toString().trim();

                dataManager.update(data);

                hideKeyboard();
            }
        });

        addressElement.setImeOptions(EditorInfo.IME_ACTION_DONE);
        addressElement.setRawInputType(InputType.TYPE_CLASS_TEXT);

        findOnMapBtn.setOnClickListener(view1 -> {
            hideKeyboard();

            String query = "";
            if (addressElement.getText() != null && !addressElement.getText().toString().trim().isEmpty()) {
                query = addressElement.getText().toString().trim().replace(" ", "+");
            } else if (titleElement.getText() != null && !titleElement.getText().toString().trim().equals(note.type.getInitialTitle())) {
                query = titleElement.getText().toString().trim().replace(" ", "+");;
            }

            if (!query.isEmpty()) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("geo:0,0?q=" + query));
                startActivity(intent);
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
        LocationNoteData data = dataManager.findById(note.dataId);

        // Update content
        data.address = addressElement.getText().toString().trim();

        String enteredTitle = titleElement.getText().toString().trim();
        note.title = enteredTitle.isEmpty() ? note.type.getInitialTitle() : enteredTitle;
        noteManager.updateNote(note);

        noteManager.updateNote(note);
        dataManager.update(data);

        // Set these listener to null, avoid mem leak
        titleElement.setOnFocusChangeListener(null);
        addressElement.setOnFocusChangeListener(null);
        iconElement.setOnClickListener(null);
        iconElement.setOnLongClickListener(null);

        super.onDestroy();
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
