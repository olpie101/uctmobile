package za.ac.myuct.klmedu001.uctmobile.constants;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by eduardokolomajr on 2014/07/27.
 */
@Table(name="newsFeed")
public class NewsItem extends Model {
    @Column private String title;
    @Column private String link;
    @Column private String photoLink;

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
