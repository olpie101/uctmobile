package za.ac.myuct.klmedu001.uctmobile.fragment;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import za.ac.myuct.klmedu001.uctmobile.constants.ottoposters.BackPressedEvent;
import za.ac.myuct.klmedu001.uctmobile.constants.ottoposters.JammieAllRoutesClickedEvent;
import za.ac.myuct.klmedu001.uctmobile.constants.ottoposters.JammieDaysClickedEvent;
import za.ac.myuct.klmedu001.uctmobile.constants.ottoposters.JammieRouteClickedEvent;
import za.ac.myuct.klmedu001.uctmobile.processes.AnimatorWeightHolder;
import za.ac.myuct.klmedu001.uctmobile.processes.rest.container.AllRoutesContainer;
import za.ac.myuct.klmedu001.uctmobile.processes.rest.container.JammieTimeTableBracketContainer;
import za.ac.myuct.klmedu001.uctmobile.processes.rest.container.RouteContainer;

/**
 * Created by eduardokolomajr on 2014/09/21.
 */
public class JammieFragment extends Fragment {
    private final String TAG = "JammieFragment";

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    @InjectView(R.id.jammie_container)
    LinearLayout container;
    @InjectView(R.id.ll_jammie_all_routes_container)
    LinearLayout allRoutesContainer;
    @InjectView(R.id.ll_jammie_routes_container)
    LinearLayout routesContainer;
    @InjectView(R.id.ll_jammie_days_container)
    LinearLayout daysContainer;
    @InjectView(R.id.iv_jammie_map)
    ImageView mapImageView;
    @InjectView(R.id.rv_jammie_all_routes)
    RecyclerView allRoutesListView;
    @InjectView(R.id.rv_jammie_routes)
    RecyclerView routesListView;
    @InjectView(R.id.rv_jammie_days)
    RecyclerView daysListView;
    @InjectView(R.id.tv_jammie_breadcrumb)
    TextView breadcrumb;

    private enum State {ALL_ROUTES_OPEN, SUB_ROUTE_OPEN, DAY_OPEN}
    private State state = State.ALL_ROUTES_OPEN;

    private JammieTimeTableBracketContainer bracket;
    private List<AllRoutesContainer> allRoutes;
    private List<RouteContainer> routes;
    private char [] days;


    private String selectedRoute = "";
    private String selectedSubRoute = "";
    private String selectedDisplayCode = "";
    private String selectedCode = "";
    private char selectDay = '\0';

    private int allRoutesPosition;
    private int routesPosition;

    boolean returningFromTimetable = false;

    private float noWeight = 0.0f;
    private float fullWeight = 9.0f;
    private float minorWeight = 1.0f;
    private float partialWeight = 3.0f;
    private float majorWeight = 5.0f;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup view,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_jammies, view, false);
        if (savedInstanceState == null) {
            ButterKnife.inject(this, rootView);

            allRoutesListView.setLayoutManager(new LinearLayoutManager((getActivity())));
            routesListView.setLayoutManager(new LinearLayoutManager((getActivity())));
            daysListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }

        if(returningFromTimetable){
            selectDay = '\0';
            allRoutesListView.setAdapter(new JammieAllRoutesAdapter(allRoutes));
            allRoutesListView.getAdapter().notifyDataSetChanged();
            routesListView.setAdapter(new JammieRouteAdapter(routes));
            routesListView.getAdapter().notifyDataSetChanged();
            daysListView.setAdapter(new JammieDaysAdapter(days));
            daysListView.getAdapter().notifyDataSetChanged();
        }else{
            long now = new Date().getTime();
            bracket = new Select().from(JammieTimeTableBracketContainer.class)
                    .where("start < ? AND end > ?", now, now).executeSingle();
            List<AllRoutesContainer> tempAllRoutes = new Select().from(AllRoutesContainer.class)
                    .where("bracket LIKE ?", "%"+bracket.getType()+"%").execute();
            List<RouteContainer> tempRoutes = new Select().from(RouteContainer.class)
                    .where("bracket LIKE ?", "%" + bracket.getType() + "%").execute();

            allRoutes.clear();
            allRoutes.addAll(tempAllRoutes);
            routes.clear();
            routes.addAll(tempRoutes);
            days = new char [0];
        }
        allRoutesListView.setAdapter(new JammieAllRoutesAdapter(allRoutes));
        allRoutesListView.getAdapter().notifyDataSetChanged();
        routesListView.setAdapter(new JammieRouteAdapter(routes));
        routesListView.getAdapter().notifyDataSetChanged();
        daysListView.setAdapter(new JammieDaysAdapter(days));
        daysListView.getAdapter().notifyDataSetChanged();
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
//        Log.d(TAG, "resuming");
        getActivity().setTitle("Jammie Shuttle");
        if (returningFromTimetable){
            restoreViewState();
            returningFromTimetable = false;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        BaseApplication.getEventBus().unregister(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        Log.d(TAG, "oSIS");
        outState.putParcelableArrayList("allRoutes", (ArrayList<? extends Parcelable>)allRoutes);
        outState.putParcelableArrayList("routes", (ArrayList<? extends Parcelable>)routes);
        outState.putCharArray("days", days);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
//        Log.d(TAG, "restoring view state");
        if(savedInstanceState != null)
            Log.d(TAG, "VSR has data");
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        ButterKnife.reset(this); //remember to reset butterknife when using fragments
//        Log.d(TAG, "destroying view");
    }

    @Override
    public void onDestroy() {
//        Log.d(TAG, "destroying fragment");
        super.onDestroy();
    }

    @Subscribe
    public void onBackPressed(BackPressedEvent evt){
        long delta = new Date().getTime() - evt.start.getTime();
        evt.intercepted = true;
        Log.d(TAG, "time to intercept = "+delta);
        switch(state){
            case ALL_ROUTES_OPEN:
                evt.intercepted = false;
                break;
            default:
                evt.intercepted = true;

        }

        switch (state){
            case SUB_ROUTE_OPEN:
                state = State.ALL_ROUTES_OPEN;
                routesPosition = 0;
                selectedDisplayCode = "";
                selectedRoute = "";

                AnimatorWeightHolder v1 = new AnimatorWeightHolder(routesContainer, noWeight);
                AnimatorWeightHolder v2 = new AnimatorWeightHolder(allRoutesContainer, fullWeight);
                AnimatorWeightHolder v3 = new AnimatorWeightHolder(mapImageView, noWeight);
                AnimatorWeightHolder v4 = new AnimatorWeightHolder(breadcrumb, noWeight);
                animateViews(300, v1, v2, v3, v4);
                break;
            case DAY_OPEN:
                state = State.SUB_ROUTE_OPEN;
                selectedCode = "";
                selectedSubRoute = "";
                if(routes.size() == 1){
                    BaseApplication.getEventBus().post(new BackPressedEvent());
                }
                setBreadcrumb();
                AnimatorWeightHolder v5 = new AnimatorWeightHolder(routesContainer, majorWeight);
                AnimatorWeightHolder v6 = new AnimatorWeightHolder(daysContainer, noWeight);
                animateViews(300, v5, v6);
                break;
        }

    }

    private void restoreViewState(){
        Log.d(TAG, "restoring view state");
        float breadcrumbWeight, mapWeight, allRoutesWeight, routesWeight, daysWeight;
        breadcrumbWeight = mapWeight = allRoutesWeight =  routesWeight = daysWeight = noWeight;
        switch(state){
            case ALL_ROUTES_OPEN:
                allRoutesWeight = fullWeight;
                break;
            case SUB_ROUTE_OPEN:
                breadcrumbWeight = minorWeight;
                mapWeight = partialWeight;
                routesWeight = majorWeight;
                break;
            case DAY_OPEN:
                breadcrumbWeight = minorWeight;
                mapWeight = partialWeight;
                daysWeight = majorWeight;
                break;
        }

        Log.d(TAG, breadcrumbWeight+","+mapWeight+","+allRoutesWeight+","+routesWeight+","+daysWeight);
        AnimatorWeightHolder v1 = new AnimatorWeightHolder(breadcrumb, breadcrumbWeight);
        AnimatorWeightHolder v2 = new AnimatorWeightHolder(mapImageView, mapWeight);
        AnimatorWeightHolder v3 = new AnimatorWeightHolder(allRoutesContainer, allRoutesWeight);
        AnimatorWeightHolder v4 = new AnimatorWeightHolder(routesContainer, routesWeight);
        AnimatorWeightHolder v5 = new AnimatorWeightHolder(daysContainer, daysWeight);
        animateViews(1, v1, v2, v3, v4, v5);

        allRoutesListView.scrollToPosition(allRoutesPosition);
        routesListView.scrollToPosition(routesPosition);

        setBreadcrumb();
    }

    private void setBreadcrumb(){
        String editedText = selectedSubRoute.replace(selectedRoute, "").trim();
        if(selectedRoute.equalsIgnoreCase(editedText) || editedText.length() == 0){
            breadcrumb.setText(selectedRoute);
        }else {
            breadcrumb.setText(selectedRoute + " - " + editedText);
        }
    }

    @Subscribe
    public void onJammieRouteClicked(JammieAllRoutesClickedEvent evt){
        state = State.SUB_ROUTE_OPEN;
        allRoutesPosition = evt.position;
        selectedDisplayCode = evt.displayCode;
        selectedRoute = evt.name.toString();

        routes = new Select().from(RouteContainer.class).where("displayCode = ? AND bracket LIKE ?",
                evt.displayCode, "%"+bracket.getType()+"%").execute();
        routesListView.setAdapter(new JammieRouteAdapter(routes));
        routesListView.getAdapter().notifyDataSetChanged();

        AnimatorWeightHolder v1 = new AnimatorWeightHolder(breadcrumb, minorWeight);
        AnimatorWeightHolder v2 = new AnimatorWeightHolder(mapImageView, partialWeight);
        AnimatorWeightHolder v3 = new AnimatorWeightHolder(allRoutesContainer, noWeight);
        AnimatorWeightHolder v4 = new AnimatorWeightHolder(routesContainer, noWeight);
        if(routes.size() == 1){
            RouteContainer route = routes.get(0);

            BaseApplication.getEventBus().post(new JammieRouteClickedEvent(selectedRoute, route.getDisplayCode(),
                    route.getCode(), 0));
        }else {
            setBreadcrumb();
            v4 = new AnimatorWeightHolder(routesContainer, majorWeight);
        }
        animateViews(300, v1, v2, v3, v4);
    }

    @Subscribe
    public void onJammieSubRouteClicked(JammieRouteClickedEvent evt){
        state = State.DAY_OPEN;
        routesPosition = evt.position;
        selectedCode = evt.code;
        selectedSubRoute = evt.name;

        RouteContainer tempRoute = new Select().from(RouteContainer.class).where("code = ?", evt.code).executeSingle();
        days = tempRoute.getOperatingDays().replaceAll(",","").toCharArray();
        daysListView.setAdapter(new JammieDaysAdapter(days));
        daysListView.getAdapter().notifyDataSetChanged();

        this.setBreadcrumb();

        AnimatorWeightHolder v1 = new AnimatorWeightHolder(routesContainer, noWeight);
        AnimatorWeightHolder v2 = new AnimatorWeightHolder(daysContainer, majorWeight);
        animateViews(300, v1, v2);
    }

    @Subscribe
    public void onJammieDayClicked(JammieDaysClickedEvent evt){
        selectDay = evt.dayChar;

        Log.d(TAG+"dayClicked", bracket.getType());
        String finalTitle = selectedRoute+" - "+selectedSubRoute.replace(selectedRoute, "").trim();
        JammieTimetableFragment fragment = JammieTimetableFragment.newInstance(finalTitle, bracket.getType(), selectedCode, selectDay);
        FragmentManager ft = this.getFragmentManager();
        returningFromTimetable = true;
        ft.beginTransaction()
                .setCustomAnimations(R.anim.pull_in_right, R.anim.push_out_left, R.anim.pull_in_left, R.anim.push_out_right)
                .replace(R.id.container, fragment)
                .addToBackStack(bracket.getType()+","+selectedCode+","+selectDay)
                .commit();
    }

    /**
     * Animates the weights for given list of views
     * @param duration Duration of animations
     * @param views Views to animate
     */
    private void animateViews(long duration, AnimatorWeightHolder... views){
        ValueAnimator [] animators = new ValueAnimator [views.length];

        for (int i = 0; i < animators.length; i++) {
            //Get enter View start and end weights and give to object animator
            ViewGroup.LayoutParams startLP = views[i].view.getLayoutParams();
            ViewGroup.LayoutParams endLP = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, views[i].endWeight);

            animators[i] = ValueAnimator.ofObject(new LinearLayoutParamsEvaluator(), startLP, endLP);
            animators[i].setInterpolator(new DecelerateInterpolator());

            animators[i].addUpdateListener(new JammieListAnimatorListener(views[i].view));
        }

        AnimatorSet set = new AnimatorSet();
        set.playTogether(animators);
        set.setDuration(duration);
        set.start();
    }

    public class JammieListAnimatorListener implements ValueAnimator.AnimatorUpdateListener{
        View view;

        public JammieListAnimatorListener(View view) {
            this.view = view;
        }

        @Override
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            view.setLayoutParams((LinearLayout.LayoutParams)valueAnimator.getAnimatedValue());
        }
    }
}
