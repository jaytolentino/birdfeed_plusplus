package com.codepath.apps.birdfeed.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.codepath.apps.birdfeed.adapters.EndlessScrollListener;
import com.codepath.apps.birdfeed.networking.TwitterApplication;

public class SearchResultsFragment extends AbstractTweetListFragment {

    private SearchListener mListener;
    private String mQuery;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof SearchListener) {
            mListener = (SearchListener) activity;
        } else {
            throw new ClassCastException(activity.toString()
                    + " must implement SearchResultsFragment.SearchListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mQuery = mListener.getQuery();
    }

    @Override
    protected void populateTimeline() {
        super.populateTimeline();
        client.getSearch(mQuery, new SearchResultsJsonHandler());
    }

    public void populateTimeline(String s) {
        client.getSearch(s, new SearchResultsJsonHandler());
    }

    @Override
    protected void setupTweetScroll() {
        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                client.getSearch(earliestId, mQuery, new ScrollRefreshJsonHandler());
            }
        });
    }

    @Override
    protected void setupSwipeContainer(View view) {
        super.setupSwipeContainer(view);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!aTweets.isEmpty()) {
                    refreshTimeline();
                    // TODO can replace with getNewSearchItems to only get new tweets
                    swipeContainer.setRefreshing(false);
                }
            }
        });
    }

    @Override
    protected void loadRecentlySavedTweets() {

    }

    @Override
    public void refreshTimeline() {
        clearAll();
        populateTimeline();
    }

    public static interface SearchListener {
        public String getQuery();
    }
}
