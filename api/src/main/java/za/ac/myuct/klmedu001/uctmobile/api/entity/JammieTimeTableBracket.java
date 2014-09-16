package za.ac.myuct.klmedu001.uctmobile.api.entity;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Serialize;

import java.util.Calendar;

/**
 * Created by eduardokolomajr on 2014/08/07.
 * simple object to manage the start and end dates of a jammie time table bracket
 * (ie. term time table: 21 July 2014 - 29 August 2014)
 */
@Entity
public class JammieTimeTableBracket {
    @Id
    @Index
    String type;
    @Serialize Calendar start;
    @Serialize Calendar end;

    public JammieTimeTableBracket() {}

    public JammieTimeTableBracket(String type, Calendar start, Calendar end) {
        this.type = type;
        this.start = start;
        this.end = end;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Calendar getStart() {
        return start;
    }

    public void setStart(Calendar start) {
        this.start = start;
    }

    public Calendar getEnd() {
        return end;
    }

    public void setEnd(Calendar end) {
        this.end = end;
    }

    @Override
    public String toString() {
        return "JammieTimeTableBracket{" +
                "type='" + type + '\'' +
                ", start=" + start.getTime() +
                ", end=" + end.getTime() +
                '}';
    }
}
