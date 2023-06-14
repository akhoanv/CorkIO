package com.risky.jotterbox.fragment.dialog;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.risky.jotterbox.R;
import com.risky.jotterbox.dao.Note;
import com.risky.jotterbox.data.ObjectBoxNoteManager;
import com.risky.jotterbox.fragment.connection.NoteEditConnectionFragment;
import com.risky.jotterbox.fragment.tag.NoteEditTagFragment;
import com.risky.jotterbox.utils.NoteEditCallback;

/**
 * Fragment for editing note
 *
 * @author knguyen
 */
public class NoteEditDialogFragment extends DialogFragment {
    // Database manager
    private ObjectBoxNoteManager noteManager;

    private View view;
    private Note note;
    private NoteEditCallback callback;
    private boolean doDelete = false;

    // Elements
    private LinearLayout deleteBtn;
    private LinearLayout summaryTabBtn;
    private LinearLayout connectionTabBtn;
    private LinearLayout tagTabBtn;

    public NoteEditDialogFragment(Note note) {
        this.note = note;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        noteManager = ObjectBoxNoteManager.get();
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        view = inflater.inflate(R.layout.fragment_note_dialog, container, false);

        // Find elements
        deleteBtn = view.findViewById(R.id.note_edit_delete_btn);
        summaryTabBtn = view.findViewById(R.id.note_edit_summary_btn);
        connectionTabBtn = view.findViewById(R.id.note_edit_connection_btn);
        tagTabBtn = view.findViewById(R.id.note_edit_tag_btn);

        // Unpin button
        deleteBtn.setOnClickListener(view -> {
            doDelete = true;
            this.dismiss();
        });

        summaryTabBtn.setOnClickListener(view -> {
            try {
                FragmentTransaction ft = getChildFragmentManager().beginTransaction();
                ft.replace(R.id.note_edit_content_container, (Fragment) note.type.getFragment()
                        .getConstructor(Note.class).newInstance(new Object[] {note}));
                ft.commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        connectionTabBtn.setOnClickListener(view -> {
            FragmentTransaction ft = getChildFragmentManager().beginTransaction();
            ft.replace(R.id.note_edit_content_container, new NoteEditConnectionFragment(note));
            ft.commit();
        });

        tagTabBtn.setOnClickListener(view -> {
            FragmentTransaction ft = getChildFragmentManager().beginTransaction();
            ft.replace(R.id.note_edit_content_container, new NoteEditTagFragment(note));
            ft.commit();
        });

        try {
            FragmentTransaction ft = getChildFragmentManager().beginTransaction();
            ft.replace(R.id.note_edit_content_container, (Fragment) note.type.getFragment()
                    .getConstructor(Note.class).newInstance(new Object[] {note}));
            ft.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean setFullScreen = false;
        if (getArguments() != null) {
            setFullScreen = getArguments().getBoolean("fullScreen");
        }

        if (setFullScreen)
            setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
    }

    public void setEditCallback(NoteEditCallback callback) {
        this.callback = callback;
    }

    @Override
    public void onDestroy() {
        // Run callback to update data on board
        callback.run(doDelete);

        // Set these listener to null, avoid mem leak
        deleteBtn.setOnClickListener(null);
        summaryTabBtn.setOnClickListener(null);
        connectionTabBtn.setOnClickListener(null);

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
