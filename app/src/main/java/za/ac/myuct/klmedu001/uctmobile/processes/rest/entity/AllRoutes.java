package za.ac.myuct.klmedu001.uctmobile.processes.rest.entity;

/**
 * Created by eduardokolomajr on 2014/08/08.
 */
public class AllRoutes {
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
                "route='" + route + '\'' +
                ", displayCode='" + displayCode + '\'' +
                '}';
    }
}
