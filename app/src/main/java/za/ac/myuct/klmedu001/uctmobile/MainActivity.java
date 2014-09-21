package za.ac.myuct.klmedu001.uctmobile;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.LoaderManager;
import android.content.Loader;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.squareup.okhttp.OkHttpClient;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;
import za.ac.myuct.klmedu001.uctmobile.api.endpoints.jammieEndpoint.JammieEndpoint;
import za.ac.myuct.klmedu001.uctmobile.api.endpoints.jammieEndpoint.model.LastJammieTimeTableBracketUpdateTransformed;
import za.ac.myuct.klmedu001.uctmobile.constants.UCTConstants;
import za.ac.myuct.klmedu001.uctmobile.fragment.JammieFragment;
import za.ac.myuct.klmedu001.uctmobile.fragment.NavigationDrawerFragment;
import za.ac.myuct.klmedu001.uctmobile.fragment.NewsFragment;
import za.ac.myuct.klmedu001.uctmobile.processes.loaders.JammieTimeTableLoader;


public class MainActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {
    private final String TAG = "MainActivity";
    public static final int JAMMIE_LOADER_ID = UCTConstants.JAMMIE_LOADER_ID;
    private final long timeout = 20;                //request timeout limit
    private final TimeUnit unit = TimeUnit.SECONDS; //time unit for requests

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
        new LastUpdateTask().execute();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        switch(position) {
            case 0:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, NewsFragment.newInstance(position))
                        .commit();
                break;
            case 1:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, JammieFragment.newInstance(position))
                        .commit();
                break;
        }
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 0:
                mTitle = getString(R.string.title_news);
                break;
            case 1:
                mTitle = getString(R.string.title_jammie_shuttle);
                break;
            case 2:
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

    /**
     * Check if the Jammie timetable has been updated since last sync
     * if so update current database
     */
    private class LastUpdateTask extends AsyncTask<Void, Integer, Void> implements LoaderManager.LoaderCallbacks<Boolean> {

        @Override
        protected Void doInBackground(Void... voids) {
            SharedPreferences prefs = getSharedPreferences(UCTConstants.SHARED_PREFS, MODE_PRIVATE);
            long lastUpdate = prefs.getLong(UCTConstants.PREFS_LAST_JAMMIE_UPDATE, 0);
            Log.d(TAG, "lastUpdate="+ lastUpdate);

            Log.d(TAG, "Querying Server");
            JammieEndpoint jammieEndpointService = UCTConstants.jammieEndpointBuilder.build();

            LastJammieTimeTableBracketUpdateTransformed serverLastUpdate;

            try {
                serverLastUpdate = jammieEndpointService.getLastUpdate().execute();
                if(lastUpdate < serverLastUpdate.getDate()){
                    Log.d(TAG, "Jammie timetable needs updating\n");
                    getLoaderManager().initLoader(JAMMIE_LOADER_ID, null, this);
                }else{
                    Log.d(TAG, "Jammie timetable !needs updating\n");
                }
            } catch (IOException e) {
                Log.d(TAG, "Error getting last update time");
            }

            return null;
        }


        @Override
        public Loader<Boolean> onCreateLoader(int i, Bundle bundle) {
            JammieTimeTableLoader jttl = new JammieTimeTableLoader(getApplicationContext());
            jttl.loadInBackground();
            return jttl;
        }

        @Override
        public void onLoadFinished(Loader<Boolean> booleanLoader, Boolean success) {
            if(success){
                Log.d(TAG, "success");
            }else{
                Log.d(TAG, "failure");
            }
        }

        @Override
        public void onLoaderReset(Loader<Boolean> booleanLoader) {

        }
    }
}
