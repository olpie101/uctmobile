package za.ac.myuct.klmedu001.uctmobile.processes.rest.container;

import android.os.Parcel;
import android.os.Parcelable;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import za.ac.myuct.klmedu001.uctmobile.api.endpoints.jammieEndpoint.model.JammieTimeTableBracketTransformed;

/**
 * Created by eduardokolomajr on 2014/09/19.
 */
@Table(name = "jammieTimeTableBrackets")
public class JammieTimeTableBracketContainer extends Model implements Parcelable {
    @Column public String type;
    @Column public long start;
    @Column public long end;

    @SuppressWarnings("unused")
    public JammieTimeTableBracketContainer(){}

    public JammieTimeTableBracketContainer(JammieTimeTableBracketTransformed in){
        type = in.getType();
        start = in.getStart();
        end = in.getEnd();
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
