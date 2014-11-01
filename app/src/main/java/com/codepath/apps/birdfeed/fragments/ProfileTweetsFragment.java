package com.codepath.apps.birdfeed.fragments;

import android.support.v4.app.Fragment;

import com.loopj.android.http.JsonHttpResponseHandler;

/**
 * Created by jay on 10/31/14.
 */
public class ProfileTweetsFragment extends AbstractTweetListFragment {

    @Override
    protected void populateTimeline() {
        super.populateTimeline();
        client.getUserTimeline(new TimelineJsonHandler());
    }

    @Override
    protected void setupTweetScroll() {

    }

    @Override
    protected void loadRecentlySavedTweets() {

    }

    @Override
    public void refreshTimeline() {

    }
}
