package com.risky.evidencevault.struct;

import androidx.annotation.NonNull;

import java.util.Comparator;

/**
 * Represent a single item from Checklist note
 *
 * @author Khoa Nguyen
 */
public class ChecklistItem {
    private long addedOrder;
    private String label;
    private ElementColor color;
    private boolean status;

    public ChecklistItem(Long addedOrder, String label, ElementColor color, boolean status) {
        this.addedOrder = addedOrder;
        this.label = label;
        this.color = color;
        this.status = status;
    }

    public long getAddedOrder() {
        return addedOrder;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public ElementColor getColor() {
        return color;
    }

    public void setColor(ElementColor color) {
        this.color = color;
    }

    public boolean getStatus() {
        return status;
    }

    public void toggleStatus() {
        this.status = !this.status;
    }

    @NonNull
    @Override
    public String toString() {
        return "[" + addedOrder + "," + label + "," + color.name() + "," + status + "]";
    }

    public static ChecklistItem fromString(String item) {
        String bracketRemove = item.replace("[","").replace("]", "");
        String[] itemSplit = bracketRemove.split(",");
        return new ChecklistItem(Long.parseLong(itemSplit[0]), itemSplit[1], ElementColor.getTypeFromString(itemSplit[2]), Boolean.parseBoolean(itemSplit[3]));
    }

    // Sorter
    public static class ChronologicalComparator implements Comparator<ChecklistItem> {
        @Override
        public int compare(ChecklistItem o1, ChecklistItem o2) {
            if (o1.getAddedOrder() > o2.getAddedOrder()) {
                return 1;
            } else if (o1.getAddedOrder() == o2.getAddedOrder()) {
                return 0;
            } else {
                return -1;
            }
        }
    }

    public static class ColorComparator implements Comparator<ChecklistItem> {
        @Override
        public int compare(ChecklistItem o1, ChecklistItem o2) {
            if (o1.getColor().ordinal() > o2.getColor().ordinal()) {
                return 1;
            } else if (o1.getColor().ordinal() == o2.getColor().ordinal()) {
                return 0;
            } else {
                return -1;
            }
        }
    }

    public static class StatusComparator implements Comparator<ChecklistItem> {
        @Override
        public int compare(ChecklistItem o1, ChecklistItem o2) {
            return Boolean.compare(o1.getStatus(), o2.getStatus());
        }
    }
}
