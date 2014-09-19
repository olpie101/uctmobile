package za.ac.myuct.klmedu001.uctmobile.processes.rest.entity;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import za.ac.myuct.klmedu001.uctmobile.api.endpoints.jammieEndpoint.model.AllRoutes;

/**
 * Created by eduardokolomajr on 2014/08/08.
 */
@Table(name = "allRoutes")
public class AllRoutesSQLiteAdapter extends Model {
    @Column String route;
    @Column String displayCode;

    public AllRoutesSQLiteAdapter() {
    }

    public AllRoutesSQLiteAdapter(AllRoutes route) {
        this.route = route.getRoute();
        this.displayCode = route.getDisplayCode();
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

    //query all command
    private void removeAll(){
        new Select().from(AllRoutesSQLiteAdapter.class).execute();
        //delete command
        new Delete().from(AllRoutesSQLiteAdapter.class).execute();
    }
}
