package za.ac.myuct.klmedu001.uctmobile.processes.rest.container;

import android.os.Parcel;
import android.os.Parcelable;

import com.grosner.dbflow.annotation.Column;
import com.grosner.dbflow.annotation.Table;
import com.grosner.dbflow.structure.BaseModel;

import za.ac.myuct.klmedu001.uctmobile.api.endpoints.jammieEndpoint.model.RouteTime;
import za.ac.myuct.klmedu001.uctmobile.constants.AppDatabase;

/**
 * Created by eduardokolomajr on 2014/09/19.
 */
@Table(databaseName = AppDatabase.NAME, value = AppDatabase.TABLE_ROUTE_TIME)
public class RouteTimeContainer extends BaseModel implements Parcelable {
    @Column (columnType = Column.PRIMARY_KEY_AUTO_INCREMENT) long _id;
    @Column(unique = true) Long internalId;
    @Column String bracket;
    @Column String routeCode;
    @Column String operatingDayType;
    @Column String routeStop;
    @Column String routeTimes;

    @SuppressWarnings("unused")
    public RouteTimeContainer() {
    }

    public RouteTimeContainer (RouteTime in){
        this.internalId = in.getInternalId();
        this.bracket = in.getBracket();
        this.routeCode = in.getRouteCode();
        this.operatingDayType = in.getOperatingDayType();
        this.routeStop = in.getRouteStop();
        this.routeTimes = in.getRouteTimes();
    }

    public Long getInternalId() { return internalId; }

    public void setInternalId(Long internalId) { this.internalId = internalId; }

    public String getBracket() { return bracket; }

    public void setBracket(String bracket) { this.bracket = bracket; }

    public String getRouteCode() { return routeCode; }

    public void setRouteCode(String routeCode) { this.routeCode = routeCode; }

    public String getOperatingDayType() { return operatingDayType; }

    public void setOperatingDayType(String operatingDayType) { this.operatingDayType = operatingDayType; }

    public String getRouteStop() { return routeStop; }

    public void setRouteStop(String routeStop) { this.routeStop = routeStop; }

    public String getRouteTimes() { return routeTimes; }

    public void setRouteTimes(String routeTimes) { this.routeTimes = routeTimes; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.bracket);
        dest.writeString(this.routeCode);
        dest.writeString(this.operatingDayType);
        dest.writeString(this.routeStop);
        dest.writeString(this.routeTimes);
    }

    private RouteTimeContainer(Parcel in) {
        this.bracket = in.readString();
        this.routeCode = in.readString();
        this.operatingDayType = in.readString();
        this.routeStop = in.readString();
        this.routeTimes = in.readString();
    }

    public static final Creator<RouteTimeContainer> CREATOR = new Creator<RouteTimeContainer>() {
        public RouteTimeContainer createFromParcel(Parcel source) {
            return new RouteTimeContainer(source);
        }

        public RouteTimeContainer[] newArray(int size) {
            return new RouteTimeContainer[size];
        }
    };
}
