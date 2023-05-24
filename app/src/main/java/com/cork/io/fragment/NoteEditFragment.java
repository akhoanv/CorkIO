package com.cork.io.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.cork.io.R;
import com.cork.io.dao.Note;
import com.cork.io.data.NoteManager;
import com.cork.io.data.ObjectBoxNoteManager;
import com.cork.io.utils.NoteEditCallback;

/**
 * Fragment for editing note
 *
 * @author knguyen
 */
public class NoteEditFragment extends DialogFragment {
    // Database manager
    private NoteManager noteManager;

    private View view;
    private Note note;
    private NoteEditCallback callback;
    private boolean doDelete = false;

    // Elements
    private ImageView unpinBtn;
    private LinearLayout summaryTabBtn;
    private LinearLayout connectionTabBtn;

    public NoteEditFragment(Note note) {
        this.note = note;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        noteManager = ObjectBoxNoteManager.get();
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        view = inflater.inflate(R.layout.fragment_note_dialog, container, false);

        // Find elements
        unpinBtn = view.findViewById(R.id.note_edit_unpin);
        summaryTabBtn = view.findViewById(R.id.note_edit_summary_btn);
        connectionTabBtn = view.findViewById(R.id.note_edit_connection_btn);

        // Unpin button
        unpinBtn.setOnClickListener(view -> {
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
        unpinBtn.setOnClickListener(null);
        summaryTabBtn.setOnClickListener(null);
        connectionTabBtn.setOnClickListener(null);

        super.onDestroy();
    }
}
