package za.ac.myuct.klmedu001.uctmobile.api.entity;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

/**
 * Created by eduardokolomajr on 2014/08/05.
 */
@Entity
public class AllRoutes{
    @Id
    @Index
    String route;
    String displayCode;

    public AllRoutes() {
    }

    public AllRoutes(String route, String displayCode) {
        this.route = route;
        this.displayCode = displayCode;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getDisplayCode() {
        return displayCode;
    }

    public void setDisplayCode(String displayCode) {
        this.displayCode = displayCode;
    }

    @Override
    public String toString() {
        return "AllRoutes{" +
                "route='" + route + "'" +
                ", displayCode='" + displayCode + "'" +
                '}';
    }
}
