package za.ac.myuct.klmedu001.uctmobile.processes.rest;

import java.util.List;

import retrofit.http.GET;
import za.ac.myuct.klmedu001.uctmobile.processes.rest.entity.AllRoutesSQLiteAdapter;
import za.ac.myuct.klmedu001.uctmobile.processes.rest.entity.JammieTimeTablePeriod;
import za.ac.myuct.klmedu001.uctmobile.processes.rest.entity.LastJammieTimeTableUpdate;

/**
 * Created by eduardokolomajr on 2014/08/08.
 */
public interface JammieService {
    @GET("/service/routes/last-time-periods-update")
    LastJammieTimeTableUpdate getLastUpdate();

    @GET("/service/routes/get-time-periods")
    List<JammieTimeTablePeriod> getTimePeriods();

    @GET("/service/routes/get-all-routes")
    List<AllRoutesSQLiteAdapter> getAllRoutes();
}
