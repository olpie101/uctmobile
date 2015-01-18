package za.ac.myuct.klmedu001.uctmobile.processes.rest.container;

import android.os.Parcel;
import android.os.Parcelable;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import za.ac.myuct.klmedu001.uctmobile.api.endpoints.jammieEndpoint.model.JammieTimeTableBracketTransformed;
import za.ac.myuct.klmedu001.uctmobile.constants.AppDatabase;

/**
 * Created by eduardokolomajr on 2014/09/19.
 */
@Table(databaseName = AppDatabase.NAME, value = AppDatabase.TABLE_JAMMIE_TIMETABLE_BRACKET)
public class JammieTimeTableBracketContainer extends BaseModel implements Parcelable {
    @Column (columnType = Column.PRIMARY_KEY_AUTO_INCREMENT) long _id;
    @Column String type;
    @Column long start;
    @Column long end;

    @SuppressWarnings("unused")
    public JammieTimeTableBracketContainer(){}

    public JammieTimeTableBracketContainer(JammieTimeTableBracketTransformed in){
        super();
        type = in.getType();
        start = in.getStart();
        end = in.getEnd();
    }

    public long get_id() {
        return _id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.type);
        dest.writeLong(this.start);
        dest.writeLong(this.end);
    }

    private JammieTimeTableBracketContainer(Parcel in) {
        this.type = in.readString();
        this.start = in.readLong();
        this.end = in.readLong();
    }

    public static final Parcelable.Creator<JammieTimeTableBracketContainer> CREATOR = new Parcelable.Creator<JammieTimeTableBracketContainer>() {
        public JammieTimeTableBracketContainer createFromParcel(Parcel source) {
            return new JammieTimeTableBracketContainer(source);
        }

        public JammieTimeTableBracketContainer[] newArray(int size) {
            return new JammieTimeTableBracketContainer[size];
        }
    };
}
