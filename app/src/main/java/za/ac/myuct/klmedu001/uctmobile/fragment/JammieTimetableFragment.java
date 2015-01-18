package za.ac.myuct.klmedu001.uctmobile.fragment;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.grosner.dbflow.sql.builder.Condition;
import com.grosner.dbflow.sql.language.Select;

import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import za.ac.myuct.klmedu001.uctmobile.R;
import za.ac.myuct.klmedu001.uctmobile.processes.rest.container.RouteTimeContainer;
import za.ac.myuct.klmedu001.uctmobile.processes.rest.container.RouteTimeContainer$Table;

import static za.ac.myuct.klmedu001.uctmobile.constants.UCTConstants.convertDay;

public class JammieTimetableFragment extends Fragment {
    private static final String TAG = "JammieTimetableFragment";
    // the fragment initialization parameters, e.g. ARG_ITEM_ID
    private static final String ARG_TITLE = "title";
    private static final String ARG_PERIOD = "period";
    private static final String ARG_CODE = "code";
    private static final String ARG_DAY = "day";

    @InjectView(R.id.tl_jammie_timetable_header)
    TableLayout headers;
//    @InjectView(R.id.tr_jammie_timetable_header_dummy)
//    TableRow dummyHeaders;
    @InjectView(R.id.tl_jammie_timetable)
    TableLayout container;
    private String title;
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
    public static JammieTimetableFragment newInstance(String title, String period, String code, char day) {
        Log.d(TAG+"newInstance", "p="+period+", c="+code+" ,d="+day);
        JammieTimetableFragment fragment = new JammieTimetableFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
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
            title = getArguments().getString(ARG_TITLE);
            period = getArguments().getString(ARG_PERIOD);
            code = getArguments().getString(ARG_CODE);
            day = getArguments().getChar(ARG_DAY);
        }
        routeTimes = new Select().from(RouteTimeContainer.class)
                .where(Condition.column(RouteTimeContainer$Table.BRACKET).is(period))
                .and(Condition.column(RouteTimeContainer$Table.OPERATINGDAYTYPE).is(day))
                .and(Condition.column(RouteTimeContainer$Table.ROUTECODE).is(code))
                .orderBy(true, RouteTimeContainer$Table.INTERNALID)
                .queryList();
//        Log.d(TAG, "oncreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        Log.d(TAG, "oncreateview");
        View rootView = inflater.inflate(R.layout.fragment_jammie_timetable, container, false);
        ButterKnife.inject(this, rootView);
        getActivity().setTitle(title+" - "+convertDay(day));
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
        ViewGroup.LayoutParams lp =
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lp.height = 0;
        int columns = routeTimes.size();
        int rows = routeTimes.get(0).getRouteTimes().split(",").length;
        HashMap<String, String []> routeMap = new HashMap<String, String[]>();
        for (RouteTimeContainer routeTime : routeTimes) {
            routeMap.put(routeTime.getRouteStop(), routeTime.getRouteTimes().split(","));
        }
        Log.d(TAG+"setUP", "col="+columns+",rows="+rows);

        TableRow headingRow = new TableRow(getActivity());
        TableRow headingRowDummy = new TableRow(getActivity());
        for (RouteTimeContainer routeTime : routeTimes) {
            TextView heading = new TextView(getActivity());
            heading.setText(routeTime.getRouteStop());
            heading.setTextAppearance(getActivity(), R.style.jammieListItemStyleBold);
            heading.setGravity(Gravity.CENTER);
            heading.setPadding(20, 10, 20, 10);
            headingRow.addView(heading);

            TextView headingDummy = new TextView(getActivity());
            headingDummy.setText(routeTime.getRouteStop());
//            ViewGroup.LayoutParams p = new  ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 1);
            headingDummy.setTextAppearance(getActivity(), R.style.invisibleText);
            headingDummy.setGravity(Gravity.CENTER);
            headingDummy.setPadding(20, 10, 20, 10);
//            headingDummy.setLayoutParams(p);

            headingRowDummy.addView(headingDummy);
//            params.height = 1;
        }
        //animate background from transparent to final colour
        ValueAnimator colorAnim = ObjectAnimator
                .ofInt(headingRow, "backgroundColor", getResources().getColor(R.color.primary_transparent),
                        getResources().getColor(R.color.primary));
        colorAnim.setEvaluator(new ArgbEvaluator());
        colorAnim.setDuration(300);
        colorAnim.setStartDelay(200);
        colorAnim.start();

        headers.addView(headingRow);
        //headingRowDummy.setLayoutParams(lp);
        container.addView(headingRowDummy);

        for (int row = 0; row < rows; row++) {
            TableRow tableRow = new TableRow(getActivity());
            int count = 0;
            for (RouteTimeContainer routeTime : routeTimes) {
                TextView item = new TextView(getActivity());
                try {
                    item.setText(routeMap.get(routeTime.getRouteStop())[row]);
                }catch(ArrayIndexOutOfBoundsException e){
                    item.setText("");
                }
                item.setTextAppearance(getActivity(), R.style.jammieListItemStyle);
                item.setGravity(Gravity.CENTER);
                item.setPadding(20, 10, 20, 10);
                item.setLayoutParams(headingRow.getChildAt(count).getLayoutParams());
//                item.setLayoutParams(lp);
                tableRow.addView(item);
                ++count;
            }
            if(row % 2 == 1)
                tableRow.setBackgroundColor(getResources().getColor(R.color.row_darker));
            container.addView(tableRow);

        }
    }
}
