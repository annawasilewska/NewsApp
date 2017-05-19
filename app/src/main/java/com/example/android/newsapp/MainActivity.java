package com.example.android.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.data;
import static com.example.android.newsapp.R.id.rvNews;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {

    ArrayList<News> news;
    private static final int BOOK_LOADER_ID = 1;
    private NewsAdapter adapter;
    public RecyclerView rvNews;
    public static Context mContext;
    /**
     * ProgressBar
     */
    private ProgressBar loadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = getApplicationContext();

        loadingIndicator = (ProgressBar) findViewById(R.id.progress_bar);
        loadingIndicator.setVisibility(View.VISIBLE);

        // Lookup the recyclerview in activity layout
        rvNews = (RecyclerView) findViewById(R.id.rvNews);

        // Initialize contacts
        news = new ArrayList<>();

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {

            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(BOOK_LOADER_ID, null, MainActivity.this);

            if (getLoaderManager().getLoader(BOOK_LOADER_ID).isStarted()) {
                //restart it if there's one
                getLoaderManager().restartLoader(BOOK_LOADER_ID, null, MainActivity.this);
            }

            // Create adapter passing in the sample user data
            adapter = new NewsAdapter(this, news);
            // Attach the adapter to the recyclerview to populate items
            rvNews.setAdapter(adapter);
            // Set layout manager to position the items
            rvNews.setLayoutManager(new LinearLayoutManager(this));

            RecyclerView.ItemDecoration itemDecoration = new
                    DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
            rvNews.addItemDecoration(itemDecoration);

        } else {
            Toast.makeText(MainActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {
        // Create a new loader for the given URL
        return new NewsLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> data) {

        // If there is a valid list of {@link News}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (data != null && !data.isEmpty()) {
            news.addAll(data);
            adapter.notifyDataSetChanged();
        }

        if (QueryUtils.errorMessage != null) {
            Toast.makeText(MainActivity.mContext, QueryUtils.errorMessage, Toast.LENGTH_SHORT).show();
            QueryUtils.errorMessage = null;
        }
        loadingIndicator.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        adapter.notifyDataSetChanged();
    }

    /** To prevent duplicating news when onResume called */
    @Override
    public void onResume(){
        super.onResume();
        news.clear();

        if (getLoaderManager().getLoader(BOOK_LOADER_ID).isStarted()) {
            //restart it if there's one
            getLoaderManager().restartLoader(BOOK_LOADER_ID, null, MainActivity.this);
        }
    }
}
