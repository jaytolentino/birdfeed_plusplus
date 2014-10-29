package com.codepath.apps.birdfeed.fragments;

import android.os.Bundle;
import android.util.Log;
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
    private TwitterClient client;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApplication.getRestClient();
        populateTimeline();
    }

    // TODO used for refreshing after composing/sending tweet, but using getActivity :( from ComposeTweetFragment - line 110
    public void refreshTimeline() {
        clearAll();
        populateTimeline();
    }

    private void populateTimeline() {
        startProgressBar();
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONArray json) {
                addAll(Tweet.fromJSONArray(json));
                setEarliestId();
                Log.d("debug", "Finished populating feed");
                stopProgressBar();
            }

            @Override
            public void onFailure(Throwable throwable, String s) {
                Log.d("debug", throwable.toString());
                Log.d("debug", s);
            }
        });
    }
}
