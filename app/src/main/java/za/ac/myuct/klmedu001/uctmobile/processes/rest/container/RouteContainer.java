package za.ac.myuct.klmedu001.uctmobile.processes.rest.container;

import android.os.Parcel;
import android.os.Parcelable;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import za.ac.myuct.klmedu001.uctmobile.api.endpoints.jammieEndpoint.model.Route;
import za.ac.myuct.klmedu001.uctmobile.constants.AppDatabase;

/**
 * Created by eduardokolomajr on 2014/09/19.
 */
@Table(databaseName = AppDatabase.NAME, value = AppDatabase.TABLE_ROUTE)
public class RouteContainer extends BaseModel implements Parcelable {
    @Column (columnType = Column.PRIMARY_KEY_AUTO_INCREMENT) long _id;
    @Column String availability;    // Periods it is available (term, vac, etc)
    @Column String name;            //Route name
    @Column String displayCode;     //actual code to display (code seen on jammie timetables publically)
    @Column String code;            //internal code (code used to differentiate between the different types and day times)
    @Column String operatingDays;   //days set for operation

    @SuppressWarnings("unused")
    public RouteContainer(){}

    public RouteContainer(Route in){
        super();
        this.name = in.getName();
        this.displayCode = in.getDisplayCode();
        this.code = in.getCode();
        this.operatingDays = in.getOperatingDays();
        this.availability = in.getAvailability();
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

    public String getAvailability() { return availability; }

    public void setAvailability(String availability) { this.availability = availability; }

    @Override
    public int describeContents() {
        return 5;
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
