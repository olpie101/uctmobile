package za.ac.myuct.klmedu001.uctmobile.processes.rest.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import za.ac.myuct.klmedu001.uctmobile.constants.AppDatabase;

/**
 * Created by eduardokolomajr on 2014/07/29.
 */
@Table(databaseName = AppDatabase.NAME, value = AppDatabase.TABLE_RSS_FEED)
public class RSSItem extends BaseModel implements Comparable, Parcelable {
    @Column(columnType = Column.PRIMARY_KEY_AUTO_INCREMENT) long _id;
    @Column public String title;
    @Column public String link;
    @Column public String description;
    @Column public Date pubDate;

    @SuppressWarnings("unused") //Used by active android ORM library
    public RSSItem(){

    }

    public RSSItem(String title, String link, String description, String pubDate) {
        this.title = title;
        this.link = link;
        this.description = description;
        Date temp = new Date();
        try {
             temp = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z", Locale.ENGLISH).parse(pubDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.pubDate = temp;
    }

    @Override
    public String toString() {
        return "Title: "+title+"; Link: "+link+"; Description: "+description+"; pubDate: "+pubDate;
    }

    @Override
    public int compareTo(Object o) {
        if(o != null){
            if(o  instanceof RSSItem){
                return pubDate.compareTo(((RSSItem) o).pubDate);
            }
            throw new UnsupportedClassVersionError();
        }
        throw new NullPointerException();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.link);
        dest.writeString(this.description);
        dest.writeLong(pubDate != null ? pubDate.getTime() : -1);
    }

    private RSSItem(Parcel in) {
        this.title = in.readString();
        this.link = in.readString();
        this.description = in.readString();
        long tmpPubDate = in.readLong();
        this.pubDate = tmpPubDate == -1 ? null : new Date(tmpPubDate);
    }

    public static final Parcelable.Creator<RSSItem> CREATOR = new Parcelable.Creator<RSSItem>() {
        public RSSItem createFromParcel(Parcel source) {
            return new RSSItem(source);
        }

        public RSSItem[] newArray(int size) {
            return new RSSItem[size];
        }
    };
}
