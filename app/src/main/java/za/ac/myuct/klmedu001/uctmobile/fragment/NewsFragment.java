package za.ac.myuct.klmedu001.uctmobile.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import za.ac.myuct.klmedu001.uctmobile.MainActivity;
import za.ac.myuct.klmedu001.uctmobile.NewsArticleActivity;
import za.ac.myuct.klmedu001.uctmobile.NewsCardsAdapter;
import za.ac.myuct.klmedu001.uctmobile.R;
import za.ac.myuct.klmedu001.uctmobile.constants.BaseApplication;
import za.ac.myuct.klmedu001.uctmobile.processes.loaders.NewsFrontPageLoader;
import za.ac.myuct.klmedu001.uctmobile.constants.NewsItem;
import za.ac.myuct.klmedu001.uctmobile.processes.loaders.NewsRSSLoader;
import za.ac.myuct.klmedu001.uctmobile.constants.RSSItem;
import za.ac.myuct.klmedu001.uctmobile.constants.UCTConstants;
import za.ac.myuct.klmedu001.uctmobile.constants.ottoposters.NewsCardClickedEvent;

/**
 * Created by eduardokolomajr on 2014/07/25.
 * help for use of SwipeRefreshLayout and its listener from
 * http://antonioleiva.com/swiperefreshlayout/
 *
 * loader and loader manager help from
 * http://www.androiddesignpatterns.com/2012/08/implementing-loaders.html
 * and its associated tutorial series
 */
public class NewsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private final String TAG = "NewsFragment";

    // The loader's unique id. Loader ids are specific to the Activity or
    // Fragment in which they reside.
    private static final int NEWS_LOADER_ID = UCTConstants.NEWS_LOADER_ID;
    private static final int RSS_LOADER_ID = UCTConstants.RSS_LOADER_ID;

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    @InjectView(R.id.rv_news)
    RecyclerView newsCardsView;
    @InjectView(R.id.swipe_container_news)
    SwipeRefreshLayout newsContainer;
    HashMap<String, RSSItem> rssFeed = new HashMap<String, RSSItem>();
    List<NewsItem> newsFeed = new ArrayList<NewsItem>();
    android.support.v4.app.LoaderManager lm;       //Used for background loading
    private boolean newsLoading;    //track if loading for news is finished
    private boolean rssLoading;     //track if loading for rss feed is finished



    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static NewsFragment newInstance(int sectionNumber) {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public NewsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup view,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_news, view, false);

        ButterKnife.inject(this, rootView);

        newsCardsView.setHasFixedSize(true);
        String [] titles = new String [5];
        for (int i = 0; i < titles.length; i++) {
            titles[i] = "News Card View"+(i+1);
        }
        newsCardsView.setAdapter(new NewsCardsAdapter(new ArrayList<NewsItem>(), getActivity()));
        newsCardsView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        newsCardsView.setItemAnimator(new DefaultItemAnimator());

        newsContainer.setOnRefreshListener(this);
        newsContainer.setColorSchemeResources(R.color.white, R.color.primary_dark, R.color.black, R.color.primary);

        if(savedInstanceState == null){
            //Load entries from database for both the news feed and rss feed
            newsFeed = new Select().all().from(NewsItem.class).execute();
            Collections.reverse(newsFeed);  //list is in reverse in the database for storage purposes
            List<RSSItem> rssFeedList = new Select().all().from(RSSItem.class).execute();

            for(RSSItem item : rssFeedList){
                rssFeed.put(item.title, item);
            }

            ((NewsCardsAdapter) newsCardsView.getAdapter()).setItems((ArrayList<NewsItem>) newsFeed);
        }

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
        lm = getLoaderManager();
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

    //Callbacks for news loader
    private LoaderManager.LoaderCallbacks<ArrayList<NewsItem>> newsItemLoaderCallbacks = new LoaderManager.LoaderCallbacks<ArrayList<NewsItem>>() {
        @Override
        public android.support.v4.content.Loader<ArrayList<NewsItem>> onCreateLoader(int i, Bundle bundle) {
            return new NewsFrontPageLoader(getActivity().getApplication().getBaseContext());
        }

        @Override
        public void onLoaderReset(android.support.v4.content.Loader<ArrayList<NewsItem>> arrayListLoader) {

        }

        @Override
        public void onLoadFinished(android.support.v4.content.Loader<ArrayList<NewsItem>> arrayListLoader, ArrayList<NewsItem> newsItems) {
            Log.d(TAG, "finished loading");
            //Done loading news feed entries
            //Add any new entries into the database
            if(newsFeed != null && newsFeed.size() == 0) {      //nothing was in database no need to check for duplicates
                Toast.makeText(getActivity(), "news items was empty", Toast.LENGTH_SHORT).show();
                //noinspection unchecked
                newsFeed = (ArrayList<NewsItem>) newsItems.clone();
                ((NewsCardsAdapter) newsCardsView.getAdapter()).setItems((ArrayList<NewsItem>) newsFeed);
                saveToNewsTable(newsItems);
            }else{
                //used for bulk insertion, ensure that items are inserted into main in correct order
                ArrayList<NewsItem> tempList = new ArrayList<NewsItem>();
                //check for duplicates O(n) = n
                //very few items in this list so it is fine
                for(NewsItem item : newsItems){
                    boolean exists = false;
                    for(NewsItem otherItem : newsFeed){
                        if(item.getLink().equals(otherItem.getLink())){
                            exists = true;
                            break;
                        }
                    }
                    if(!exists){
                        //news item doesn't exist in data base add to database
                        tempList.add(item);
                    }
                }

                if(tempList.size() > 0){
                    Log.d(TAG+12, "about to append");
                    newsFeed.addAll(0, tempList);
                    ((NewsCardsAdapter) newsCardsView.getAdapter()).setItems((ArrayList<NewsItem>) newsFeed);
                    saveToNewsTable(tempList);
                }
            }
            lm.destroyLoader(NEWS_LOADER_ID);
            newsLoading = false;
            updateSwipeToRefresh();
        }
    };

    //Callbacks for rss loader
    private LoaderManager.LoaderCallbacks<HashMap<String, RSSItem>> rssItemLoaderCallbacks = new LoaderManager.LoaderCallbacks<HashMap<String, RSSItem>>() {
        @Override
        public android.support.v4.content.Loader<HashMap<String, RSSItem>> onCreateLoader(int i, Bundle bundle) {
            return new NewsRSSLoader(getActivity().getApplication().getBaseContext());
        }

        @Override
        public void onLoadFinished(android.support.v4.content.Loader<HashMap<String, RSSItem>> hashMapLoader, HashMap<String, RSSItem> rssItems) {
            Log.d(TAG, "Loaded RSS Feed");
            //Done loading news feed entries
            //Add any new entries into the database
            if(rssItems != null)
            {
                //noinspection unchecked
                HashMap<String, RSSItem> tempItems = (HashMap<String, RSSItem>) rssItems.clone();

                String [] keys = tempItems.keySet().toArray(new String[5]);
                for(String key : keys){
                    if(rssFeed.containsKey(key)){
                        tempItems.remove(key);
                    }
                }
                rssFeed.putAll(tempItems);
                if(tempItems.size() > 0)
                    saveToRssTable(tempItems);
            }

            lm.destroyLoader(RSS_LOADER_ID);    //destroy rss loader
            rssLoading = false;
            updateSwipeToRefresh();
        }

        @Override
        public void onLoaderReset(android.support.v4.content.Loader<HashMap<String, RSSItem>> arrayListLoader) {

        }
    };


    @Subscribe
    public void onNewsCardClickedEvent(NewsCardClickedEvent card){
        Toast.makeText(getActivity(), "card ' "+card.title +"' clicked, size = "+rssFeed.values().size(), Toast.LENGTH_SHORT).show();
        if(rssFeed.containsKey(card.title)){
            Toast.makeText(this.getActivity(), "key in map", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), NewsArticleActivity.class);
            intent.putExtra(UCTConstants.BUNDLE_EXTRA_RSS_ITEM, rssFeed.get(card.title));
            startActivity(intent);
        }
    }

    //required for the swipe to refresh pattern
    @Override
    public void onRefresh() {
        // Initialize the Loader with id '1' and callbacks 'mCallbacks'.
        // If the loader doesn't already exist, one is created. Otherwise,
        // the already created Loader is reused. In either case, the
        // LoaderManager will manage the Loader across the Activity/Fragment
        // lifecycle, will receive any new loads once they have completed,
        // and will report this new data back to the 'mCallbacks' object.

        boolean startedLoading = false;
        Bundle bundle = new Bundle();
        if(lm.getLoader(NEWS_LOADER_ID) == null) {
            lm.initLoader(NEWS_LOADER_ID, bundle, newsItemLoaderCallbacks);
            startedLoading = true;
            newsLoading = true;
//            Toast.makeText(getActivity(), "ref news", Toast.LENGTH_SHORT).show();
        }

        if(lm.getLoader(RSS_LOADER_ID) == null){
            lm.initLoader(RSS_LOADER_ID, null, rssItemLoaderCallbacks);
            startedLoading = true;
            rssLoading = true;
//            Toast.makeText(getActivity(), "ref rss", Toast.LENGTH_SHORT).show();
        }

        if(!startedLoading) {
            newsContainer.setRefreshing(false);
            Toast.makeText(getActivity(), "ref none", Toast.LENGTH_SHORT).show();
        }

    }

    //called in both onLoadFinished callbacks
    //both loading booleans need to be false for view to
    //stop displaying the loading icon
    private void updateSwipeToRefresh(){
        if(!newsLoading && !rssLoading)
            newsContainer.setRefreshing(false);
    }

    private boolean saveToNewsTable (ArrayList<NewsItem> list){
        boolean success = false;
        //noinspection unchecked
        ArrayList<NewsItem> tempList = (ArrayList<NewsItem>) list.clone();
        Collections.reverse(tempList);  //reverse list so works fine with the database
        ActiveAndroid.beginTransaction();
        try {
            for(NewsItem item : tempList){
                item.save();
            }
            ActiveAndroid.setTransactionSuccessful();
            Log.d(TAG+12, "saved to news table");
            success = true;
        }catch (Exception e){
            Log.d(TAG+12, "error saving to news table");
        }
        finally {
            ActiveAndroid.endTransaction();
            Log.d(TAG+12, "end save to news table");
        }
        return success;
    }

    private boolean saveToRssTable(HashMap<String, RSSItem> rssMap){
        boolean success = false;
        Collection<RSSItem> rssList = rssMap.values();
        ActiveAndroid.beginTransaction();
        try {
            for(RSSItem item : rssList){
                item.save();
            }
            ActiveAndroid.setTransactionSuccessful();
            Log.d(TAG+12, "saved to rss table");
            success = true;
        }catch (Exception e){
            Log.d(TAG+12, "error saving to rss table");
        }
        finally {
            ActiveAndroid.endTransaction();
            Log.d(TAG+12, "end save to rss table");
        }
        return success;
    }
}
