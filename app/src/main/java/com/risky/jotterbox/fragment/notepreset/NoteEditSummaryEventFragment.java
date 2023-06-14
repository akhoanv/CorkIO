package com.risky.jotterbox.fragment.notepreset;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.risky.jotterbox.R;
import com.risky.jotterbox.dao.EventNoteData;
import com.risky.jotterbox.dao.Note;
import com.risky.jotterbox.data.ObjectBoxNoteEventDataManager;
import com.risky.jotterbox.data.ObjectBoxNoteManager;
import com.risky.jotterbox.utils.IntentRequestCode;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NoteEditSummaryEventFragment extends Fragment implements INoteEditSummaryFragment {
    // Database manager
    private ObjectBoxNoteManager noteManager;
    private ObjectBoxNoteEventDataManager dataManager;

    private View view;
    private Note note;
    private Calendar storedDateTime = Calendar.getInstance();

    private TextView idElement;
    private ImageView iconElement;
    private EditText titleElement;
    private CalendarView calendarElement;
    private ImageView timeIcon;
    private EditText timeElement;
    private LinearLayout reminderBtn;

    public NoteEditSummaryEventFragment(Note note) {
        this.note = note;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        noteManager = ObjectBoxNoteManager.get();
        dataManager = ObjectBoxNoteEventDataManager.get();
        view = inflater.inflate(R.layout.fragment_note_summary_event, container, false);

        // Find elements
        idElement = view.findViewById(R.id.note_edit_id);
        titleElement = view.findViewById(R.id.note_edit_title);
        calendarElement = view.findViewById(R.id.note_edit_calendar);
        timeElement = view.findViewById(R.id.note_edit_time);
        timeIcon = view.findViewById(R.id.note_edit_time_icon);
        reminderBtn = view.findViewById(R.id.note_edit_reminder_btn);
        iconElement = view.findViewById(R.id.note_edit_icon);

        EventNoteData data = dataManager.findById(note.dataId);
        storedDateTime.setTimeInMillis(data.datetime);
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

        // Assign appropriate data
        idElement.setText("Note #" + note.getDisplayId());
        titleElement.setText(note.title);
        calendarElement.setDate(data.datetime);
        timeElement.setText(timeFormat.format(storedDateTime.getTime()));

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

        // Set listeners
        titleElement.setOnFocusChangeListener((view1, hasFocus) -> {
            if (!hasFocus) {
                hideKeyboard();
            }
        });

        titleElement.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                titleElement.clearFocus();
            }
            return false;
        });

        calendarElement.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                storedDateTime.set(i, i1, i2);
                data.datetime = storedDateTime.getTimeInMillis();

                dataManager.update(data);
            }
        });

        timeElement.setOnClickListener(view1 -> showTimePicker(data));

        timeIcon.setOnClickListener(view1 -> showTimePicker(data));

        reminderBtn.setOnClickListener(view -> {
            if (titleElement.getText() == null) {
                return;
            }

            Intent intent = new Intent(Intent.ACTION_EDIT);
            intent.setType("vnd.android.cursor.item/event");
            intent.putExtra("beginTime", storedDateTime.getTimeInMillis());
            intent.putExtra("allDay", false);
            intent.putExtra("title", titleElement.getText().toString());
            startActivity(intent);
        });

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
        // Set these listener to null, avoid mem leak
        titleElement.setOnFocusChangeListener(null);
        titleElement.setOnEditorActionListener(null);
        calendarElement.setOnDateChangeListener(null);
        timeElement.setOnClickListener(null);
        timeIcon.setOnClickListener(null);
        iconElement.setOnClickListener(null);
        iconElement.setOnLongClickListener(null);

        super.onDestroy();
    }

    private void showTimePicker(EventNoteData data) {
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(getContext(), R.style.DefaultPickerTheme,(timePicker, selectedHour, selectedMinute) -> {
            timeElement.setText(String.format("%02d", selectedHour) + ":" + String.format("%02d", selectedMinute));

            storedDateTime.set(Calendar.HOUR_OF_DAY, selectedHour);
            storedDateTime.set(Calendar.MINUTE, selectedMinute);

            data.datetime = storedDateTime.getTimeInMillis();
            dataManager.update(data);
        }, storedDateTime.get(Calendar.HOUR_OF_DAY), storedDateTime.get(Calendar.MINUTE), true);
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
