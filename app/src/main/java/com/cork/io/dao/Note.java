package com.cork.io.dao;

import android.util.Log;

import com.cork.io.dao.converter.IdArrayConverter;
import com.cork.io.dao.converter.NoteTypeConverter;
import com.cork.io.data.ObjectBoxConnectionManager;
import com.cork.io.data.ObjectBoxNoteChecklistDataManager;
import com.cork.io.data.ObjectBoxNoteContactDataManager;
import com.cork.io.data.ObjectBoxNoteEventDataManager;
import com.cork.io.data.ObjectBoxNoteGenericDataManager;
import com.cork.io.data.ObjectBoxNoteLocationDataManager;
import com.cork.io.struct.NoteType;
import com.cork.io.utils.NumberUtil;

import java.util.LinkedHashSet;
import java.util.Set;

import io.objectbox.annotation.Convert;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * Note DAO. This is the smallest database object.
 */
@Entity
public class Note {
    @Id
    public long id;

    public long boardId;
    public float positionX; // absolute X from 0
    public float positionY; // absolute Y from 0

    public String customIconPath;
    public long dataId;
    public String title;

    @Convert(converter = NoteTypeConverter.class, dbType = String.class)
    public NoteType type;

    @Convert(converter = IdArrayConverter.class, dbType = String.class)
    public Set<Long> connection;

    public Note(){}

    public Note(long boardId, NoteType type, float positionX, float positionY) {
        this.boardId = boardId;
        this.type = type;
        this.customIconPath = "";
        this.title = type.getInitialTitle();

        this.positionX = positionX;
        this.positionY = positionY;
        this.connection = new LinkedHashSet<>();

        // Create new data record
        switch (type) {
            case GENERIC:
                BaseNoteData baseNoteData = ObjectBoxNoteGenericDataManager.get().add(new BaseNoteData());
                this.dataId = baseNoteData.id;
                break;
            case CONTACT:
                ContactNoteData contactNoteData = ObjectBoxNoteContactDataManager.get().add(new ContactNoteData());
                this.dataId = contactNoteData.id;
                break;
            case LOCATION:
                LocationNoteData locationNoteData = ObjectBoxNoteLocationDataManager.get().add(new LocationNoteData());
                this.dataId = locationNoteData.id;
                break;
            case EVENT:
                EventNoteData eventNoteData = ObjectBoxNoteEventDataManager.get().add(new EventNoteData());
                this.dataId = eventNoteData.id;
                break;
            case CHECKLIST:
                ChecklistNoteData checklistNoteData = ObjectBoxNoteChecklistDataManager.get().add(new ChecklistNoteData());
                this.dataId = checklistNoteData.id;
                break;
        }
    }

    public Set<Long> getLinkedNotes() {
        Set<Long> result = new LinkedHashSet<>();
        for (Long connId : connection) {
            Connection conn = ObjectBoxConnectionManager.get().findConnectionById(connId);

            result.add(conn.getLinkedNoteId(this.id));
        }

        return result;
    }

    public long getConnectionIdFromLinkedNote(long linkedNoteId) {
        for (Long connId : connection) {
            if (ObjectBoxConnectionManager.get().findConnectionById(connId).getLinkedNoteId(id) == linkedNoteId) {
                return connId;
            }
        }

        return -1;
    }

    public String getDisplayId() {
        return NumberUtil.convertToDisplayId(id);
    }
}
