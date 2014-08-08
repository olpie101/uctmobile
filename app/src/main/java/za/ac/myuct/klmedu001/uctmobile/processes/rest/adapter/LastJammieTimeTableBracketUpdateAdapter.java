package za.ac.myuct.klmedu001.uctmobile.processes.rest.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import za.ac.myuct.klmedu001.uctmobile.constants.UCTConstants;
import za.ac.myuct.klmedu001.uctmobile.processes.rest.entity.LastJammieTimeTableBracketUpdate;

/**
 * Created by eduardokolomajr on 2014/08/08.
 */
public class LastJammieTimeTableBracketUpdateAdapter extends TypeAdapter<LastJammieTimeTableBracketUpdate> {
    @Override
    public void write(JsonWriter out, LastJammieTimeTableBracketUpdate lastUpdate) throws IOException {
        out.beginObject();
        out.name("date").value(lastUpdate.getDate().getTimeInMillis());
        out.endObject();
    }

    @Override
    public LastJammieTimeTableBracketUpdate read(JsonReader in) throws IOException {
        Calendar temp = new GregorianCalendar(UCTConstants.TIME_ZONE);
        in.beginObject();
        in.nextName();
        temp.setTimeInMillis(in.nextLong());
        in.endObject();
        return new LastJammieTimeTableBracketUpdate(temp);
    }
}
