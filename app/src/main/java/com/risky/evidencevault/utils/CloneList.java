package com.risky.evidencevault.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * A copy of ArrayList, to pass into functions as a copy instead of reference
 *
 * @author knguyen
 */
public class CloneList<T> extends ArrayList<T> {
    public CloneList(List<T> listToBeCloned) {
        addAll(listToBeCloned);
    }
}
