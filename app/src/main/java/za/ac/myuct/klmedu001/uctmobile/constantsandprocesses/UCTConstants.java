package za.ac.myuct.klmedu001.uctmobile.constantsandprocesses;

import android.content.res.Resources;
import android.graphics.Color;

import java.util.TimeZone;

import za.ac.myuct.klmedu001.uctmobile.R;

/**
 * Created by eduardokolomajr on 2014/07/26.
 */
public interface UCTConstants {
    public static final String UCT_URL = "http://www.uct.ac.za";
    public static final String UCT_DAILY_NEWS_URL = UCT_URL+"/dailynews/rss/";
    public static final String UCT_MONDAY_PAPER_URL = UCT_URL+"/mondaypaper/rss/";
//    public static final String UCT_URL = "http://localhost/~eduardokolomajr/www.uct.ac.za";
//    public static final String UCT_URL = "http://192.168.56.1/~eduardokolomajr/www.uct.ac.za/";
    public static final String HOMEPAGE_FEATURED_STORIES_CONTAINER = "#slider";
    public static final String HOMEPAGE_FEATURED_STORIES_ITEM = "li";
    public static final String HOMEPAGE_STORIES_CONTAINER = "#hp_main_holder";
    public static final String HOMEPAGE_STORIES_ITEM = "td";
    public static final String HOMEPAGE_TITLE = "b";
    public static final String HOMEPAGE_LINK = "a";
    public static final String HOMEPAGE_LINK_HREF = "href";
    public static final String HOMEPAGE_IMAGE = "img";
    public static final String HOMEPAGE_IMAGE_SRC = "src";


    public static final String RSS_FEED_FEED_TAG = "channel";
    public static final String RSS_FEED_ENTRY_TAG = "item";
    public static final String RSS_FEED_TITLE_TAG = "title";
    public static final String RSS_FEED_LINK_TAG = "link";
    public static final String RSS_FEED_DESCRIPTION_TAG = "description";
    public static final String RSS_FEED_PUB_DATE_TAG = "pubDate";

    public static final String BUNDLE_EXTRA_RSS_ITEM = "rss-item";

    public static final String SHARED_PREFS = "za.ac.myuct.klmedu001.uctmobile.MAIN_PREFS";
    public static final String PREFS_LAST_JAMMIE_UPDATE = "LJA";

    public static final TimeZone TIME_ZONE = TimeZone.getTimeZone("Africa/Johannesburg");
    public static final String AE_URL = "http://moonlit-bliss-663.appspot.com";

    public static final String html_header_body_open = "<html><head><style type='text/css'>html,body{" +
            "margin: 0 auto;padding: 0px;font-family: Sans-Serif;}" +
            "h1:first-of-type{padding:5%; color:white}" +
            "p{padding:0% 5%;}" +
            "a:hover, a:visited:hover {font-weight: bold;color: #006699;text-decoration: underline;}" +
            "a:visited {font-weight: bold;color: #1284C6;text-decoration: none;}" +
            ".top-section{background-color:#0099FF; min-height:100px;}" +
            ".date {font-weight: bold; font-size:10pt; color:#666666; text-align:justify;}"+
            ".rightmargin{width:100%;height:auto;margin:0px;padding:0px;clear:both;}" +
            ".small{font-weight:normal;color:#666666;font-size:11pt;}" +
            "</style></head><body>";
    public static final String html_body_close = "</body></html>";
}
