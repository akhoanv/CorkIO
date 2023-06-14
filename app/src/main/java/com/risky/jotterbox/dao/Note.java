package com.risky.jotterbox.dao;

import com.risky.jotterbox.dao.converter.IdArrayConverter;
import com.risky.jotterbox.dao.converter.NoteTypeConverter;
import com.risky.jotterbox.dao.converter.Position2DConverter;
import com.risky.jotterbox.data.ObjectBoxConnectionManager;
import com.risky.jotterbox.data.ObjectBoxNoteChecklistDataManager;
import com.risky.jotterbox.data.ObjectBoxNoteContactDataManager;
import com.risky.jotterbox.data.ObjectBoxNoteEventDataManager;
import com.risky.jotterbox.data.ObjectBoxNoteGenericDataManager;
import com.risky.jotterbox.data.ObjectBoxNoteImageDataManager;
import com.risky.jotterbox.data.ObjectBoxNoteWebLinkDataManager;
import com.risky.jotterbox.data.ObjectBoxNoteLocationDataManager;
import com.risky.jotterbox.struct.NoteType;
import com.risky.jotterbox.struct.Point2D;
import com.risky.jotterbox.utils.NumberUtil;

import java.util.LinkedHashSet;
import java.util.Set;

import io.objectbox.annotation.Convert;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Uid;

/**
 * Note DAO. This is the smallest database object.
 */
@Entity
@Uid(6708627763836334107L)
public class Note {
    @Id
    public long id;

    @Uid(2329310424445038161L)
    public long boardId;

    @Convert(converter = Position2DConverter.class, dbType = String.class)
    @Uid(2305233739250448219L)
    public Point2D position; // absolute from 0

    @Uid(5961820350283559114L)
    public String customIconPath;

    @Uid(7792151678298590196L)
    public long dataId;

    @Uid(1072002065242505641L)
    public String title;

    @Convert(converter = NoteTypeConverter.class, dbType = String.class)
    @Uid(1242922150514037626L)
    public NoteType type;

    @Convert(converter = IdArrayConverter.class, dbType = String.class)
    @Uid(2718532524019566224L)
    public Set<Long> connection;

    @Convert(converter = IdArrayConverter.class, dbType = String.class)
    @Uid(913654504793566238L)
    public Set<Long> tag;

    public Note(){}

    public Note(long boardId, NoteType type, float positionX, float positionY) {
        this.boardId = boardId;
        this.type = type;
        this.customIconPath = "";
        this.title = type.getInitialTitle();

        this.position = new Point2D(positionX, positionY);
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
            case IMAGE:
                ImageNoteData imageNoteData = ObjectBoxNoteImageDataManager.get().add(new ImageNoteData());
                this.dataId = imageNoteData.id;
                break;
            case WEBLINK:
                WebLinkNoteData webLinkNoteData = ObjectBoxNoteWebLinkDataManager.get().add(new WebLinkNoteData());
                this.dataId = webLinkNoteData.id;
                break;
        }
    }

    public Set<Long> getLinkedNotes() {
        Set<Long> result = new LinkedHashSet<>();
        for (Long connId : connection) {
            Connection conn = ObjectBoxConnectionManager.get().findById(connId);

            result.add(conn.getLinkedNoteId(this.id));
        }

        return result;
    }

    public long getConnectionIdFromLinkedNote(long linkedNoteId) {
        for (Long connId : connection) {
            if (ObjectBoxConnectionManager.get().findById(connId).getLinkedNoteId(id) == linkedNoteId) {
                return connId;
            }
        }

        return -1;
    }

    public String getDisplayId() {
        return NumberUtil.convertToDisplayId(id);
    }
}
