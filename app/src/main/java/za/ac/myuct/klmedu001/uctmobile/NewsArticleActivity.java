package za.ac.myuct.klmedu001.uctmobile;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.webkit.WebView;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;
import za.ac.myuct.klmedu001.uctmobile.R;
import za.ac.myuct.klmedu001.uctmobile.constantsandprocesses.RSSItem;
import za.ac.myuct.klmedu001.uctmobile.constantsandprocesses.UCTConstants;

public class NewsArticleActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_article);
        if (savedInstanceState == null) {
            Toast.makeText(this, "Article Null", Toast.LENGTH_SHORT).show();
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }else{
            Toast.makeText(this, "Article != Null", Toast.LENGTH_SHORT).show();
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.news_article, menu);
        return true;
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
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        @InjectView(R.id.wv_news_article)WebView webView;
        public String TAG = "NEWS_ARTICLE_FRAGMENT";

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_news_article, container, false);
            ButterKnife.inject(this, rootView);
            return rootView;
        }

        @Override
        public void onResume() {
            super.onResume();
            RSSItem article = getActivity().getIntent().getParcelableExtra(UCTConstants.BUNDLE_EXTRA_RSS_ITEM);

            String finalHtml = UCTConstants.html_header_body_open+"<div class='top-section'><h1>"+article.title+"</h1></div><p class='date'>"+article.pubDate+"</p>"+article.description+UCTConstants.html_body_close;
            Log.d(TAG, finalHtml);
            webView.loadDataWithBaseURL(UCTConstants.UCT_URL, finalHtml, "text/html", "UTF-8", null);
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
            ButterKnife.reset(this);
        }
    }
}
