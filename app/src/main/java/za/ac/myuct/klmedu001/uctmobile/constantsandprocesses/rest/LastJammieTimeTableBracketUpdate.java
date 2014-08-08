package za.ac.myuct.klmedu001.uctmobile.constantsandprocesses.rest;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.GregorianCalendar;

import retrofit.converter.ConversionException;
import retrofit.converter.Converter;
import retrofit.mime.TypedInput;
import retrofit.mime.TypedOutput;

/**
 * Created by eduardokolomajr on 2014/08/08.
 */
public class LastJammieTimeTableBracketUpdate implements Serializable{
    Calendar date = new GregorianCalendar();

    public LastJammieTimeTableBracketUpdate() {
    }

    public LastJammieTimeTableBracketUpdate(Calendar date) {
        this.date = date;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }
}
