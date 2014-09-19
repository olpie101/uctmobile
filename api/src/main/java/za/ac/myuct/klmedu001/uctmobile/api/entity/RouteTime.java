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
    private String routecode;
    private String operatingDayType;
    private String routeStop;
    private String routeTimes;

    public RouteTime(){};

    public RouteTime(String routecode, String operatingDayType, String routeStop, String routeTimes) {
        this.routecode = routecode;
        this.operatingDayType = operatingDayType;
        this.routeStop = routeStop;
        this.routeTimes = routeTimes;
    }

    public String getRoutecode() {
        return routecode;
    }

    public void setRoutecode(String routecode) {
        this.routecode = routecode;
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
                "routeTimes='" + routeTimes + '\'' +
                ", routeStop='" + routeStop + '\'' +
                ", operatingDayType='" + operatingDayType + '\'' +
                ", routecode='" + routecode + '\'' +
                '}';
    }
}
