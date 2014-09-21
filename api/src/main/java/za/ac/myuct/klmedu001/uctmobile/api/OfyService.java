package za.ac.myuct.klmedu001.uctmobile.api;

/**
 * Created by eduardokolomajr on 2014/09/16.
 */
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;

import za.ac.myuct.klmedu001.uctmobile.api.entity.AllRoutes;
import za.ac.myuct.klmedu001.uctmobile.api.entity.JammieTimeTableBracket;
import za.ac.myuct.klmedu001.uctmobile.api.entity.LastJammieTimeTableBracketUpdate;
import za.ac.myuct.klmedu001.uctmobile.api.entity.Route;
import za.ac.myuct.klmedu001.uctmobile.api.entity.RouteTime;

/**
 * Objectify service wrapper so we can statically register our persistence classes
 * More on Objectify here : https://code.google.com/p/objectify-appengine/
 *
 */
public class OfyService {

    static {
        ObjectifyService.register(AllRoutes.class);
        ObjectifyService.register(Route.class);
        ObjectifyService.register(JammieTimeTableBracket.class);
        ObjectifyService.register(LastJammieTimeTableBracketUpdate.class);
        ObjectifyService.register(RouteTime.class);
    }

    public static Objectify ofy() {
        return ObjectifyService.ofy();
    }

    public static ObjectifyFactory factory() {
        return ObjectifyService.factory();
    }
}
