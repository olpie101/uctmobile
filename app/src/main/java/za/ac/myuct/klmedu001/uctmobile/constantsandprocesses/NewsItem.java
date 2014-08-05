package za.ac.myuct.klmedu001.uctmobile.constantsandprocesses;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * Created by eduardokolomajr on 2014/07/27.
 */
public class NewsItem {
    private final String title;
    private final String link;
    private final String photoLink;

    public NewsItem(String title, String link, String photoLink) {
        this.title = title;
        this.link = link;
        this.photoLink = photoLink;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getPhotoLink() {
        return photoLink;
    }
}
