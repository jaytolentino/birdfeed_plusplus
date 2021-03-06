package com.codepath.apps.birdfeed.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.codepath.apps.birdfeed.R;
import com.codepath.apps.birdfeed.activities.BaseActivity;
import com.codepath.apps.birdfeed.activities.TweetDetailActivity;
import com.codepath.apps.birdfeed.adapters.TweetsAdapter;
import com.codepath.apps.birdfeed.models.Tweet;
import com.codepath.apps.birdfeed.networking.TwitterApplication;
import com.codepath.apps.birdfeed.networking.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.commons.collections4.ListUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jay on 10/28/14.
 */
public abstract class AbstractTweetListFragment extends Fragment {
    protected TwitterClient client;
    protected static ArrayList<Tweet> tweets;
    protected TweetsAdapter aTweets;
    protected ListView lvTweets;
    protected String earliestId;
    protected SwipeRefreshLayout swipeContainer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tweets = new ArrayList<Tweet>();
        aTweets = new TweetsAdapter(getActivity(), tweets);
        client = TwitterApplication.getRestClient();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tweets_list, container, false);
        lvTweets = (ListView) v.findViewById(R.id.lvTweets);
        lvTweets.setAdapter(aTweets);
        setupTweetScroll();
        setupTweetClick();
        setupSwipeContainer(v);
        if (isNetworkAvailable()) {
            populateTimeline();
        } else {
            displayConnectivityAlert();
        }
        return v;
    }

    protected void displayConnectivityAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Network Unavailable")
                .setMessage("Check network connectivity to load feed")
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create().show();
    }

    protected void populateTimeline() {
        startProgressBar();
    }

    protected abstract void setupTweetScroll();

    protected void setupTweetClick() {
        lvTweets.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent tweetDetailView = new Intent(getActivity(), TweetDetailActivity.class);
                tweetDetailView.putExtra("tweet", aTweets.getItem(position));
                startActivity(tweetDetailView);
            }
        });
    }

    protected void setupSwipeContainer(View view) {
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        swipeContainer.setColorScheme(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    protected abstract void loadRecentlySavedTweets();
//    {
//        List<Tweet> savedTweets = Tweet.getRecentlySaved();
//        if (!savedTweets.isEmpty()) {
//            addAll(savedTweets);
//            setEarliestId();
//            Log.d("debug", "Added " + savedTweets.size() + " saved tweets");
//        }
//    }

    public abstract void refreshTimeline();

    public static boolean hasTweets() {
        return tweets != null;
    }

    public static Tweet getTweet(int position) {
        return tweets.get(position);
    }

    protected void clearAll() {
        aTweets.clear();
    }

    protected void setEarliestId() {
        earliestId = String.valueOf(aTweets.getLastTweet().getTweetId());
    }

    protected void startProgressBar() {
        ((BaseActivity) getActivity()).showProgressBar();
    }

    protected void stopProgressBar() {
        ((BaseActivity) getActivity()).hideProgressBar();
    }

    protected class TimelineJsonHandler extends JsonHttpResponseHandler {
        @Override
        public void onSuccess(JSONArray json) {
            aTweets.addAll(Tweet.fromJSONArray(json));
            setEarliestId();
            Log.d("debug", "Finished populating feed");
            stopProgressBar();
        }

        @Override
        public void onFailure(Throwable throwable, String s) {
            Log.d("debug", throwable.toString());
            Log.d("debug", s);
        }
    }

    protected boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public class SearchResultsJsonHandler extends JsonHttpResponseHandler {
        @Override
        public void onSuccess(int i, JSONObject jsonObject) {
            try {
                aTweets.addAll(Tweet.fromJSONArray(jsonObject.getJSONArray("statuses")));
                setEarliestId();
                stopProgressBar();
            } catch (JSONException e) {
                Log.e("ERROR", e.toString());
            }

        }

        @Override
        public void onFailure(Throwable throwable, String s) {
            Log.d("debug", throwable.toString());
            Log.e("ERROR", s);
        }
    }

    protected class ScrollRefreshJsonHandler extends JsonHttpResponseHandler {
        @Override
        public void onSuccess(JSONArray json) {
            aTweets.addAll(Tweet.fromJSONArray(json));
            setEarliestId();
            Log.d("debug", "Finished loading endless scroll");
        }

        @Override
        public void onFailure(Throwable throwable, String s) {
            Log.d("debug", throwable.toString());
            Log.d("debug", s);
        }
    }

    protected class RefreshWithNewItemsJsonHandler extends JsonHttpResponseHandler{
        private ArrayList<Tweet> childTweets;

        public RefreshWithNewItemsJsonHandler(ArrayList<Tweet> childTweets) {
            this.childTweets = childTweets;
        }
        @Override // TODO  refactor?
        public void onSuccess(JSONArray json) {
            List<Tweet> refreshedTweets = ListUtils.union(Tweet.fromJSONArray(json), childTweets);
            aTweets.clear();
            aTweets.addAll(refreshedTweets);
            swipeContainer.setRefreshing(false);
        }

        @Override
        public void onFailure(Throwable throwable, String s) {
            Log.d("debug", throwable.toString());
            Log.d("debug", s);
        }
    }
}
