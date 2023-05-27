package com.risky.evidencevault.data;

import com.risky.evidencevault.dao.BaseNoteData;

import java.util.List;

public interface NoteDataManager<T extends BaseNoteData> {
    T add(T data);
    T findById(long id);
    boolean remove(long id);
    boolean update(T data);
    List<T> getAll();
    boolean removeAll();
}
