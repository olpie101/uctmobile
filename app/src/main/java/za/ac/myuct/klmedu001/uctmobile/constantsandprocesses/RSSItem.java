package za.ac.myuct.klmedu001.uctmobile.constantsandprocesses;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by eduardokolomajr on 2014/07/29.
 */
public class RSSItem implements Comparable{
    public final String title;
    public final String link;
    public final String description;
    public final Date pubDate;

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
}
