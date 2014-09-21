package za.ac.myuct.klmedu001.uctmobile.api.entity;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

/**
 * Created by eduardokolomajr on 2014/09/19.
 */
@Entity
public class RouteTime {
    @Id @Index
    private Long Id;
    private String bracket;
    private String routeCode;
    private String operatingDayType;
    private String routeStop;
    private String routeTimes;

    public RouteTime(){};

    public RouteTime(String bracket, String routecode, String operatingDayType, String routeStop, String routeTimes) {
        this.bracket = bracket;
        this.routeCode = routecode;
        this.operatingDayType = operatingDayType;
        this.routeStop = routeStop;
        this.routeTimes = routeTimes;
    }

    public String getBracket() { return bracket; }

    public void setBracket(String bracket) { this.bracket = bracket; }

    public String getRouteCode() {
        return routeCode;
    }

    public void setRouteCode(String routeCode) {
        this.routeCode = routeCode;
    }

    public String getOperatingDayType() {
        return operatingDayType;
    }

    public void setOperatingDayType(String operatingDayType) {
        this.operatingDayType = operatingDayType;
    }

    public String getRouteStop() {
        return routeStop;
    }

    public void setRouteStop(String routeStop) {
        this.routeStop = routeStop;
    }

    public String getRouteTimes() {
        return routeTimes;
    }

    public void setRouteTimes(String routeTimes) {
        this.routeTimes = routeTimes;
    }

    @Override
    public String toString() {
        return "RouteTime{" +
                "Id=" + Id +
                ", bracket='" + bracket + '\'' +
                ", routeCode='" + routeCode + '\'' +
                ", operatingDayType='" + operatingDayType + '\'' +
                ", routeStop='" + routeStop + '\'' +
                ", routeTimes='" + routeTimes + '\'' +
                '}';
    }
}
