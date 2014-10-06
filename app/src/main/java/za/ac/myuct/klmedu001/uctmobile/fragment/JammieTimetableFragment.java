package za.ac.myuct.klmedu001.uctmobile.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.activeandroid.query.Select;

import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import za.ac.myuct.klmedu001.uctmobile.R;
import za.ac.myuct.klmedu001.uctmobile.processes.rest.container.RouteTimeContainer;

public class JammieTimetableFragment extends Fragment {
    private static final String TAG = "JammieTimetableFragment";
    // the fragment initialization parameters, e.g. ARG_ITEM_ID
    private static final String ARG_PERIOD = "period";
    private static final String ARG_CODE = "code";
    private static final String ARG_DAY = "day";

    @InjectView(R.id.tl_jammie_timetable)
    TableLayout container;
    private String period;
    private String code;
    private char day;

    List<RouteTimeContainer> routeTimes;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param period Jammie timtable period (ie. term ,vac).
     * @param code Route code.
     * @param day Operating day.
     * @return A new instance of fragment JammieTimetableFragment.
     */
    public static JammieTimetableFragment newInstance(String period, String code, char day) {
        Log.d(TAG+"newInstance", "p="+period+", c="+code+" ,d="+day);
        JammieTimetableFragment fragment = new JammieTimetableFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PERIOD, period);
        args.putString(ARG_CODE, code);
        args.putChar(ARG_DAY, day);
        fragment.setArguments(args);
        return fragment;
    }
    public JammieTimetableFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            period = getArguments().getString(ARG_PERIOD);
            code = getArguments().getString(ARG_CODE);
            day = getArguments().getChar(ARG_DAY);
        }
        routeTimes = new Select().from(RouteTimeContainer.class)
                .where("bracket = ? AND operatingDayType = ? AND routeCode = ?", period, day, code)
                .orderBy("internalId ASC")
                .execute();
        Log.d(TAG, "oncreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "oncreateview");
        View rootView = inflater.inflate(R.layout.fragment_jammie_timetable, container, false);
        ButterKnife.inject(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(container.getChildCount() == 0)
            setUpTable();
    }

    /**
     * Sets up the TableLayout used to represent jammie times with
     * the table table for the given requested information
     */
    private void setUpTable() {
        TableLayout.LayoutParams lp =
                new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT, 1);
        lp.gravity = Gravity.CENTER;
        int columns = routeTimes.size();
        int rows = routeTimes.get(0).getRouteTimes().split(",").length-1;
        HashMap<String, String []> routeMap = new HashMap<String, String[]>();
        for (RouteTimeContainer routeTime : routeTimes) {
            routeMap.put(routeTime.getRouteStop(), routeTime.getRouteTimes().split(","));
        }
        Log.d(TAG+"setUP", "col="+columns+",rows="+rows);

        TableRow headingRow = new TableRow(getActivity());
        for (RouteTimeContainer routeTime : routeTimes) {
            TextView heading = new TextView(getActivity());
            heading.setText(routeTime.getRouteStop());
            heading.setTextAppearance(getActivity(), R.style.jammieListItemStyleBold);
            heading.setGravity(Gravity.CENTER);
            heading.setPadding(20, 10, 20, 10);
//            heading.setLayoutParams(lp);
            headingRow.addView(heading);
        }
        headingRow.setBackgroundColor(getResources().getColor(R.color.primary));
        container.addView(headingRow);

        for (int row = 0; row < rows; row++) {
            TableRow tableRow = new TableRow(getActivity());
            for (RouteTimeContainer routeTime : routeTimes) {
                TextView item = new TextView(getActivity());
                item.setText(routeMap.get(routeTime.getRouteStop())[row]);
                item.setTextAppearance(getActivity(), R.style.jammieListItemStyle);
                item.setGravity(Gravity.CENTER);
//                item.setLayoutParams(lp);
                tableRow.addView(item);
            }
            if(row % 2 == 1)
                tableRow.setBackgroundColor(getResources().getColor(R.color.row_darker));
            container.addView(tableRow);
        }
    }
}
