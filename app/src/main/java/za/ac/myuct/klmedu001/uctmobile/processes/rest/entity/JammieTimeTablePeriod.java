package za.ac.myuct.klmedu001.uctmobile.processes.rest.entity;

import java.util.Calendar;

/**
 * Created by eduardokolomajr on 2014/08/08.
 */
public class JammieTimeTablePeriod {
    String type;
    Calendar start;
    Calendar end;

    public JammieTimeTablePeriod() {}

    public JammieTimeTablePeriod(String type, Calendar start, Calendar end) {
        this.type = type;
        this.start = start;
        this.end = end;
    }

    public String getType() {
        return type;
    }

    public Calendar getStart() {
        return start;
    }

    public Calendar getEnd() {
        return end;
    }

    @Override
    public String toString() {
        return "JammieTimeTablePeriod{" +
                "type='" + type + '\'' +
                ", start=" + start.getTime() +
                ", end=" + end.getTime() +
                '}';
    }
}
