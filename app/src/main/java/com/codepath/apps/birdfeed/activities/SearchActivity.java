package com.codepath.apps.birdfeed.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.codepath.apps.birdfeed.R;
import com.codepath.apps.birdfeed.fragments.AbstractTweetListFragment;
import com.codepath.apps.birdfeed.fragments.SearchResultsFragment;
import com.codepath.apps.birdfeed.networking.TwitterApplication;

public class SearchActivity extends BaseActivity
        implements SearchResultsFragment.SearchListener {

    public static final String QUERY = "query";

    private String mQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (getIntent().hasExtra(QUERY)) {
            mQuery = getIntent().getStringExtra(QUERY);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        setTitle("Search");
        TextView tvResultsTitle = (TextView) findViewById(R.id.tvResultsTitle);
        tvResultsTitle.setText("Results for \"" + mQuery + "\"");
        return super.onCreateView(name, context, attrs);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        if (searchView != null) {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    search(s);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    return false;
                }
            });
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public String getQuery() {
        return mQuery;
    }

    private void search(String s) {
        SearchResultsFragment fragment = (SearchResultsFragment) getSupportFragmentManager().findFragmentById(R.id.frSearchResults);
        fragment.populateTimeline(s);
    }
}
