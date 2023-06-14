package com.risky.jotterbox.dao;

import com.risky.jotterbox.dao.converter.ColorConverter;
import com.risky.jotterbox.dao.converter.IdArrayConverter;
import com.risky.jotterbox.dao.converter.Position2DConverter;
import com.risky.jotterbox.struct.ElementColor;
import com.risky.jotterbox.struct.Point2D;

import java.util.Calendar;
import java.util.Set;

import io.objectbox.annotation.Convert;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Uid;

/**
 * Contains data for a {@link com.risky.jotterbox.worldobject.BoardFragment} within the database
 *
 * @author Khoa Nguyen
 */
@Entity
@Uid(3284756564701398499L)
public class Board {
    @Id
    public long id;

    @Convert(converter = Position2DConverter.class, dbType = String.class)
    @Uid(7824221245197078356L)
    public Point2D panPosition;

    @Uid(1981065436060520912L)
    public float scaleFactor;

    @Uid(6119998153631567120L)
    public String name;

    @Uid(1654151340542458684L)
    public long createdDate;

    @Convert(converter = ColorConverter.class, dbType = String.class)
    @Uid(8191186176177623737L)
    public ElementColor color;

    @Convert(converter = IdArrayConverter.class, dbType = String.class)
    @Uid(1538570977416494015L)
    public Set<Long> notes;

    @Convert(converter = IdArrayConverter.class, dbType = String.class)
    @Uid(1385828363769857664L)
    public Set<Long> tags;

    public Board() {
        this.panPosition = new Point2D(0f, 0f);
        this.scaleFactor = 1f;
        this.name = "Untitled board";
        this.color = ElementColor.BLUE;
        this.createdDate = Calendar.getInstance().getTimeInMillis();
    }
}
