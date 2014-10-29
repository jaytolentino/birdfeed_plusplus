package com.codepath.apps.birdfeed.activities;


import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.codepath.apps.birdfeed.R;
import com.codepath.apps.birdfeed.adapters.EndlessScrollListener;
import com.codepath.apps.birdfeed.adapters.TweetsAdapter;
import com.codepath.apps.birdfeed.fragments.ComposeTweetFragment;
import com.codepath.apps.birdfeed.fragments.TweetListFragment;
import com.codepath.apps.birdfeed.models.Tweet;
import com.codepath.apps.birdfeed.models.User;
import com.codepath.apps.birdfeed.networking.TwitterApplication;
import com.codepath.apps.birdfeed.networking.TwitterClient;
import com.codepath.apps.birdfeed.utils.ProgressBarHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.commons.collections4.ListUtils;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

// TODO cleanup feedactivity with fragment(s)
public class FeedActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProgressBarHandler.create(this);
        setContentView(R.layout.activity_feed);
    }

    public void onComposeTweet(MenuItem item) {
        Bundle bundle = new Bundle();
        bundle.putString("title", "Compose New Tweet");

        FragmentManager fm = getSupportFragmentManager();
        ComposeTweetFragment composeTweet = ComposeTweetFragment.newInstance("Write a new tweet");
        composeTweet.setArguments(bundle);
        composeTweet.show(fm, "fragment_compose_tweet");
    }
}
