package com.cork.io.fragment;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cork.io.R;
import com.cork.io.dao.EventNoteData;
import com.cork.io.dao.Note;
import com.cork.io.data.NoteManager;
import com.cork.io.data.ObjectBoxNoteEventDataManager;
import com.cork.io.data.ObjectBoxNoteManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NoteEditSummaryEventFragment extends Fragment implements INoteEditSummaryFragment {
    // Database manager
    private NoteManager noteManager;
    private ObjectBoxNoteEventDataManager dataManager;

    private View view;
    private Note note;
    private Calendar storedDateTime = Calendar.getInstance();

    private TextView idElement;
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

        EventNoteData data = dataManager.findById(note.dataId);
        storedDateTime.setTimeInMillis(data.datetime);
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

        // Assign appropriate data
        idElement.setText("Note #" + note.id);
        titleElement.setText(note.title);
        calendarElement.setDate(data.datetime);
        timeElement.setText(timeFormat.format(storedDateTime.getTime()));

        // Set onChangeListener to update the database
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

        return view;
    }

    @Override
    public void onDestroy() {
        // Set these listener to null, avoid mem leak
        calendarElement.setOnDateChangeListener(null);
        timeElement.setOnClickListener(null);
        timeIcon.setOnClickListener(null);

        super.onDestroy();
    }

    private void showTimePicker(EventNoteData data) {
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(getContext(), (timePicker, selectedHour, selectedMinute) -> {
            timeElement.setText(String.format("%02d", selectedHour) + ":" + String.format("%02d", selectedMinute));

            storedDateTime.set(Calendar.HOUR_OF_DAY, selectedHour);
            storedDateTime.set(Calendar.MINUTE, selectedMinute);

            data.datetime = storedDateTime.getTimeInMillis();
            dataManager.update(data);
        }, storedDateTime.get(Calendar.HOUR_OF_DAY), storedDateTime.get(Calendar.MINUTE), true);
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }
}
