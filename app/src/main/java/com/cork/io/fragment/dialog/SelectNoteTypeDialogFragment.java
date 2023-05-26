package com.cork.io.fragment.dialog;

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

import com.cork.io.R;
import com.cork.io.fragment.adapter.NoteTypeSelectAdapter;
import com.cork.io.struct.NoteType;
import com.cork.io.utils.NewNoteCallback;

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
}
