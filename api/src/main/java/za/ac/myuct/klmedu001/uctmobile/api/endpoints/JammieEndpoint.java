package za.ac.myuct.klmedu001.uctmobile.api.endpoints;

import com.google.api.server.spi.SystemServiceServlet;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.ConflictException;
import com.google.api.server.spi.response.NotFoundException;

import java.applet.AppletContext;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Logger;

import javax.inject.Named;
import javax.servlet.ServletContext;

import static za.ac.myuct.klmedu001.uctmobile.api.OfyService.ofy;

import za.ac.myuct.klmedu001.uctmobile.api.entity.AllRoutes;
import za.ac.myuct.klmedu001.uctmobile.api.entity.JammieTimeTableBracket;
import za.ac.myuct.klmedu001.uctmobile.api.entity.LastJammieTimeTableBracketUpdate;
import za.ac.myuct.klmedu001.uctmobile.api.entity.Route;
import za.ac.myuct.klmedu001.uctmobile.api.entity.RouteTime;
import za.ac.myuct.klmedu001.uctmobile.api.entity.transformed.LastJammieTimeTableBracketUpdateTransformed;

/**
 * Created by eduardokolomajr on 2014/09/16.
 */
@Api (name = "jammieEndpoint", version = "v1", description = "Access Jammie routes and timetables", namespace = @ApiNamespace (
        ownerDomain = "endpoints.api.uctmobile.klmedu001.myuct.ac.za",
        ownerName = "endpoints.api.uctmobile.klmedu001.myuct.ac.za", packagePath = ""))
public class JammieEndpoint {
    // Make sure to add this endpoint to your web.xml file if this is a web application.

    private static final Logger LOG = Logger.getLogger(JammieEndpoint.class.getName());

    /**
     * This method gets all the <code>AllRoutes</code> objects.
     * @return The all available <code>AllRoutes</code>.
     */
    @ApiMethod(name = "getAllRoutes")
    public List<AllRoutes> getAllRoutes() {
        // Implement this function

        List<AllRoutes> list = ofy().load().type(AllRoutes.class).orderKey(false).list();

        LOG.info("Calling getAllRoutes method");
        return list;
    }

    /**
     * This creates all routes necessary <code>AllRoutes</code> object.
     * @return list of all <code>AllRoutes</code> created.
     */
    @ApiMethod(name = "createAllRoutes")
    public List<AllRoutes> createAllRoutes() throws ConflictException{
        // Implement this function

        if(ofy().load().type(AllRoutes.class).list().size() > 0)
            throw new ConflictException("The routes list has been created already");

        List<AllRoutes> routes = new ArrayList<AllRoutes>();
        routes.add(new AllRoutes("Bremner", "4B"));
        routes.add(new AllRoutes("Claremont", "1"));
        routes.add(new AllRoutes("Clarinus", "6"));
        routes.add(new AllRoutes("Forest Hill", "5"));
        routes.add(new AllRoutes("Pilot", "Groote Schuur", "3A"));
        routes.add(new AllRoutes("Hiddingh Hall", "10"));
        routes.add(new AllRoutes("Liesbeeck", "9"));
        routes.add(new AllRoutes("Medical School (anti-clockwise)", "9C"));
        routes.add(new AllRoutes("Mowbray", "8"));
        routes.add(new AllRoutes("Obz Square", "9C"));
        routes.add(new AllRoutes("Residence Loop", "9B"));
        routes.add(new AllRoutes("Rochester", "7"));
        routes.add(new AllRoutes("Sandown", "3"));
        routes.add(new AllRoutes("Tugwell", "4"));

        ofy().save().entities(routes).now();

        LOG.info("Calling insertAllRoutes method");
        return routes;
    }

    /**
     * This get the last time the time tables were updated
     * @return Date of last update
     */
    @ApiMethod(name = "getLastUpdate")
    public LastJammieTimeTableBracketUpdate getLastUpdate(){
        List<LastJammieTimeTableBracketUpdate> list = ofy().load().type(LastJammieTimeTableBracketUpdate.class).list();
        LastJammieTimeTableBracketUpdate lastUpdate = list.get(list.size() - 1);
        return lastUpdate;
    }

    /**
     * This gets all the jammie time table brackets (ie. term, vacation, etc)
     * @return List of all available brackets
     */
    @ApiMethod(name = "getTimeTableBrackets")
    public List<JammieTimeTableBracket> getTimeTableBrackets(){
        return ofy().load().type(JammieTimeTableBracket.class).orderKey(false).list();
    }

    @ApiMethod (name="createTimeTableBrackets")
    public List<JammieTimeTableBracket> createTimeTableBrackets(){
        //Months are always represented one less (ie. 0-11)
        List<JammieTimeTableBracket> brackets = new ArrayList<JammieTimeTableBracket>();
        TimeZone SAST = TimeZone.getTimeZone("Africa/Johannesburg");

        GregorianCalendar start = new GregorianCalendar();
        GregorianCalendar end = new GregorianCalendar(2014, 8, 31);
        start.setTimeZone(SAST);
        end.setTimeZone(SAST);
        brackets.add(new JammieTimeTableBracket("Pilot", start, end));

        start = new GregorianCalendar(2014, 9, 1);
        end = new GregorianCalendar(2014, 10, 12);
        start.setTimeZone(SAST);
        end.setTimeZone(SAST);
        brackets.add(new JammieTimeTableBracket("Term", start, end));

        start = new GregorianCalendar(2014, 10, 13);
        end = new GregorianCalendar(2015, 1, 15);
        start.setTimeZone(SAST);
        end.setTimeZone(SAST);
        brackets.add(new JammieTimeTableBracket("Vac", start, end));

        ofy().save().entities(brackets).now();
        ofy().save().entity(new LastJammieTimeTableBracketUpdate(new GregorianCalendar()));
        return brackets;
    }

    @ApiMethod(name = "createRoutes")
    public List<Route> createRoutes() throws ConflictException {
        File f = new File("assets/JammieRoutes.csv");
        ArrayList<Route> routes = new ArrayList<Route>();
        if(f.exists()){
            try {
                BufferedReader br = new BufferedReader(new FileReader(f));
                String line;
                while((line = br.readLine()) != null){
                    String [] entry = line.split(",");
                    routes.add(new Route(entry[0].replaceAll(";", ","), entry[1], entry[2], entry[3], entry[4].replaceAll(";", ",")));
                }
                ofy().save().entities(routes).now();
            }catch(IOException e){
                throw new ConflictException("error creating routes");
            }

        }
        return routes;
    }

    @ApiMethod(name = "getRoutes")
    public List<Route> getRoutes(){
        return ofy().load().type(Route.class).list();
    }

    @ApiMethod(name = "createRouteTimes")
    public List<RouteTime> createRouteTimes () throws ConflictException {
        File f = new File("assets/RouteTimes.csv");
        ArrayList<RouteTime> routeTimes= new ArrayList<RouteTime>();
        if(f.exists()){
            try{
                BufferedReader br = new BufferedReader(new FileReader(f));
                String line;
                while((line = br.readLine()) != null){
                    String [] entry = line.split(",");
                    routeTimes.add(new RouteTime(entry[0], entry[1], entry[2], entry[3], entry[4], entry[5].replaceAll(";", ",")));
                }
                ofy().save().entities(routeTimes).now();
            }catch(IOException e){
                throw new ConflictException("error creating routeTimes");
            }
        }
        return routeTimes;
    }

    @ApiMethod(name = "getRouteTime")
    public List<RouteTime> getRouteTimes(){return ofy().load().type(RouteTime.class).order("internalId").list();}

    @ApiMethod(name = "setUpEnvironment")
    public LastJammieTimeTableBracketUpdate setUpEnvironment () throws ConflictException {
        createTimeTableBrackets();
        createAllRoutes();
        createRoutes();
        createRouteTimes();

        return getLastUpdate();
    }
}
