package za.ac.myuct.klmedu001.uctmobile.processes.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import za.ac.myuct.klmedu001.uctmobile.constants.NewsItem;
import za.ac.myuct.klmedu001.uctmobile.constants.UCTConstants;

/**
 * Created by eduardokolomajr on 2014/07/26.
 *
 * loader and loader manager help from
 * http://www.androiddesignpatterns.com/2012/08/implementing-loaders.html
 * and its associated tutorial series
 */
public class NewsFrontPageLoader extends AsyncTaskLoader<ArrayList<NewsItem>> {
    private final long timeout = 20;                //request timeout limit
    private final TimeUnit unit = TimeUnit.SECONDS; //time unit for requests
    private final String TAG = "NewsLoader";
    // We hold a reference to the Loader’s data here.
    private ArrayList<NewsItem> newsItems;

    public NewsFrontPageLoader(Context ctx) {
        // Loaders may be used across multiple Activity's (assuming they aren't
        // bound to the LoaderManager), so NEVER hold a reference to the context
        // directly. Doing so will cause you to leak an entire Activity's context.
        // The superclass constructor will store a reference to the Application
        // Context instead, and can be retrieved with a call to getContext().
        super(ctx);
        Log.d(TAG, "created");
    }

    /****************************************************/
    /** (1) A task that performs the asynchronous load **/
    /**
     * ************************************************
     */

    @Override
    public ArrayList<NewsItem> loadInBackground() {
        Log.d(TAG, "BG");


        // This method is called on a background thread and should generate a
        // new set of data to be delivered back to the client.
        ArrayList<NewsItem> data = new ArrayList<NewsItem>();

        // TODO: Perform the query here and add the results to 'data'.
        try{
            //Use OkHttp to get the DOM of the uct homepage
            OkHttpClient client = new OkHttpClient();
            client.setConnectTimeout(timeout, unit);

            Log.d(TAG, "about to request");
            Request request = new Request.Builder()
                    .url(UCTConstants.UCT_URL)
                    .build();

            Log.d(TAG, "about to get response");
            Response response = client.newCall(request).execute();
            Log.d(TAG, "about to get response2");
//            Log.d(TAG, "body = "+body);

            //Success, use JSoup to parse data
            if(response.isSuccessful()) {
                Document doc = Jsoup.parse(response.body().string());

                //Find featured stories
                Element featuredStoriesSlider = doc.select(UCTConstants.HOMEPAGE_FEATURED_STORIES_CONTAINER).first();
                //Log.d(TAG, "Fstories size = " + featuredStoriesSlider.toString());
                Elements featuredStories = featuredStoriesSlider.select(UCTConstants.HOMEPAGE_FEATURED_STORIES_ITEM);
                Log.d(TAG, "Fss = " + featuredStories.size());
                //            Log.d(TAG, doc.toString());
                //            Elements featuredStories = doc.getElementsByClass(UCTConstants.HOMEPAGE_FEATURED_STORIES_CONTAINER);

                //process each featured story
                for (Element story : featuredStories) {
//                    Log.d(TAG, "title search" + story.toString());
                    String title = story.getElementsByTag(UCTConstants.HOMEPAGE_TITLE).first().text();                      //Get title
                    String link = story.select(UCTConstants.HOMEPAGE_LINK).last().attr(UCTConstants.HOMEPAGE_LINK_HREF);    //Get story link
                    String imageLink = UCTConstants.UCT_URL + story.select(UCTConstants.HOMEPAGE_IMAGE).first().attr(UCTConstants.HOMEPAGE_IMAGE_SRC); //Get story image link

                    data.add(new NewsItem(title, link, imageLink));
                }

                //Find stories in main holder
                Element storiesContainer = doc.select(UCTConstants.HOMEPAGE_STORIES_CONTAINER).first();
                Elements stories = storiesContainer.select(UCTConstants.HOMEPAGE_STORIES_ITEM);
                Log.d(TAG, "stories size = "+stories.size());

                for(Element story : stories){
                    String title = story.getElementsByTag(UCTConstants.HOMEPAGE_TITLE).first().text();                      //Get title
                    String link = story.select(UCTConstants.HOMEPAGE_LINK).last().attr(UCTConstants.HOMEPAGE_LINK_HREF);    //Get story link
                    String imageLink = UCTConstants.UCT_URL + story.select(UCTConstants.HOMEPAGE_IMAGE).first().attr(UCTConstants.HOMEPAGE_IMAGE_SRC); //Get story image link

                    data.add(new NewsItem(title, link, imageLink));
                }

            }else{
                Log.d(TAG, "No Response");
            }
        }catch(IOException e){
            Log.d(TAG, "IOException");
            stopLoading();

        }

        Log.d(TAG, "BG end");

        return data;
    }

    /********************************************************/
    /** (2) Deliver the results to the registered listener **/
    /**
     * ****************************************************
     */

    @Override
    public void deliverResult(ArrayList<NewsItem> data) {
        if (isReset()) {
            // The Loader has been reset; ignore the result and invalidate the data.
            releaseResources(data);
            return;
        }
//        Log.d(TAG, "dr not reset");

        // Hold a reference to the old data so it doesn't get garbage collected.
        // We must protect it until the new data has been delivered.
        ArrayList<NewsItem> oldData = newsItems;
        newsItems = data;

        if (isStarted()) {
            // If the Loader is in a started state, deliver the results to the
            // client. The superclass method does this for us.
            super.deliverResult(data);
        }
//        Log.d(TAG, "dr is started");

        // Invalidate the old data as we don't need it any more.
        if (oldData != null && oldData != data) {
            releaseResources(oldData);
        }

//        Log.d(TAG, "dr invalidated");
    }

    /*********************************************************/
    /** (3) Implement the Loader’s state-dependent behavior **/
    /**
     * *****************************************************
     */

    @Override
    protected void onStartLoading() {
        if (newsItems != null) {
            // Deliver any previously loaded data immediately.
            deliverResult(newsItems);
        }

        // Begin monitoring the underlying data source.
//        if (mObserver == null) {
//            mObserver = new SampleObserver();
//            // TODO: register the observer
//        }

        if (takeContentChanged() || newsItems == null) {
            // When the observer detects a change, it should call onContentChanged()
            // on the Loader, which will cause the next call to takeContentChanged()
            // to return true. If this is ever the case (or if the current data is
            // null), we force a new load.
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        // The Loader is in a stopped state, so we should attempt to cancel the 
        // current load (if there is one).
        cancelLoad();

        // Note that we leave the observer as is. Loaders in a stopped state
        // should still monitor the data source for changes so that the Loader
        // will know to force a new load if it is ever started again.
    }

    @Override
    protected void onReset() {
        // Ensure the loader has been stopped.
        onStopLoading();

        // At this point we can release the resources associated with 'newsItems'.
        if (newsItems != null) {
            releaseResources(newsItems);
            newsItems = null;
        }

        // The Loader is being reset, so we should stop monitoring for changes.
//        if (mObserver != null) {
//            // TODO: unregister the observer
//            mObserver = null;
//        }
    }

    @Override
    public void onCanceled(ArrayList<NewsItem> data) {
        // Attempt to cancel the current asynchronous load.
        super.onCanceled(data);

        // The load has been canceled, so we should release the resources
        // associated with 'data'.
        releaseResources(data);
    }

    private void releaseResources(ArrayList<NewsItem> data) {
        // For a simple List, there is nothing to do. For something like a Cursor, we 
        // would close it in this method. All resources associated with the Loader
        // should be released here.
    }

    /*********************************************************************/
    /** (4) Observer which receives notifications when the data changes **/
    /*********************************************************************/

    // NOTE: Implementing an observer is outside the scope of this post (this example
    // uses a made-up "SampleObserver" to illustrate when/where the observer should 
    // be initialized). 

    // The observer could be anything so long as it is able to detect content changes
    // and report them to the loader with a call to onContentChanged(). For example,
    // if you were writing a Loader which loads a list of all installed applications
    // on the device, the observer could be a BroadcastReceiver that listens for the
    // ACTION_PACKAGE_ADDED intent, and calls onContentChanged() on the particular 
    // Loader whenever the receiver detects that a new application has been installed.
    // Please don’t hesitate to leave a comment if you still find this confusing! :)
//    private SampleObserver mObserver;
}

