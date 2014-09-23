package za.ac.myuct.klmedu001.uctmobile.fragment;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.activeandroid.query.Select;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import za.ac.myuct.klmedu001.uctmobile.JammieAllRoutesAdapter;
import za.ac.myuct.klmedu001.uctmobile.JammieDaysAdapter;
import za.ac.myuct.klmedu001.uctmobile.JammieRouteAdapter;
import za.ac.myuct.klmedu001.uctmobile.MainActivity;
import za.ac.myuct.klmedu001.uctmobile.R;
import za.ac.myuct.klmedu001.uctmobile.constants.BaseApplication;
import za.ac.myuct.klmedu001.uctmobile.constants.LinearLayoutParamsEvaluator;
import za.ac.myuct.klmedu001.uctmobile.constants.ottoposters.JammieAllRoutesClickedEvent;
import za.ac.myuct.klmedu001.uctmobile.constants.ottoposters.JammieDaysClickedEvent;
import za.ac.myuct.klmedu001.uctmobile.constants.ottoposters.JammieRouteClickedEvent;
import za.ac.myuct.klmedu001.uctmobile.processes.rest.container.AllRoutesContainer;
import za.ac.myuct.klmedu001.uctmobile.processes.rest.container.JammieTimeTableBracketContainer;
import za.ac.myuct.klmedu001.uctmobile.processes.rest.container.RouteContainer;

/**
 * Created by eduardokolomajr on 2014/09/21.
 */
public class JammieFragment extends Fragment{
    private final String TAG = "JammieFragment";

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    @InjectView(R.id.jammie_container)
    LinearLayout container;
    @InjectView(R.id.iv_jammie_map)
    ImageView mapImageView;
    @InjectView(R.id.rv_jammie_all_routes)
    RecyclerView allRoutesListView;
    @InjectView(R.id.rv_jammie_routes)
    RecyclerView routesListView;
    @InjectView(R.id.rv_jammie_days)
    RecyclerView daysListView;

    List<AllRoutesContainer> allRoutes;
    List<RouteContainer> routes;

    enum Type {MAP, ALLROUTES, ROUTES, DAYS}
    private String selectedDisplayCode = "";
    private String selectedCode = "";
    private char selectDay = '\0';

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static JammieFragment newInstance(int sectionNumber) {
        JammieFragment fragment = new JammieFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public JammieFragment(){
        allRoutes = new ArrayList<AllRoutesContainer>();
        routes = new ArrayList<RouteContainer>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup view,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_jammies, view, false);

        ButterKnife.inject(this, rootView);

//        if(savedInstanceState == null){
//            allRoutes = new Select().from(AllRoutesContainer.class).execute();
//            routes = new Select().from(RouteContainer.class).execute();
//            allRoutesListView.setAdapter(new JammieAllRoutesAdapter(allRoutes));
//            allRoutesListView.getAdapter().notifyDataSetChanged();
//        }

        allRoutesListView.setLayoutManager(new LinearLayoutManager((getActivity())));
        allRoutesListView.setAdapter(new JammieAllRoutesAdapter(new ArrayList<AllRoutesContainer>()));

        routesListView.setLayoutManager(new LinearLayoutManager((getActivity())));
        routesListView.setAdapter(new JammieRouteAdapter(new ArrayList<RouteContainer>()));

        daysListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        daysListView.setAdapter(new JammieDaysAdapter(new char [0]));

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(getArguments().getInt(ARG_SECTION_NUMBER) > 0)
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));


    }

    @Override
    public void onResume() {
        super.onResume();
        BaseApplication.getEventBus().register(this);
        new GetJammieInfoFromDatabase().execute();
        onRefresh();
    }

    @Override
    public void onStop() {
        super.onStop();
        BaseApplication.getEventBus().unregister(this);
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        ButterKnife.reset(this); //remember to reset butterknife when using fragments
    }

    public void onRefresh(){}

    @Subscribe
    public void onJammieRouteClicked(JammieAllRoutesClickedEvent evt){
        selectedDisplayCode = evt.displayCode;
        //Get AllRouteRecyclerView start and end weights and give to object animator
        ViewGroup.LayoutParams mapStartLP = mapImageView.getLayoutParams();
        LinearLayout.LayoutParams mapEndLP = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1.0f);

        ObjectAnimator mapAnimator = ObjectAnimator.ofObject(mapImageView, "LayoutParams", new LinearLayoutParamsEvaluator(), mapStartLP, mapEndLP);
        mapAnimator.addUpdateListener(new JammieListAnimatorListener(Type.MAP));
        
        //Get AllRoutesRecyclerView start and end weights and give to object animator
        ViewGroup.LayoutParams allRoutesStartLP = allRoutesListView.getLayoutParams();
        ViewGroup.LayoutParams allRoutesEndLP = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 0.0f);
        allRoutesListView.setLayoutParams(allRoutesStartLP);

        ObjectAnimator allRoutesAnimator = ObjectAnimator.ofObject(allRoutesListView, "LayoutParams",
                new LinearLayoutParamsEvaluator(), allRoutesStartLP, allRoutesEndLP);
        allRoutesAnimator.addUpdateListener(new JammieListAnimatorListener(Type.ALLROUTES));

        List<RouteContainer> tempRoutes = new Select().from(RouteContainer.class).where("displayCode = ?", evt.displayCode).execute();
        routesListView.setAdapter(new JammieRouteAdapter(tempRoutes));
        routesListView.getAdapter().notifyDataSetChanged();
        //Get RouteRecyclerView start and end weights and give to object animator
        ViewGroup.LayoutParams routeStartLP = routesListView.getLayoutParams();
        ViewGroup.LayoutParams routeEndLP = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 2.0f);

        ObjectAnimator routesAnimator = ObjectAnimator.ofObject(routesListView, "LayoutParams",
                new LinearLayoutParamsEvaluator(), routeStartLP, routeEndLP);
        routesAnimator.addUpdateListener(new JammieListAnimatorListener(Type.ROUTES));

        AnimatorSet set = new AnimatorSet();
        set.playTogether(allRoutesAnimator, mapAnimator, routesAnimator);
        set.setDuration(500);
        set.start();
        Log.d(TAG, "done animating");
    }

    @Subscribe
    public void onJammieSubRouteClicked(JammieRouteClickedEvent evt){
        selectedCode = evt.code;
        //Get RouteRecyclerView start and end weights and give to object animator
        ViewGroup.LayoutParams routeStartLP = routesListView.getLayoutParams();
        ViewGroup.LayoutParams routeEndLP = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 0f);

        ObjectAnimator routesAnimator = ObjectAnimator.ofObject(routesListView, "LayoutParams",
                new LinearLayoutParamsEvaluator(), routeStartLP, routeEndLP);
        routesAnimator.addUpdateListener(new JammieListAnimatorListener(Type.ROUTES));

        RouteContainer tempRoute = new Select().from(RouteContainer.class).where("code = ?", evt.code).executeSingle();

        char [] days = tempRoute.getOperatingDays().replaceAll(",","").toCharArray();
        for (char day : days) {
            Log.d(TAG+"days", ""+day);
        }
        daysListView.setAdapter(new JammieDaysAdapter(days));
        daysListView.getAdapter().notifyDataSetChanged();
        //Get DaysRecyclerView start and end weights and give to object animator
        ViewGroup.LayoutParams daysStartLP = daysListView.getLayoutParams();
        ViewGroup.LayoutParams daysEndLP = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 2.0f);

        ObjectAnimator daysAnimator = ObjectAnimator.ofObject(daysListView, "LayoutParams",
                new LinearLayoutParamsEvaluator(), daysStartLP, daysEndLP);
        daysAnimator.addUpdateListener(new JammieListAnimatorListener(Type.DAYS));

        AnimatorSet set = new AnimatorSet();
        set.playTogether(routesAnimator, daysAnimator);
        set.setDuration(500);
        set.start();
    }

    @Subscribe
    public void onJammieDayClicked(JammieDaysClickedEvent evt){
        selectDay = evt.dayChar;
        long now = new Date().getTime();
        JammieTimeTableBracketContainer bracket = new Select().from(JammieTimeTableBracketContainer.class)
                .where("start < ? AND end > ?", now, now).executeSingle();
        JammieTimetableFragment fragment = JammieTimetableFragment.newInstance(bracket.getType(), selectedCode, selectDay);
        FragmentManager ft = this.getFragmentManager();
        ft.beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(bracket.getType()+","+selectedCode+","+selectDay)
                .commit();
    }

    private class GetJammieInfoFromDatabase extends AsyncTask <Void, Void, Boolean>{
        List<AllRoutesContainer> tempAllRoutes;
        List<RouteContainer> tempRoutes;
        @Override
        protected Boolean doInBackground(Void... voids) {
            tempAllRoutes = new Select().from(AllRoutesContainer.class).execute();
            tempRoutes = new Select().from(RouteContainer.class).execute();
            return true;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if(success){
                allRoutes.clear();
                allRoutes.addAll(tempAllRoutes);
                routes.clear();
                routes.addAll(tempRoutes);

                for (RouteContainer tempRoute : tempRoutes) {
                    Log.d(TAG+"tempRoutes", tempRoute.getName());
                }

                allRoutesListView.setAdapter(new JammieAllRoutesAdapter(allRoutes));
                allRoutesListView.getAdapter().notifyDataSetChanged();
                routesListView.setAdapter(new JammieRouteAdapter(routes));
                routesListView.getAdapter().notifyDataSetChanged();
            }
        }
    }

    public class JammieListAnimatorListener implements ValueAnimator.AnimatorUpdateListener{
        Type type;

        public JammieListAnimatorListener(Type type) {
            this.type = type;
        }

        @Override
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            switch(type){
                case MAP:
                    mapImageView.setLayoutParams((LinearLayout.LayoutParams)valueAnimator.getAnimatedValue());
                    break;
                case ALLROUTES:
                    allRoutesListView.setLayoutParams((LinearLayout.LayoutParams)valueAnimator.getAnimatedValue());
                    break;
                case ROUTES:
                    routesListView.setLayoutParams((LinearLayout.LayoutParams)valueAnimator.getAnimatedValue());
                    break;
                case DAYS:
                    daysListView.setLayoutParams((LinearLayout.LayoutParams)valueAnimator.getAnimatedValue());
                    break;

            }
        }
    }
}
