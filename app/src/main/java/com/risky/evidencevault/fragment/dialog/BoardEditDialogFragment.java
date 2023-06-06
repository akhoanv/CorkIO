package com.risky.evidencevault.fragment.dialog;

import android.app.Activity;
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
import androidx.fragment.app.FragmentTransaction;

import com.risky.evidencevault.MainActivity;
import com.risky.evidencevault.R;
import com.risky.evidencevault.dao.Board;
import com.risky.evidencevault.dao.Connection;
import com.risky.evidencevault.dao.Note;
import com.risky.evidencevault.data.ObjectBoxBoardManager;
import com.risky.evidencevault.data.ObjectBoxConnectionManager;
import com.risky.evidencevault.data.ObjectBoxNoteManager;
import com.risky.evidencevault.data.ObjectBoxTagManager;
import com.risky.evidencevault.fragment.board.BoardEditSummaryFragment;

public class BoardEditDialogFragment extends DialogFragment {

    private View view;
    private Board board;

    public BoardEditDialogFragment(Board board) {
        this.board = board;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        view = inflater.inflate(R.layout.fragment_board_dialog, container, false);

        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.replace(R.id.board_edit_content_container, new BoardEditSummaryFragment(board));
        ft.commit();

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
