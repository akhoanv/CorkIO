package com.risky.evidencevault.fragment.notepreset;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.risky.evidencevault.R;
import com.risky.evidencevault.dao.ContactNoteData;
import com.risky.evidencevault.dao.Note;
import com.risky.evidencevault.data.ObjectBoxNoteContactDataManager;
import com.risky.evidencevault.data.ObjectBoxNoteManager;
import com.risky.evidencevault.utils.IntentRequestCode;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NoteEditSummaryContactFragment extends Fragment implements INoteEditSummaryFragment {
    // Database manager
    private ObjectBoxNoteManager noteManager;
    private ObjectBoxNoteContactDataManager dataManager;

    private View view;
    private Note note;

    private TextView idElement;
    private EditText nameElement;
    private EditText bdayElement;
    private EditText emailElement;
    private EditText phoneElement;
    private EditText commentElement;
    private ImageView iconElement;
    private LinearLayout callBtn;
    private LinearLayout smsBtn;
    private LinearLayout emailBtn;

    private Calendar storedDateTime = Calendar.getInstance();

    public NoteEditSummaryContactFragment(Note note) {
        this.note = note;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        noteManager = ObjectBoxNoteManager.get();
        dataManager = ObjectBoxNoteContactDataManager.get();
        view = inflater.inflate(R.layout.fragment_note_summary_contact, container, false);

        // Find elements
        idElement = view.findViewById(R.id.note_edit_id);
        nameElement = view.findViewById(R.id.note_edit_name);
        emailElement = view.findViewById(R.id.note_edit_email);
        phoneElement = view.findViewById(R.id.note_edit_phone_number);
        bdayElement = view.findViewById(R.id.note_edit_bday);
        commentElement = view.findViewById(R.id.note_edit_content);
        iconElement = view.findViewById(R.id.note_edit_icon);

        callBtn = view.findViewById(R.id.note_edit_call_btn);
        smsBtn = view.findViewById(R.id.note_edit_sms_btn);
        emailBtn = view.findViewById(R.id.note_edit_email_btn);

        ContactNoteData data = dataManager.findById(note.dataId);
        storedDateTime.setTimeInMillis(data.bday);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

        // Assign appropriate data
        nameElement.setText(data.name);
        emailElement.setText(data.emailAddress);
        phoneElement.setText(data.phoneNumber);
        bdayElement.setText(data.bday == 0L ? "" : dateFormat.format(storedDateTime.getTime()));
        idElement.setText("Note #" + note.getDisplayId());
        commentElement.setText(data.content);

        if (note.customIconPath.isEmpty()) {
            iconElement.setImageResource(note.type.getIcon().getId());
        } else {
            try {
                InputStream inputStream = getContext().getContentResolver().openInputStream(Uri.parse(note.customIconPath));
                Bitmap importedImg = BitmapFactory.decodeStream(new BufferedInputStream(inputStream));
                iconElement.setImageBitmap(importedImg);
            } catch (Exception e) {
                e.getStackTrace();
                iconElement.setImageResource(note.type.getIcon().getId());

                // Not being able to get it means we gotta reset it
                note.customIconPath = "";
                noteManager.update(note);
            }
        }

        // Set onChangeListener to update the database
        nameElement.setOnFocusChangeListener((view, hasFocus) -> {
            if (!hasFocus) {
                data.name = nameElement.getText().toString().trim();
                note.title = nameElement.getText().toString().trim().isEmpty()
                        ? note.type.getInitialTitle() : nameElement.getText().toString().trim();

                noteManager.update(note);
                dataManager.update(data);

                hideKeyboard();
            }
        });

        nameElement.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                nameElement.clearFocus();
            }
            return false;
        });

        emailElement.setOnFocusChangeListener((view, hasFocus) -> {
            if (!hasFocus) {
                data.emailAddress = emailElement.getText().toString().trim();
                dataManager.update(data);

                hideKeyboard();
            }
        });

        emailElement.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                emailElement.clearFocus();
            }
            return false;
        });

        phoneElement.setOnFocusChangeListener((view, hasFocus) -> {
            if (!hasFocus) {
                data.phoneNumber= phoneElement.getText().toString().trim();
                dataManager.update(data);

                hideKeyboard();
            }
        });

        phoneElement.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                phoneElement.clearFocus();
            }
            return false;
        });

        commentElement.setOnFocusChangeListener((view, hasFocus) -> {
            if (!hasFocus) {
                data.content = commentElement.getText().toString().trim();
                dataManager.update(data);

                hideKeyboard();
            }
        });

        callBtn.setOnClickListener(view1 -> {
            hideKeyboard();

            if (phoneElement.getText() == null || phoneElement.getText().toString().isEmpty()){
                return;
            }

            Intent intent = new Intent(Intent.ACTION_DIAL);

            intent.setData(Uri.parse("tel:" + phoneElement.getText().toString()));
            getContext().startActivity(intent);
        });

        smsBtn.setOnClickListener(view1 -> {
            hideKeyboard();

            if (phoneElement.getText() == null || phoneElement.getText().toString().isEmpty()){
                return;
            }

            Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( "sms:" + phoneElement.getText().toString()));
            intent.putExtra( "sms_body", "");
            startActivity(intent);
        });

        emailBtn.setOnClickListener(view1 -> {
            hideKeyboard();

            if (emailElement.getText() == null || emailElement.getText().toString().isEmpty()){
                return;
            }

            Intent intent = new Intent(Intent.ACTION_SEND);

            intent.setType("text/plain");
            intent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{emailElement.getText().toString()});
            intent.setType("message/rfc822");

            try {
                startActivity(Intent.createChooser(intent, "Send email using..."));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(getActivity(), "No email clients found", Toast.LENGTH_SHORT).show();
            }
        });

        bdayElement.setOnClickListener(view -> showDatePicker(data));

        iconElement.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, IntentRequestCode.IMAGE_PICKER.ordinal());
        });

        iconElement.setOnLongClickListener(view -> {
            iconElement.setImageResource(note.type.getIcon().getId());
            note.customIconPath = "";
            noteManager.update(note);

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
                noteManager.update(note);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        ContactNoteData data = dataManager.findById(note.dataId);

        // Update content
        data.name = nameElement.getText().toString().trim();
        data.emailAddress = emailElement.getText().toString().trim();
        data.phoneNumber = phoneElement.getText().toString().trim();
        data.content = commentElement.getText().toString().trim();

        note.title = nameElement.getText().toString().trim().isEmpty()
                ? note.type.getInitialTitle() : nameElement.getText().toString().trim();

        noteManager.update(note);
        dataManager.update(data);

        // Set these listener to null, avoid mem leak
        nameElement.setOnFocusChangeListener(null);
        nameElement.setOnEditorActionListener(null);
        emailElement.setOnFocusChangeListener(null);
        emailElement.setOnEditorActionListener(null);
        phoneElement.setOnFocusChangeListener(null);
        phoneElement.setOnEditorActionListener(null);
        commentElement.setOnFocusChangeListener(null);
        iconElement.setOnClickListener(null);
        iconElement.setOnLongClickListener(null);
        callBtn.setOnClickListener(null);
        smsBtn.setOnClickListener(null);
        emailBtn.setOnClickListener(null);
        bdayElement.setOnClickListener(null);

        super.onDestroy();
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void showDatePicker(ContactNoteData data) {
        DatePickerDialog mDatePicker;
        mDatePicker = new DatePickerDialog(getContext(), R.style.DefaultPickerTheme, (datePicker, year, month, dayOfMonth) -> {
            bdayElement.setText(String.format("%04d", year) + "/" + String.format("%02d", month)
                    + "/" + String.format("%02d", dayOfMonth));

            storedDateTime.set(Calendar.YEAR, year);
            storedDateTime.set(Calendar.MONTH, month);
            storedDateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            data.bday = storedDateTime.getTimeInMillis();
            dataManager.update(data);
        }, storedDateTime.get(Calendar.YEAR), storedDateTime.get(Calendar.MONTH), storedDateTime.get(Calendar.DAY_OF_MONTH));
        mDatePicker.setTitle("Select Date");
        mDatePicker.show();
    }
}
