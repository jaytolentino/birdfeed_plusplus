package com.codepath.apps.birdfeed.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.codepath.apps.birdfeed.activities.TweetDetailActivity;
import com.codepath.apps.birdfeed.adapters.EndlessScrollListener;
import com.codepath.apps.birdfeed.models.Tweet;
import com.codepath.apps.birdfeed.networking.TwitterApplication;
import com.codepath.apps.birdfeed.networking.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import org.json.JSONArray;

import java.util.List;


/**
 * Created by jay on 10/28/14.
 */
public class HomeTimelineFragment extends TweetListFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void populateTimeline() {
        super.populateTimeline();
        client.getHomeTimeline(new TimelineJsonHandler());
    }

    @Override
    protected void setupTweetScroll() {
        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                Log.d("debug", "Began loading endless scroll");
                client.getHomeTimeline(earliestId, new ScrollRefreshJsonHandler());
            }
        });
    }

    @Override
    protected void setupTweetClick() {
//        lvTweets.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
//                Intent tweetDetailView = new Intent(getActivity(), TweetDetailActivity.class);
//                tweetDetailView.putExtra("tweetPosition", position);
//                startActivity(tweetDetailView);
//            }
//        });
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
