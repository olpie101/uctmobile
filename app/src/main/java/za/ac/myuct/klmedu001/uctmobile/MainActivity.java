package za.ac.myuct.klmedu001.uctmobile;

import android.app.Activity;

import android.app.ActionBar;
import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;
import za.ac.myuct.klmedu001.uctmobile.constants.UCTConstants;
import za.ac.myuct.klmedu001.uctmobile.fragment.NavigationDrawerFragment;
import za.ac.myuct.klmedu001.uctmobile.fragment.NewsFragment;
import za.ac.myuct.klmedu001.uctmobile.processes.rest.JammieService;
import za.ac.myuct.klmedu001.uctmobile.processes.rest.entity.LastJammieTimeTableBracketUpdate;
import za.ac.myuct.klmedu001.uctmobile.processes.rest.adapter.LastJammieTimeTableBracketUpdateAdapter;


public class MainActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {
    private final String TAG = "MainActivity";
    private Date lastUpdate;
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("MAIN", "starting");

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = getSharedPreferences(UCTConstants.SHARED_PREFS, MODE_PRIVATE);
        lastUpdate = new Date(prefs.getLong(UCTConstants.PREFS_LAST_JAMMIE_UPDATE, 0));
        Log.d(TAG, "lastUpdate="+lastUpdate);

        new LastUpdateTask().execute();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        switch(position+1) {
            case 1:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, NewsFragment.newInstance(position + 1))
                        .commit();
        }
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_news);
                break;
            case 2:
                mTitle = getString(R.string.title_jammie_shuttle);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class LastUpdateTask extends AsyncTask<Void, Integer, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            Gson gson = new GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .registerTypeAdapter(LastJammieTimeTableBracketUpdate.class, new LastJammieTimeTableBracketUpdateAdapter())
                    .create();

            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(UCTConstants.AE_URL) // The base API endpoint.
                    .setConverter(new GsonConverter(gson))
                    .build();

            Log.d(TAG, "Querying Server");
            JammieService jammieService= restAdapter.create(JammieService.class);

            LastJammieTimeTableBracketUpdate serverLastUpdate = jammieService.lastUpdate();

            if(lastUpdate.compareTo(serverLastUpdate.getDate().getTime()) < 0){
                Log.d(TAG, "Jammie timetable needs updating\n"+lastUpdate+"\nvs.\n"+serverLastUpdate.getDate().getTime());
            }else{
                Log.d(TAG, "Jammie timetable !needs updating\n"+lastUpdate+"\nvs.\n"+serverLastUpdate.getDate().getTime());
            }
            return null;
        }
    }
}
