package com.risky.jotterbox.fragment.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.risky.jotterbox.R;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

/**
 * Adapter containing carrousel image, provided with {@link List} of URI string
 *
 * @author Khoa Nguyen
 */
public class NoteImagePagerAdapter extends PagerAdapter {
    private Context context;
    private List<String> imageList;

    // Layout Inflater
    LayoutInflater mLayoutInflater;

    // Viewpager Constructor
    public NoteImagePagerAdapter(Context context, List<String> images) {
        this.context = context;
        this.imageList= images;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // return the number of images
        return imageList.size();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        // Hack to get notifyDatasetChange to listen when dynamically change list
        return POSITION_NONE;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((LinearLayout) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        View itemView = mLayoutInflater.inflate(R.layout.fragment_note_image_item, container, false);
        ImageView imageView = itemView.findViewById(R.id.note_edit_image);

        if (!imageList.get(position).isEmpty()) {
            try {
                InputStream inputStream = context.getContentResolver().openInputStream(Uri.parse(imageList.get(position)));
                Bitmap importedImg = BitmapFactory.decodeStream(new BufferedInputStream(inputStream));
                imageView.setImageBitmap(importedImg);
            } catch (Exception e) {
                e.getStackTrace();

                imageView.setImageResource(R.drawable.image_broken_white);
            }
        }

        // Adding the View
        Objects.requireNonNull(container).addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }

    public void add(String uri) {
        imageList.add(uri);

        notifyDataSetChanged();
    }

    public void remove(int position) {
        imageList.remove(position);

        notifyDataSetChanged();
    }
}
