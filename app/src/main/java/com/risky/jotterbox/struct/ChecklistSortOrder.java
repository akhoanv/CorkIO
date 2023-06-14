package com.risky.jotterbox.struct;

/**
 * Represent sort order for {@link ChecklistItem}
 *
 * @author Khoa Nguyen
 */
public enum ChecklistSortOrder {
    CHRONOLOGICAL, BY_COLOR, BY_STATUS;

    public static ChecklistSortOrder getTypeFromString(String sortOrder) {
        for (ChecklistSortOrder order : values()) {
            if (sortOrder.equals(order.name())) {
                return order;
            }
        }

        return CHRONOLOGICAL; // gotta have a default value
    }

    public ChecklistSortOrder next() {
        return values()[(this.ordinal() + 1) % values().length];
    }
}
