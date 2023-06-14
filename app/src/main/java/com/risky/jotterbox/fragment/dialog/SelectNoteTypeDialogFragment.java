package com.risky.jotterbox.fragment.dialog;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.risky.jotterbox.R;
import com.risky.jotterbox.fragment.adapter.NoteTypeSelectAdapter;
import com.risky.jotterbox.struct.NoteType;
import com.risky.jotterbox.utils.NewNoteCallback;

import java.util.Arrays;

public class SelectNoteTypeDialogFragment extends DialogFragment {
    private View view;
    private GridView typeGrid;
    private NewNoteCallback callback;

    public SelectNoteTypeDialogFragment(NewNoteCallback callback) {
        this.callback = callback;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        view = inflater.inflate(R.layout.fragment_select_note_type, container, false);

        // Find elements
        typeGrid = view.findViewById(R.id.note_type_select_grid);

        typeGrid.setAdapter(new NoteTypeSelectAdapter(getContext(), R.layout.fragment_note_type_select_item,
            Arrays.asList(NoteType.values()), type -> {
                callback.run(type);
                SelectNoteTypeDialogFragment.this.dismiss();
            }
        ));

        return view;
    }

    @Override
    public void onDestroy() {
        View decorView = ((Activity) getContext()).getWindow().getDecorView();
        // Hide both the navigation bar and the status bar.
        // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
        // a general rule, you should design your app to hide the status bar whenever you
        // hide the navigation bar.
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);

        super.onDestroy();
    }
}
