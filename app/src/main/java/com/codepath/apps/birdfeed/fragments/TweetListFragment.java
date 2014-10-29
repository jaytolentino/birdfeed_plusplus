package com.codepath.apps.birdfeed.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.codepath.apps.birdfeed.R;
import com.codepath.apps.birdfeed.activities.BaseActivity;
import com.codepath.apps.birdfeed.activities.TweetDetailActivity;
import com.codepath.apps.birdfeed.adapters.EndlessScrollListener;
import com.codepath.apps.birdfeed.adapters.TweetsAdapter;
import com.codepath.apps.birdfeed.models.Tweet;
import com.codepath.apps.birdfeed.networking.TwitterApplication;
import com.codepath.apps.birdfeed.networking.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.commons.collections4.ListUtils;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jay on 10/28/14.
 */
public class TweetListFragment extends Fragment {
    private TwitterClient client;
    private static ArrayList<Tweet> tweets;
    private TweetsAdapter aTweets;
    private ListView lvTweets;
    private String earliestId;
    private SwipeRefreshLayout swipeContainer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tweets = new ArrayList<Tweet>();
        aTweets = new TweetsAdapter(getActivity(), tweets);
        client = TwitterApplication.getRestClient();
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tweets_list, container, false);
        initializeMemberVariables(v);
        return v;
    }

    private void initializeMemberVariables(View view) {
        lvTweets = (ListView) view.findViewById(R.id.lvTweets);
        lvTweets.setAdapter(aTweets);
        setupTweetScroll();
        setupTweetClick();
        setupSwipeContainer(view);
    }

    // TODO: possible to reduce repeated code here?
    private void setupTweetScroll() {
        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                Log.d("debug", "Began loading endless scroll");
                client.getHomeTimeline(earliestId, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONArray json) {
                        aTweets.addAll(Tweet.fromJSONArray(json));
                        earliestId = String.valueOf(aTweets.getItem(tweets.size() - 1).getTweetId());
                        Log.d("debug", "Finished loading endless scroll");
                    }

                    @Override
                    public void onFailure(Throwable throwable, String s) {
                        Log.d("debug", throwable.toString());
                        Log.d("debug", s);
                    }
                });
            }
        });
    }

    private void setupTweetClick() {
        lvTweets.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Intent tweetDetailView = new Intent(getActivity(), TweetDetailActivity.class);
                tweetDetailView.putExtra("tweetPosition", position);
                startActivity(tweetDetailView);
            }
        });
    }

    private void setupSwipeContainer(View view) {
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshFeed();
            }
        });
        swipeContainer.setColorScheme(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    // TODO: get rid of repeated error handling code when checking for net connection
    // TODO: refreshFeed() vs refreshTimeline()... -_-
    private void refreshFeed() {
        if (!aTweets.isEmpty()) {
            String mostRecentId = String.valueOf(aTweets.getItem(0).getTweetId());
            Log.d("debug", "Began refreshing feed");
            client.getNewTimelineItems(mostRecentId, new JsonHttpResponseHandler() {
                @Override //TODO refactor?
                public void onSuccess(JSONArray json) {
                    List<Tweet> refreshedTweets = ListUtils.union(Tweet.fromJSONArray(json), tweets);
                    aTweets.clear();
                    addAll(refreshedTweets);
                    swipeContainer.setRefreshing(false);
                    Log.d("debug", "Finished refreshing feed");
                }

                @Override
                public void onFailure(Throwable throwable, String s) {
                    Log.d("debug", throwable.toString());
                    Log.d("debug", s);
                }
            });
        }
    }

    public static boolean hasTweets() {
        return tweets != null;
    }

    public static Tweet getTweet(int position) {
        return tweets.get(position);
    }

    public void addAll(List<Tweet> tweets) {
        aTweets.addAll(tweets);
    }

    public void clearAll() {
        aTweets.clear();
    }

    public void setEarliestId() {
        String.valueOf(aTweets.getItem(tweets.size() - 1).getTweetId());
    }

    public void startProgressBar() {
        ((BaseActivity) getActivity()).showProgressBar();
    }

    public void stopProgressBar() {
        ((BaseActivity) getActivity()).hideProgressBar();
    }
}
