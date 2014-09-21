package za.ac.myuct.klmedu001.uctmobile.processes.rest.container;

import android.os.Parcel;
import android.os.Parcelable;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import za.ac.myuct.klmedu001.uctmobile.api.endpoints.jammieEndpoint.model.Route;

/**
 * Created by eduardokolomajr on 2014/09/19.
 */
@Table(name = "route")
public class RouteContainer extends Model implements Parcelable {
    @Column private String name;            //Route name
    @Column private String displayCode;     //actual code to display (code seen on jammie timetables publically)
    @Column private String code;            //internal code (code used to differentiate between the different types and day times)
    @Column private String operatingDays;   //days set for operation

    @SuppressWarnings("unused")
    public RouteContainer(){super();}

    public RouteContainer(Route in){
        super();
        this.name = in.getName();
        this.displayCode = in.getDisplayCode();
        this.code = in.getCode();
        this.operatingDays = in.getOperatingDays();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayCode() {
        return displayCode;
    }

    public void setDisplayCode(String displayCode) {
        this.displayCode = displayCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getOperatingDays() {
        return operatingDays;
    }

    public void setOperatingDays(String operatingDays) {
        this.operatingDays = operatingDays;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.displayCode);
        dest.writeString(this.code);
        dest.writeString(this.operatingDays);
    }

    private RouteContainer(Parcel in) {
        this.name = in.readString();
        this.displayCode = in.readString();
        this.code = in.readString();
        this.operatingDays = in.readString();
    }

    public static final Parcelable.Creator<RouteContainer> CREATOR = new Parcelable.Creator<RouteContainer>() {
        public RouteContainer createFromParcel(Parcel source) {
            return new RouteContainer(source);
        }

        public RouteContainer[] newArray(int size) {
            return new RouteContainer[size];
        }
    };
}
