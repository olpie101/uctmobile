package za.ac.myuct.klmedu001.uctmobile.processes.loaders;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.grosner.dbflow.runtime.DBTransactionInfo;
import com.grosner.dbflow.runtime.TransactionManager;
import com.grosner.dbflow.runtime.transaction.process.ProcessModelInfo;
import com.grosner.dbflow.sql.language.Delete;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import za.ac.myuct.klmedu001.uctmobile.api.endpoints.jammieEndpoint.JammieEndpoint;
import za.ac.myuct.klmedu001.uctmobile.api.endpoints.jammieEndpoint.model.AllRoutes;
import za.ac.myuct.klmedu001.uctmobile.api.endpoints.jammieEndpoint.model.JammieTimeTableBracketTransformed;
import za.ac.myuct.klmedu001.uctmobile.api.endpoints.jammieEndpoint.model.Route;
import za.ac.myuct.klmedu001.uctmobile.api.endpoints.jammieEndpoint.model.RouteTime;
import za.ac.myuct.klmedu001.uctmobile.constants.UCTConstants;
import za.ac.myuct.klmedu001.uctmobile.processes.rest.container.AllRoutesContainer;
import za.ac.myuct.klmedu001.uctmobile.processes.rest.container.JammieTimeTableBracketContainer;
import za.ac.myuct.klmedu001.uctmobile.processes.rest.container.RouteContainer;
import za.ac.myuct.klmedu001.uctmobile.processes.rest.container.RouteTimeContainer;


/**
 * Created by eduardokolomajr on 2014/08/08.
 */
public class JammieTimeTableLoader extends AsyncTaskLoader<Boolean> {
    private final String TAG = "JammieTimeTableLoader";
    private static JammieEndpoint myApiJammieService = null;

    // We hold a reference to the Loader’s success here.
    boolean success = false;

    public JammieTimeTableLoader(Context ctx) {
        // Loaders may be used across multiple Activity's (assuming they aren't
        // bound to the LoaderManager), so NEVER hold a reference to the context
        // directly. Doing so will cause you to leak an entire Activity's context.
        // The superclass constructor will store a reference to the Application
        // Context instead, and can be retrieved with a call to getContext().
        super(ctx);
        Log.d(TAG, "created");
    }

    /****************************************************/
    /** (1) A task that performs the asynchronous load **/
    /**
     * ************************************************
     */

    @Override
    public Boolean loadInBackground() {
        Log.d(TAG, "BG");


        // This method is called on a background thread and should generate a
        // new set of data to be delivered back to the client.


        List<JammieTimeTableBracketTransformed> timeTableBrackets ;
        List<AllRoutes> allRoutes;
        List<Route> routes;
        List<RouteTime> routeTimes;

        List<JammieTimeTableBracketContainer> timeTableBracketContainers = new ArrayList<JammieTimeTableBracketContainer>();
        List<AllRoutesContainer> allRoutesContainers = new ArrayList<AllRoutesContainer>();
        List<RouteContainer> routeContainers = new ArrayList<RouteContainer>();
        List<RouteTimeContainer> routeTimeContainers = new ArrayList<RouteTimeContainer>();

        if(myApiJammieService == null) {
            JammieEndpoint.Builder builder = UCTConstants.jammieEndpointBuilder;
            myApiJammieService = builder.build();
        }

        try {
            timeTableBrackets = myApiJammieService.getTimeTableBrackets().execute().getItems();

            for (JammieTimeTableBracketTransformed timeTableBracket : timeTableBrackets) {
//                Log.d(TAG+"Bracket", timeTableBracket.getType()+" =>"+timeTableBracket.getStart());
                timeTableBracketContainers.add(new JammieTimeTableBracketContainer(timeTableBracket));
            }
            timeTableBrackets.clear();

            allRoutes = myApiJammieService.getAllRoutes().execute().getItems();

            for (AllRoutes allRoute : allRoutes) {
//                Log.d(TAG+"AllRoutes", allRoute.getRoute());
                allRoutesContainers.add(new AllRoutesContainer(allRoute));
            }
            allRoutes.clear();

            routes = myApiJammieService.getRoutes().execute().getItems();

            for(Route route : routes){
//                Log.d(TAG+"Routes", route.getName());
                routeContainers.add(new RouteContainer(route));
            }
            routes.clear();

            routeTimes = myApiJammieService.getRouteTime().execute().getItems();

            for(RouteTime routeTime : routeTimes) {
//                Log.d(TAG + "RouteTime", routeTime.getRoutecode());
                routeTimeContainers.add(new RouteTimeContainer(routeTime));
            }
            routeTimes.clear();

            TransactionManager.getInstance().delete(DBTransactionInfo.create(), JammieTimeTableBracketContainer.class);
            new Delete().from(AllRoutesContainer.class);
            new Delete().from(RouteContainer.class);
            new Delete().from(RouteTimeContainer.class);

            long lastUpdateTime = myApiJammieService.getLastUpdate().execute().getDate();

            try {
                TransactionManager.getInstance().save(ProcessModelInfo.withModels(timeTableBracketContainers));
                TransactionManager.getInstance().save(ProcessModelInfo.withModels(allRoutesContainers));
                TransactionManager.getInstance().save(ProcessModelInfo.withModels(routeContainers));
                TransactionManager.getInstance().save(ProcessModelInfo.withModels(routeTimeContainers));

                SharedPreferences prefs = getContext()
                        .getSharedPreferences(UCTConstants.SHARED_PREFS, getContext().MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putLong(UCTConstants.PREFS_LAST_JAMMIE_UPDATE, lastUpdateTime);
                editor.apply();
            }finally {

            }

        } catch (IOException e) {
            Log.d(TAG+"error","Error loading jammies");
            e.printStackTrace();
        }

        Log.d(TAG, "done loading Jammies");
        return success;
    }

    /********************************************************/
    /** (2) Deliver the results to the registered listener **/
    /**
     * ****************************************************
     */

    @Override
    public void deliverResult(Boolean successResp) {
        if (isReset()) {
            // The Loader has been reset; ignore the result and invalidate the successResp.
            return;
        }

        // Hold a reference to the old successResp so it doesn't get garbage collected.
        // We must protect it until the new successResp has been delivered.
        Boolean oldData = success;
        success = successResp;

        if (isStarted()) {
            // If the Loader is in a started state, deliver the results to the
            // client. The superclass method does this for us.
            super.deliverResult(successResp);
        }
    }

    /*********************************************************/
    /** (3) Implement the Loader’s state-dependent behavior **/
    /**
     * *****************************************************
     */

    @Override
    protected void onStartLoading() {
        //Working with a boolean. nothing to do here
    }

    @Override
    protected void onStopLoading() {
        // The Loader is in a stopped state, so we should attempt to cancel the
        // current load (if there is one).
        cancelLoad();

        // Note that we leave the observer as is. Loaders in a stopped state
        // should still monitor the data source for changes so that the Loader
        // will know to force a new load if it is ever started again.
    }

    @Override
    protected void onReset() {
        // Ensure the loader has been stopped.
        onStopLoading();
        //Working with a boolean. nothing to do here
    }

    @Override
    public void onCanceled(Boolean data) {
        // Attempt to cancel the current asynchronous load.
        super.onCanceled(data);
        //Working with a boolean. nothing to do here
    }
}
