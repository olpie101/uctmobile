package za.ac.myuct.klmedu001.uctmobile.processes.rest.entity;


import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import za.ac.myuct.klmedu001.uctmobile.constants.AppDatabase;

/**
 * Created by eduardokolomajr on 2014/07/27.
 */
@Table(databaseName = AppDatabase.NAME, value = AppDatabase.TABLE_NEWS_FEED)
public class NewsItem extends BaseModel {
    @Column (columnType = Column.PRIMARY_KEY_AUTO_INCREMENT) long _id;
    @Column String title;
    @Column String link;
    @Column String photoLink;

    @SuppressWarnings("unused") //Used by active android ORM library
    public NewsItem() {
    }

    public NewsItem(String title, String link, String photoLink) {
        this.title = title;
        this.link = link;
        this.photoLink = photoLink;
    }

    public String getTitle() {
        return title;
    }

    @SuppressWarnings("unused")
    public String getLink() {
        return link;
    }

    public String getPhotoLink() {
        return photoLink;
    }
}
