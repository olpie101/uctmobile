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
    String availability; // Periods it is available (term, vac, etc)

    @SuppressWarnings("unused")
    public AllRoutes() {
    }

    public AllRoutes(String route, String displayCode) {
        this.route = route;
        this.displayCode = displayCode;
        this.availability = "Pilot,Term,Vac,Exam";
    }

    public AllRoutes(String availability, String route, String displayCode) {
        this.route = route;
        this.displayCode = displayCode;
        this.availability = availability;
    }


    @SuppressWarnings("unused")
    public String getRoute() {
        return route;
    }

    @SuppressWarnings("unused")
    public void setRoute(String route) {
        this.route = route;
    }

    @SuppressWarnings("unused")
    public String getDisplayCode() {
        return displayCode;
    }

    @SuppressWarnings("unused")
    public void setDisplayCode(String displayCode) {
        this.displayCode = displayCode;
    }

    @SuppressWarnings("unused")
    public String getAvailability() { return availability; }

    @SuppressWarnings("unused")
    public void setAvailability(String availability) { this.availability = availability; }

    @Override
    public String toString() {
        return "AllRoutes{" +
                "route='" + route + "'\n" +
                ", displayCode='" + displayCode + "'\n" +
                "availability{"+availability + "'\n" +
                "}";
    }
}
