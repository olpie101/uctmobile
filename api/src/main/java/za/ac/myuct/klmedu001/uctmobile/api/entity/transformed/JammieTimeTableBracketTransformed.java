package za.ac.myuct.klmedu001.uctmobile.api.entity.transformed;

import java.util.Calendar;

/**
 * Created by eduardokolomajr on 2014/09/18.
 * Used to transform <code>JammieTimeTableBracket</code> into a class
 * that can be transferred via JSON
 */
public class JammieTimeTableBracketTransformed {
    public String type;
    public long start;
    public long end;

    public JammieTimeTableBracketTransformed(String type, Calendar start, Calendar end) {
        this.type = type;
        this.start = start.getTimeInMillis();
        this.end = end.getTimeInMillis();
    }
}
