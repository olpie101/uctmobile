package za.ac.myuct.klmedu001.uctmobile.processes.rest.entity;

import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by eduardokolomajr on 2014/08/08.
 * Entity used to track when last the Jammie Timetable was updated
 */
public class LastJammieTimeTableUpdate implements Serializable{
    Calendar date = new GregorianCalendar();

    @SuppressWarnings("unused")
    public LastJammieTimeTableUpdate() {
    }

    public LastJammieTimeTableUpdate(Calendar date) {
        this.date = date;
    }

    public Calendar getDate() {
        return date;
    }
}
