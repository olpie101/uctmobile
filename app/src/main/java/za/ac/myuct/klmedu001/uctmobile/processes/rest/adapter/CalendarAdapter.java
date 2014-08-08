package za.ac.myuct.klmedu001.uctmobile.processes.rest.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import za.ac.myuct.klmedu001.uctmobile.constants.UCTConstants;
import za.ac.myuct.klmedu001.uctmobile.processes.rest.entity.LastJammieTimeTableUpdate;

/**
 * Created by eduardokolomajr on 2014/08/08.
 * Gson TypeAdapter to convert the last Jammie timetable update from json format received from
 * server an instance of LastTimeTableUpdate
 */
public class CalendarAdapter extends TypeAdapter<Calendar> {
    @Override
    public void write(JsonWriter out, Calendar lastUpdate) throws IOException {
        out.name("date").value(lastUpdate.getTimeInMillis());
    }

    @Override
    public Calendar read(JsonReader in) throws IOException {
        Calendar temp = new GregorianCalendar(UCTConstants.TIME_ZONE);
        temp.setTimeInMillis(in.nextLong());
        return temp;
    }
}
