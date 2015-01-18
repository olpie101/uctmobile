package za.ac.myuct.klmedu001.uctmobile.constants;

import com.grosner.dbflow.annotation.Database;

/**
 * Created by eduardokolomajr on 2014/12/02.
 * Database class used by DBFlow
 */
@Database(name = AppDatabase.NAME, version = AppDatabase.VERSION, foreignKeysSupported = true)
public class AppDatabase {
    public static final String NAME = "uctmobile";
    public static final int  VERSION = 1;

    public static final String TABLE_ALL_ROUTES = "routes";
    public static final String TABLE_JAMMIE_TIMETABLE_BRACKET = "brackets";
    public static final String TABLE_ROUTE = "route";
    public static final String TABLE_ROUTE_TIME = "routeTime";
    public static final String TABLE_NEWS_FEED = "newsFeed";
    public static final String TABLE_RSS_FEED = "rssFeed";
}
