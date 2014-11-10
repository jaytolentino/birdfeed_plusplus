package com.codepath.apps.birdfeed.fragments;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.codepath.apps.birdfeed.adapters.EndlessScrollListener;

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
        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                client.getUserTimeline(earliestId, new ScrollRefreshJsonHandler());
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
