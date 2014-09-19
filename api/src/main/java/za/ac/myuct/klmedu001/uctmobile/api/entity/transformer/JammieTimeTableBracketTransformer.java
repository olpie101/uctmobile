package za.ac.myuct.klmedu001.uctmobile.api.entity.transformer;

import com.google.api.server.spi.config.Transformer;

import java.util.GregorianCalendar;

import za.ac.myuct.klmedu001.uctmobile.api.entity.JammieTimeTableBracket;
import za.ac.myuct.klmedu001.uctmobile.api.entity.transformed.JammieTimeTableBracketTransformed;

/**
 * Created by eduardokolomajr on 2014/09/18.
 */
public class JammieTimeTableBracketTransformer implements Transformer<JammieTimeTableBracket, JammieTimeTableBracketTransformed> {
    @Override
    public JammieTimeTableBracketTransformed transformTo(JammieTimeTableBracket in) {
        return new JammieTimeTableBracketTransformed(in.getType(),
                in.getStart(), in.getEnd());
    }

    @Override
    public JammieTimeTableBracket transformFrom(JammieTimeTableBracketTransformed in) {
        GregorianCalendar start = new GregorianCalendar();
        GregorianCalendar end = new GregorianCalendar();
        start.setTimeInMillis(in.start);
        end.setTimeInMillis(in.end);
        return new JammieTimeTableBracket(in.type, start, end);
    }
}
