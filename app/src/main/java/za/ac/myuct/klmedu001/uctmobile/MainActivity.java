package za.ac.myuct.klmedu001.uctmobile;


import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.squareup.otto.Subscribe;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.InjectView;
import za.ac.myuct.klmedu001.uctmobile.api.endpoints.jammieEndpoint.JammieEndpoint;
import za.ac.myuct.klmedu001.uctmobile.api.endpoints.jammieEndpoint.model.LastJammieTimeTableBracketUpdateTransformed;
import za.ac.myuct.klmedu001.uctmobile.constants.BaseApplication;
import za.ac.myuct.klmedu001.uctmobile.constants.UCTConstants;
import za.ac.myuct.klmedu001.uctmobile.constants.ottoposters.ActionBarAlphaChange;
import za.ac.myuct.klmedu001.uctmobile.constants.ottoposters.ActionBarTextColorChange;
import za.ac.myuct.klmedu001.uctmobile.constants.ottoposters.BackPressedEvent;
import za.ac.myuct.klmedu001.uctmobile.fragment.JammieFragment;
import za.ac.myuct.klmedu001.uctmobile.fragment.NavigationDrawerFragment;
import za.ac.myuct.klmedu001.uctmobile.fragment.NewsFragment;
import za.ac.myuct.klmedu001.uctmobile.processes.loaders.JammieTimeTableLoader;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {
    private int oldPosition = -1;
    private final String TAG = "MainActivity";
    public static final int JAMMIE_LOADER_ID = UCTConstants.JAMMIE_LOADER_ID;
    private final long timeout = 20;                //request timeout limit
    private final TimeUnit unit = TimeUnit.SECONDS; //time unit for requests
    private NewsFragment newsFragment;
    private Drawable actionbarDrawable;
    @InjectView(R.id.main_toolbar)
    Toolbar actionBar;

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
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        ButterKnife.inject(this);

        actionbarDrawable = getResources().getDrawable(R.color.primary);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT_WATCH) {
            Log.d(TAG, "SBD");
            actionBar.setBackgroundDrawable(actionbarDrawable);
        } else {
            Log.d(TAG, "SB");
            actionBar.setBackground(actionbarDrawable);
        }

        setSupportActionBar(actionBar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        BaseApplication.getEventBus().register(this);
        new LastUpdateTask().execute();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        //don't change fragment if same position
        if(position == oldPosition)
            return;
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();

        Log.d(TAG, "frag count=" + fragmentManager.getFragments().size());
        switch(position) {
            case 0:
                if(newsFragment == null)
                    newsFragment = NewsFragment.newInstance(position);

                fragmentManager.beginTransaction()
                        .replace(R.id.container, newsFragment)
                        .commit();
                break;
            case 1:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, JammieFragment.newInstance(position))
                        .commit();
                break;
        }
        oldPosition = position;
        Log.d(TAG, "fragment replaced");
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 0:
                mTitle = getString(R.string.title_news);
                setTitle(mTitle);
                break;
            case 1:
                mTitle = getString(R.string.title_jammie_shuttle);
                break;
            case 2:
                mTitle = getString(R.string.title_section3);
                break;
        }
        setTitle(mTitle);
    }

    public void restoreActionBar() {
        setSupportActionBar(actionBar);
        setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            //restoreActionBar();
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

    @Override
    public void onBackPressed() {
        BackPressedEvent back = new BackPressedEvent();
        BaseApplication.getEventBus().post(back);

        if(!back.intercepted)
            super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        BaseApplication.getEventBus().unregister(this);
    }

    @Subscribe
    public void setActionBarTextColor(ActionBarTextColorChange evt){
//        actionBar.setTitleTextColor(evt.color);
        Toast.makeText(this, "ABTCC"+evt.color, Toast.LENGTH_SHORT).show();
        actionBar.setTitleTextColor(getResources().getColor(R.color.com));
        actionBar.setSubtitleTextColor(getResources().getColor(R.color.hsc));
        setSupportActionBar(actionBar);
    }

    @Subscribe
    public void onAlphaSet(ActionBarAlphaChange evt){
        actionbarDrawable.setAlpha(evt.alphaVal);
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
            Log.d(TAG, "#lastUpdate="+ lastUpdate);

            Log.d(TAG, "Querying Server");
            JammieEndpoint jammieEndpointService = UCTConstants.jammieEndpointBuilder.build();

            LastJammieTimeTableBracketUpdateTransformed serverLastUpdate;

            try {
                serverLastUpdate = jammieEndpointService.getLastUpdate().execute();
                Log.d(TAG, "###########"+lastUpdate+" vs "+serverLastUpdate);
                if(lastUpdate < serverLastUpdate.getDate()){
                    Log.d(TAG, "Jammie timetable needs updating\n");
                    getSupportLoaderManager().initLoader(JAMMIE_LOADER_ID, null, this);
                }else{
                    Log.d(TAG, "Jammie timetable !needs updating\n");
                }
            } catch (IOException e) {
                Log.d(TAG, "Error getting last update time");
                Log.e(TAG, e.toString());
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
