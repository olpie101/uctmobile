package za.ac.myuct.klmedu001.uctmobile.constantsandprocesses.rest;

import java.util.List;

import retrofit.http.GET;

/**
 * Created by eduardokolomajr on 2014/08/08.
 */
public interface JammieService {
    @GET("/service/routes/last-time-periods-update")
    LastJammieTimeTableBracketUpdate lastUpdate();
}
