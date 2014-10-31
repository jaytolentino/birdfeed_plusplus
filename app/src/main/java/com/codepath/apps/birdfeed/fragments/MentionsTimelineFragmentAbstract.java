package com.codepath.apps.birdfeed.fragments;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;

import com.codepath.apps.birdfeed.adapters.EndlessScrollListener;

/**
 * Created by jay on 10/29/14.
 */
public class MentionsTimelineFragmentAbstract extends AbstractTweetListFragment {
    private String mostRecentId;

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
    protected void setupSwipeContainer(View view) {
        super.setupSwipeContainer(view);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!aTweets.isEmpty()) {
                    refreshTimeline();
//                    mostRecentId = String.valueOf(aTweets.getItem(0).getTweetId());
//                    client.getNewMentionItems(mostRecentId, new RefreshWithNewItemsJsonHandler(tweets));
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
        // loadRecentlySavedTweets(); TODO this is the SAME in HomeTimeline! Reuse?
        populateTimeline();
    }
}
