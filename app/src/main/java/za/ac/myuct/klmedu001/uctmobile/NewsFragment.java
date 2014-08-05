package za.ac.myuct.klmedu001.uctmobile;

import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;
import android.os.Bundle;
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

import butterknife.ButterKnife;
import butterknife.InjectView;

import za.ac.myuct.klmedu001.uctmobile.constantsandprocesses.BaseApplication;
import za.ac.myuct.klmedu001.uctmobile.constantsandprocesses.NewsFrontPageLoader;
import za.ac.myuct.klmedu001.uctmobile.constantsandprocesses.NewsItem;
import za.ac.myuct.klmedu001.uctmobile.constantsandprocesses.NewsRSSLoader;
import za.ac.myuct.klmedu001.uctmobile.constantsandprocesses.RSSItem;
import za.ac.myuct.klmedu001.uctmobile.constantsandprocesses.ottoposters.NewsCardClickedEvent;

/**
 * Created by eduardokolomajr on 2014/07/25.
 */
public class NewsFragment extends Fragment{
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

        // Initialize the Loader with id '1' and callbacks 'mCallbacks'.
        // If the loader doesn't already exist, one is created. Otherwise,
        // the already created Loader is reused. In either case, the
        // LoaderManager will manage the Loader across the Activity/Fragment
        // lifecycle, will receive any new loads once they have completed,
        // and will report this new data back to the 'mCallbacks' object.
        LoaderManager lm = getLoaderManager();
        lm.initLoader(NEWS_LOADER_ID, null, newsItemLoaderCallbacks);
        lm.initLoader(RSS_LOADER_ID, null, rssItemLoaderCallbacks);

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
        }

        @Override
        public void onLoaderReset(Loader<ArrayList<NewsItem>> arrayListLoader) {

        }
    };

    //Callbacks for rss loader
    private LoaderCallbacks<ArrayList<RSSItem>> rssItemLoaderCallbacks = new LoaderCallbacks<ArrayList<RSSItem>>() {
        @Override
        public Loader<ArrayList<RSSItem>> onCreateLoader(int i, Bundle bundle) {
            return new NewsRSSLoader(getActivity().getApplication().getBaseContext());
        }

        @Override
        public void onLoadFinished(Loader<ArrayList<RSSItem>> arrayListLoader, ArrayList<RSSItem> rssItems) {
            Log.d(TAG, "Loaded RSS Feed");
        }

        @Override
        public void onLoaderReset(Loader<ArrayList<RSSItem>> arrayListLoader) {

        }
    };


    @Subscribe
    public void onNewsCardClickedEvent(NewsCardClickedEvent card){
        Toast.makeText(this.getActivity(), "card at position "+card.position+" clicked", Toast.LENGTH_LONG).show();
    }
}
