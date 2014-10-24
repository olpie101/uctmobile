package za.ac.myuct.klmedu001.uctmobile;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;
import za.ac.myuct.klmedu001.uctmobile.constants.RSSItem;
import za.ac.myuct.klmedu001.uctmobile.constants.UCTConstants;

public class NewsArticleActivity extends ActionBarActivity {
    @InjectView(R.id.main_toolbar)
    Toolbar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_article);
        ButterKnife.inject(this);
        actionBar.setTitle("TESTING !@#");
        setSupportActionBar(actionBar);
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
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
        switch(id) {
            case R.id.action_settings:
                return true;
            case android.R.id.home:
                super.onOptionsItemSelected(item);
                this.finish();
                this.overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
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

        //get the required html and display and load images from online
        @Override
        public void onResume() {
            super.onResume();
            RSSItem article = getActivity().getIntent().getParcelableExtra(UCTConstants.BUNDLE_EXTRA_RSS_ITEM);

            String finalHtml = createHtmlFromArticle(article);
            Log.d(TAG, finalHtml);
            webView.loadDataWithBaseURL(UCTConstants.UCT_URL, finalHtml, "text/html", "UTF-8", null);
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
            ButterKnife.reset(this);
        }

        /**
         *
         * @param item RSSItem
         * @return Full HTML string to display in webview, styling included
         */
        private String createHtmlFromArticle(RSSItem item){
            return UCTConstants.html_header_body_open+
                    "<div class='top-section'><h1>"+item.title+"</h1></div>" +
                    "<p class='date'>"+item.pubDate+"</p>"+item.description
                    +UCTConstants.html_body_close;
        }
    }
}
