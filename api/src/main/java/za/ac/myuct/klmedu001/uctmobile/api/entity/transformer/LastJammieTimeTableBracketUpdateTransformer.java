package za.ac.myuct.klmedu001.uctmobile.api.entity.transformer;

import com.google.api.server.spi.config.Transformer;

import java.util.Calendar;
import java.util.GregorianCalendar;

import za.ac.myuct.klmedu001.uctmobile.api.entity.LastJammieTimeTableBracketUpdate;
import za.ac.myuct.klmedu001.uctmobile.api.entity.transformed.LastJammieTimeTableBracketUpdateTransformed;

/**
 * Created by eduardokolomajr on 2014/09/19.
 */
public class LastJammieTimeTableBracketUpdateTransformer implements
        Transformer<LastJammieTimeTableBracketUpdate, LastJammieTimeTableBracketUpdateTransformed> {
    @Override
    public LastJammieTimeTableBracketUpdateTransformed transformTo(LastJammieTimeTableBracketUpdate in) {
        return new LastJammieTimeTableBracketUpdateTransformed(in.getDate().getTimeInMillis());
    }

    @Override
    public LastJammieTimeTableBracketUpdate transformFrom(LastJammieTimeTableBracketUpdateTransformed in) {
        Calendar c = new GregorianCalendar();
        c.setTimeInMillis(in.date);
        return new LastJammieTimeTableBracketUpdate(c);
    }
}
