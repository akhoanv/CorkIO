package com.risky.jotterbox.data;

import com.risky.jotterbox.dao.ImageNoteData;
import com.risky.jotterbox.objectbox.ObjectBox;

import java.util.List;

public class ObjectBoxNoteImageDataManager implements NoteDataManager<ImageNoteData> {
    private static ObjectBoxNoteImageDataManager manager;

    public static ObjectBoxNoteImageDataManager get() {
        if (manager == null) {
            manager = new ObjectBoxNoteImageDataManager();
        }

        return manager;
    }

    @Override
    public ImageNoteData add(ImageNoteData data) {
        long id = ObjectBox.get().boxFor(ImageNoteData.class).put(data);
        return findById(id);
    }

    @Override
    public ImageNoteData findById(long id) {
        return ObjectBox.get().boxFor(ImageNoteData.class).get(id);
    }

    @Override
    public boolean remove(long id) {
        return ObjectBox.get().boxFor(ImageNoteData.class).remove(id);
    }

    @Override
    public boolean update(ImageNoteData data) {
        ObjectBox.get().boxFor(ImageNoteData.class).put(data);
        return true;
    }

    @Override
    public List<ImageNoteData> getAll() {
        return ObjectBox.get().boxFor(ImageNoteData.class).getAll();
    }

    @Override
    public boolean removeAll() {
        ObjectBox.get().boxFor(ImageNoteData.class).removeAll();
        return true;
    }
}