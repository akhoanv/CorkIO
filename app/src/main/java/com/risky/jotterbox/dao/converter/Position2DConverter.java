package com.risky.jotterbox.dao.converter;

import com.risky.jotterbox.struct.Point2D;

import io.objectbox.converter.PropertyConverter;

/**
 * Convert ObjectBox database to {@link Point2D} object and vice versa
 *
 * @author Khoa Nguyen
 */
public class Position2DConverter implements PropertyConverter<Point2D, String> {
    @Override
    public Point2D convertToEntityProperty(String databaseValue) {
        String[] split = databaseValue.split(",");
        return new Point2D(Float.valueOf(split[0]), Float.valueOf(split[1]));
    }

    @Override
    public String convertToDatabaseValue(Point2D entityProperty) {
        return entityProperty.toString();
    }
}
