package za.ac.myuct.klmedu001.uctmobile.api.entity;

import com.google.api.server.spi.config.ApiTransformer;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Serialize;

import java.util.Calendar;
import java.util.GregorianCalendar;

import za.ac.myuct.klmedu001.uctmobile.api.entity.transformer.LastJammieTimeTableBracketUpdateTransformer;

/**
 * Created by eduardokolomajr on 2014/08/07.
 */
@Entity
@ApiTransformer(LastJammieTimeTableBracketUpdateTransformer.class)
public class LastJammieTimeTableBracketUpdate {
    @Id
    Long id;
    @Serialize
    @Index
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
