package com.risky.jotterbox.fragment.notepreset;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.risky.jotterbox.R;
import com.risky.jotterbox.dao.Note;
import com.risky.jotterbox.dao.WebLinkNoteData;
import com.risky.jotterbox.data.ObjectBoxNoteManager;
import com.risky.jotterbox.data.ObjectBoxNoteWebLinkDataManager;
import com.risky.jotterbox.utils.IntentRequestCode;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class NoteEditSummaryWebLinkFragment extends Fragment implements INoteEditSummaryFragment {
    // Database manager
    private ObjectBoxNoteManager noteManager;
    private ObjectBoxNoteWebLinkDataManager dataManager;

    private View view;
    private Note note;

    private TextView idElement;
    private EditText titleElement;
    private ImageView iconElement;
    private ImageView qrContainer;
    private EditText webLinkElement;
    private LinearLayout visitWebBtn;

    public NoteEditSummaryWebLinkFragment(Note note) {
        this.note = note;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        noteManager = ObjectBoxNoteManager.get();
        dataManager = ObjectBoxNoteWebLinkDataManager.get();
        view = inflater.inflate(R.layout.fragment_note_summary_web_link, container, false);

        // Find elements
        titleElement = view.findViewById(R.id.note_edit_title);
        idElement = view.findViewById(R.id.note_edit_id);
        iconElement = view.findViewById(R.id.note_edit_icon);
        qrContainer = view.findViewById(R.id.note_edit_qr_container);
        webLinkElement = view.findViewById(R.id.note_edit_web_link);
        visitWebBtn = view.findViewById(R.id.note_edit_visit_web_btn);

        WebLinkNoteData data = dataManager.findById(note.dataId);

        // Assign appropriate data
        idElement.setText("Note #" + note.getDisplayId());
        titleElement.setText(note.title);
        webLinkElement.setText(data.url);

        if (URLUtil.isValidUrl(data.url)) {
            setQrCode();
        }

        // Set listeners
        titleElement.setOnFocusChangeListener((view, hasFocus) -> {
            String enteredTitle = titleElement.getText().toString().trim();
            note.title = enteredTitle.isEmpty() ? note.type.getInitialTitle() : enteredTitle;
            noteManager.update(note);

            hideKeyboard();
        });

        titleElement.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                titleElement.clearFocus();
            }
            return false;
        });

        webLinkElement.setOnFocusChangeListener((view1, hasFocus) -> {
            if (!hasFocus) {
                data.url = webLinkElement.getText().toString().trim();
                dataManager.update(data);

                hideKeyboard();

                if (URLUtil.isValidUrl(webLinkElement.getText().toString().trim())) {
                    setQrCode();
                } else {
                    qrContainer.setImageResource(0);
                }
            }
        });

        webLinkElement.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                webLinkElement.clearFocus();
            }
            return false;
        });

        visitWebBtn.setOnClickListener(view1 -> {
            String url = webLinkElement.getText().toString().trim();

            if (!url.isEmpty() && URLUtil.isValidUrl(url)) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
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
            noteManager.update(note);

            return true;
        });

        return view;
    }

    @Override
    public void onDestroy() {
        WebLinkNoteData data = dataManager.findById(note.dataId);

        // Update content
        data.url = webLinkElement.getText().toString().trim();

        String enteredTitle = titleElement.getText().toString().trim();
        note.title = enteredTitle.isEmpty() ? note.type.getInitialTitle() : enteredTitle;
        noteManager.update(note);

        noteManager.update(note);
        dataManager.update(data);

        // Set these listener to null, avoid mem leak
        titleElement.setOnFocusChangeListener(null);
        titleElement.setOnEditorActionListener(null);
        webLinkElement.setOnFocusChangeListener(null);
        webLinkElement.setOnEditorActionListener(null);
        visitWebBtn.setOnClickListener(null);
        iconElement.setOnClickListener(null);
        iconElement.setOnLongClickListener(null);

        super.onDestroy();
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

    private void setQrCode() {
        QRCodeWriter writer = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = writer.encode(
                    webLinkElement.getText().toString().trim(), BarcodeFormat.QR_CODE, 680, 680);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ?
                            getContext().getColor(R.color.board_black) :
                            Color.TRANSPARENT);
                }
            }
            qrContainer.setImageBitmap(bmp);

        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
