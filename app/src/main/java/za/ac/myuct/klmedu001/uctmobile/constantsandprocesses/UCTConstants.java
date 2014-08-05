package za.ac.myuct.klmedu001.uctmobile.constantsandprocesses;

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

    public static final String html_header_body_open = "<html><head><style>html,body{margin: 0 auto;padding: 0px;}.rightmargin{width:100%;height:auto;margin:0px;padding:0px;}p{padding:5%;}</style></head><body>";
    public static final String html_body_close = "</body></html>";
}
