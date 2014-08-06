package za.ac.myuct.klmedu001.uctmobile;

import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;

import za.ac.myuct.klmedu001.uctmobile.constantsandprocesses.BaseApplication;
import za.ac.myuct.klmedu001.uctmobile.constantsandprocesses.NewsFrontPageLoader;
import za.ac.myuct.klmedu001.uctmobile.constantsandprocesses.NewsItem;
import za.ac.myuct.klmedu001.uctmobile.constantsandprocesses.NewsRSSLoader;
import za.ac.myuct.klmedu001.uctmobile.constantsandprocesses.RSSItem;
import za.ac.myuct.klmedu001.uctmobile.constantsandprocesses.UCTConstants;
import za.ac.myuct.klmedu001.uctmobile.constantsandprocesses.ottoposters.NewsCardClickedEvent;

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
    private static final int NEWS_LOADER_ID = 1;
    private static final int RSS_LOADER_ID = 2;

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
    LoaderManager lm;       //Used for background loading
    private boolean newsLoading;    //track if loading for news is finished
    private boolean rssLoading;     //track if loading for rss feed is fnished



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
    private LoaderCallbacks<ArrayList<NewsItem>> newsItemLoaderCallbacks = new LoaderCallbacks<ArrayList<NewsItem>>() {
        @Override
        public Loader<ArrayList<NewsItem>> onCreateLoader(int i, Bundle bundle) {
            return new NewsFrontPageLoader(getActivity().getApplication().getBaseContext());
        }

        @Override
        public void onLoadFinished(Loader<ArrayList<NewsItem>> arrayListLoader, ArrayList<NewsItem> newsItems) {
            Log.d(TAG, "finished loading");
            ((NewsCardsAdapter)newsCardsView.getAdapter()).setItems(newsItems);
            lm.destroyLoader(NEWS_LOADER_ID);
            newsLoading = false;
            updateSwipeToRefresh();
        }

        @Override
        public void onLoaderReset(Loader<ArrayList<NewsItem>> arrayListLoader) {

        }
    };

    //Callbacks for rss loader
    private LoaderCallbacks<HashMap<String, RSSItem>> rssItemLoaderCallbacks = new LoaderCallbacks<HashMap<String, RSSItem>>() {
        @Override
        public Loader<HashMap<String, RSSItem>> onCreateLoader(int i, Bundle bundle) {
            return new NewsRSSLoader(getActivity().getApplication().getBaseContext());
        }

        @Override
        public void onLoadFinished(Loader<HashMap<String, RSSItem>> hashMapLoader, HashMap<String, RSSItem> rssItems) {
            Log.d(TAG, "Loaded RSS Feed");
            if(rssItems != null)
                rssFeed = rssItems;

            lm.destroyLoader(RSS_LOADER_ID);    //destroy rss loader
            rssLoading = false;
            updateSwipeToRefresh();
        }

        @Override
        public void onLoaderReset(Loader<HashMap<String, RSSItem>> arrayListLoader) {

        }
    };


    @Subscribe
    public void onNewsCardClickedEvent(NewsCardClickedEvent card){
        Toast.makeText(getActivity(), "card ' "+card.title +"' clicked", Toast.LENGTH_SHORT).show();
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
        if(lm.getLoader(NEWS_LOADER_ID) == null) {
            lm.initLoader(NEWS_LOADER_ID, null, newsItemLoaderCallbacks);
            startedLoading = true;
            newsLoading = true;
        }

        if(lm.getLoader(RSS_LOADER_ID) == null){
            lm.initLoader(RSS_LOADER_ID, null, rssItemLoaderCallbacks);
            startedLoading = true;
            rssLoading = true;
        }

        if(!startedLoading)
            newsContainer.setRefreshing(false);

    }

    //called in both onLoadFinished callbacks
    //both loading booleans need to be false for view to
    //stop displaying the loading icon
    private void updateSwipeToRefresh(){
        if(!newsLoading && !rssLoading)
            newsContainer.setRefreshing(false);
    }
}
