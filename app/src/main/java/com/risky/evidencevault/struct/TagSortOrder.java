package com.risky.evidencevault.struct;

/**
 * Represent sort order for {@link com.risky.evidencevault.dao.Tag}
 *
 * @author Khoa Nguyen
 */
public enum TagSortOrder {
    CHRONOLOGICAL, BY_COLOR;

    public static TagSortOrder getTypeFromString(String sortOrder) {
        for (TagSortOrder order : values()) {
            if (sortOrder.equals(order.name())) {
                return order;
            }
        }

        return CHRONOLOGICAL; // gotta have a default value
    }

    public TagSortOrder next() {
        return values()[(this.ordinal() + 1) % values().length];
    }
}
