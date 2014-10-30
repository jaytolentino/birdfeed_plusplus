package com.codepath.apps.birdfeed.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.codepath.apps.birdfeed.adapters.EndlessScrollListener;
import com.codepath.apps.birdfeed.models.Tweet;
import com.codepath.apps.birdfeed.networking.TwitterApplication;
import com.codepath.apps.birdfeed.networking.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;

/**
 * Created by jay on 10/29/14.
 */
public class MentionsTimelineFragment extends TweetListFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void populateTimeline() {
        super.populateTimeline();
        client.getMentionsTimeline(new TimelineJsonHandler());
    }

    @Override
    protected void setupTweetScroll() {
        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                Log.d("debug", "Began loading endless scroll");
                client.getMentionsTimeline(earliestId, new ScrollRefreshJsonHandler());
            }
        });
    }

    @Override
    protected void setupTweetClick() {

    }

    @Override
    protected void setupSwipeContainer(View view) {

    }

    @Override
    protected void refreshFeed() {

    }

    @Override
    protected void loadRecentlySavedTweets() {

    }

    @Override
    protected void refreshTimeline() {

    }
}
