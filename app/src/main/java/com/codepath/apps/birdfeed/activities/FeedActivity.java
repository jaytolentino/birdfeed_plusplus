package com.codepath.apps.birdfeed.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.codepath.apps.birdfeed.R;
import com.codepath.apps.birdfeed.fragments.ComposeTweetFragment;
import com.codepath.apps.birdfeed.fragments.HomeTimelineFragment;
import com.codepath.apps.birdfeed.fragments.MentionsTimelineFragment;
import com.codepath.apps.birdfeed.fragments.SearchResultsFragment;
import com.codepath.apps.birdfeed.fragments.SupportFragmentTabListener;
import com.codepath.apps.birdfeed.fragments.AbstractTweetListFragment;
import com.codepath.apps.birdfeed.networking.TwitterApplication;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

public class FeedActivity extends BaseActivity
        implements ComposeTweetFragment.RefreshTimelineListener {
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        setupTabs();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.feed, menu);
//        menu.findItem(R.id.action_profile).setIcon() TODO make profile picture the profile icon

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
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onComposeTweet(MenuItem item) {
        Bundle bundle = new Bundle();
        bundle.putString("title", "Compose New Tweet");

        FragmentManager fm = getSupportFragmentManager();
        ComposeTweetFragment composeTweet = ComposeTweetFragment.newInstance("Write a new tweet");
        composeTweet.setArguments(bundle);
        composeTweet.show(fm, "fragment_compose_tweet");
    }

    public void onViewProfile(MenuItem item) {
        Intent viewProfile = new Intent(this, ProfileActivity.class);
        startActivity(viewProfile);
    }

    private void setupTabs() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowTitleEnabled(true);

        ActionBar.Tab homeTimeline = actionBar
                .newTab()
                .setText(R.string.title_home_fragment)
                .setTabListener(new SupportFragmentTabListener<HomeTimelineFragment>(
                        R.id.fragment_timeline,
                        this,
                        "first",
                        HomeTimelineFragment.class));

        actionBar.addTab(homeTimeline);
        actionBar.selectTab(homeTimeline);

        ActionBar.Tab mentionsTimeline = actionBar
                .newTab()
                .setText(R.string.title_mentions_fragment)
                .setTabListener(new SupportFragmentTabListener<MentionsTimelineFragment>(
                        R.id.fragment_timeline,
                        this,
                        "first",
                        MentionsTimelineFragment.class));

        actionBar.addTab(mentionsTimeline);
    }

    @Override
    public void refreshAfterTweet() {
        AbstractTweetListFragment abstractTweetListFragment = (AbstractTweetListFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_timeline);
        abstractTweetListFragment.refreshTimeline();
    }

    private void search(String query) {
        Intent searchResults = new Intent(this, SearchActivity.class);
        searchResults.putExtra(SearchActivity.QUERY, query);
        startActivity(searchResults);
    }
}