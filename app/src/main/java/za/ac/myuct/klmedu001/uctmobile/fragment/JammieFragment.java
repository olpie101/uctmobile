package za.ac.myuct.klmedu001.uctmobile.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import za.ac.myuct.klmedu001.uctmobile.JammieAllRoutesAdapter;
import za.ac.myuct.klmedu001.uctmobile.MainActivity;
import za.ac.myuct.klmedu001.uctmobile.NewsCardsAdapter;
import za.ac.myuct.klmedu001.uctmobile.R;
import za.ac.myuct.klmedu001.uctmobile.constants.BaseApplication;
import za.ac.myuct.klmedu001.uctmobile.constants.NewsItem;
import za.ac.myuct.klmedu001.uctmobile.constants.RSSItem;
import za.ac.myuct.klmedu001.uctmobile.constants.UCTConstants;
import za.ac.myuct.klmedu001.uctmobile.processes.rest.container.AllRoutesContainer;
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
    @InjectView(R.id.iv_jammie_map)
    ImageView mapImageView;
    @InjectView(R.id.rv_jammie_all_routes)
    RecyclerView allRoutesListView;
//    @InjectView(R.id.rv_jammie_routes)
//    RecyclerView routesListView;
//    @InjectView(R.id.rv_jammie_days)
//    RecyclerView daysListView;

    List<AllRoutesContainer> allRoutes;
    List<RouteContainer> routes;

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

        allRoutesListView.setLayoutManager(new LinearLayoutManager((getActivity())));
        allRoutesListView.setAdapter(new JammieAllRoutesAdapter(new ArrayList<AllRoutesContainer>()));

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

                allRoutesListView.setAdapter(new JammieAllRoutesAdapter(allRoutes));
                allRoutesListView.getAdapter().notifyDataSetChanged();
            }
        }
    }
}
