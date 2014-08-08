package za.ac.myuct.klmedu001.uctmobile.processes.rest;

import retrofit.http.GET;
import za.ac.myuct.klmedu001.uctmobile.processes.rest.entity.LastJammieTimeTableBracketUpdate;

/**
 * Created by eduardokolomajr on 2014/08/08.
 */
public interface JammieService {
    @GET("/service/routes/last-time-periods-update")
    LastJammieTimeTableBracketUpdate lastUpdate();
}
