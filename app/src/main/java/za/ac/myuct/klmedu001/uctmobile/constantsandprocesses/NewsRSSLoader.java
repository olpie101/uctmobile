package za.ac.myuct.klmedu001.uctmobile.constantsandprocesses;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;
import android.util.Xml;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * Created by eduardokolomajr on 2014/07/29.
 * All code for rss feed xml parsing is from: http://developer.android.com/training/basics/network-ops/xml.html
 */
public class NewsRSSLoader extends AsyncTaskLoader<ArrayList<RSSItem>> {
    private final long timeout = 20;                //request timeout limit
    private final TimeUnit unit = TimeUnit.SECONDS; //time unit for requests
    private final String TAG = "RSSLoader";
    // We hold a reference to the Loader’s data here.
    private ArrayList<RSSItem> newsItems;
    // We don't use namespaces
    private static final String ns = null;

    public NewsRSSLoader(Context context) {
        super(context);
    }

    @Override
    public ArrayList<RSSItem> loadInBackground() {
        ArrayList<RSSItem> data = new ArrayList<RSSItem>();
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(timeout, unit);

        Request request = new Request.Builder().url(UCTConstants.UCT_DAILY_NEWS_URL).build();

        try {
            Response response = client.newCall(request).execute();
            if(response.isSuccessful()) {
                Log.d(TAG+"1", "Got response, parsing data paper");

                data = parseFeed(response.body().charStream());
                Log.d(TAG+"3", "feedSize="+data.size());

                for(RSSItem item : data){
                    Log.d(TAG, "ITEM: "+item.toString());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        request = new Request.Builder().url(UCTConstants.UCT_MONDAY_PAPER_URL).build();

        try {
            Response response = client.newCall(request).execute();
            if(response.isSuccessful()) {
                Log.d(TAG+"1", "Got response, parsing data for monday paper");

                data.addAll(parseFeed(response.body().charStream()));
                Log.d(TAG+"3", "feedSize="+data.size());

                for(RSSItem item : data){
                    Log.d(TAG, "ITEM: "+item.toString());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    /********************************************************/
    /** (2) Deliver the results to the registered listener **/
    /**
     * ****************************************************
     */

    @Override
    public void deliverResult(ArrayList<RSSItem> data) {
        if (isReset()) {
            // The Loader has been reset; ignore the result and invalidate the data.
            releaseResources(data);
            return;
        }
//        Log.d(TAG, "dr not reset");

        // Hold a reference to the old data so it doesn't get garbage collected.
        // We must protect it until the new data has been delivered.
        ArrayList<RSSItem> oldData = newsItems;
        newsItems = data;

        if (isStarted()) {
            // If the Loader is in a started state, deliver the results to the
            // client. The superclass method does this for us.
            Collections.sort(data);
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
    public void onCanceled(ArrayList<RSSItem> data) {
        // Attempt to cancel the current asynchronous load.
        super.onCanceled(data);

        // The load has been canceled, so we should release the resources
        // associated with 'data'.
        releaseResources(data);
    }

    private void releaseResources(ArrayList<RSSItem> data) {
        // For a simple List, there is nothing to do. For something like a Cursor, we
        // would close it in this method. All resources associated with the Loader
        // should be released here.
        data.clear();
    }

    private ArrayList<RSSItem> parseFeed(Reader input) {
        //Log.d(TAG, "about to start reading tag");
        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(input);
            parser.nextTag();
            parser.nextTag();
            //Log.d(TAG, "about to return feed");
            return readFeed(parser);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //Log.d(TAG, "about to return null feed");
        return null;
    }

    private ArrayList<RSSItem> readFeed(XmlPullParser parser) throws IOException, XmlPullParserException {
        //Log.d(TAG, "inside feed reader");
        ArrayList<RSSItem> entries = new ArrayList<RSSItem>();

        //Log.d(TAG+"4", parser.getName());
        parser.require(XmlPullParser.START_TAG, ns, UCTConstants.RSS_FEED_FEED_TAG);        //find initial feed tag
        while (parser.next() != XmlPullParser.END_TAG) {            //while not reached end of "feed tag"
//            //Log.d(TAG+"5", parser.getName());
            if (parser.getEventType() != XmlPullParser.START_TAG) { //If current reading point isn't a start tag, skip it
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals(UCTConstants.RSS_FEED_ENTRY_TAG)) {
                //Log.d(TAG, "about to read an entry");
                entries.add(readEntry(parser));
            } else {
                skip(parser);
            }
        }
        return entries;
    }

    //read entry
    private RSSItem readEntry(XmlPullParser parser) throws IOException, XmlPullParserException {
        //Log.d(TAG, "started to read an entry");
        parser.require(XmlPullParser.START_TAG, ns, UCTConstants.RSS_FEED_ENTRY_TAG); //ensure reading from "entry" start tag
        String title = null;
        String description = null;
        String link = null;
        String pubDate = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals(UCTConstants.RSS_FEED_TITLE_TAG)) {
                //Log.d(TAG, "about to read a title");
                title = readTitle(parser);
            } else if (name.equals(UCTConstants.RSS_FEED_DESCRIPTION_TAG)) {
                //Log.d(TAG, "about to read a description");
                description = readSummary(parser);
            } else if (name.equals(UCTConstants.RSS_FEED_LINK_TAG)) {
                //Log.d(TAG, "about to read a link");
                link = readLink(parser);
            } else if (name.equals(UCTConstants.RSS_FEED_PUB_DATE_TAG)){
                //Log.d(TAG, "about to read a date");
                pubDate = readPubDate(parser);
            } else {
                skip(parser);
            }
        }
        return new RSSItem(title, link, description, pubDate);
    }

    // Processes title tags in the feed.
    private String readTitle(XmlPullParser parser) throws IOException, XmlPullParserException {
        //Log.d(TAG, "started to read a title");
        parser.require(XmlPullParser.START_TAG, ns, UCTConstants.RSS_FEED_TITLE_TAG);
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, UCTConstants.RSS_FEED_TITLE_TAG);
        return title;
    }

    // Processes link tags in the feed.
    private String readLink(XmlPullParser parser) throws IOException, XmlPullParserException {
        //Log.d(TAG, "started to read a link");
        parser.require(XmlPullParser.START_TAG, ns, UCTConstants.RSS_FEED_LINK_TAG);
        String link = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, UCTConstants.RSS_FEED_LINK_TAG);
        return link;
    }

    // Processes summary tags in the feed.
    private String readSummary(XmlPullParser parser) throws IOException, XmlPullParserException {
        //Log.d(TAG, "started to read a description");
        parser.require(XmlPullParser.START_TAG, ns, UCTConstants.RSS_FEED_DESCRIPTION_TAG);
        String description = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, UCTConstants.RSS_FEED_DESCRIPTION_TAG);
        return description;
    }

    private String readPubDate(XmlPullParser parser) throws IOException, XmlPullParserException {
        //Log.d(TAG, "started to read a date");
        parser.require(XmlPullParser.START_TAG, ns, UCTConstants.RSS_FEED_PUB_DATE_TAG);
        String pubDate = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, UCTConstants.RSS_FEED_PUB_DATE_TAG);
        return pubDate;
    }

    // For the tags title and summary, extracts their text values.
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    //Performs the skip operation for whatever tag initiated the operation
    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}
