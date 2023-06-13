package com.risky.evidencevault.dao;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Uid;

/**
 * Data for WebLink note type
 *
 * @author Khoa Nguyen
 */
@Entity
@Uid(4943673408453480600L)
public class WebLinkNoteData extends BaseNoteData {
    @Uid(7873361877488021416L)
    public String url;

    public WebLinkNoteData() {}
}
