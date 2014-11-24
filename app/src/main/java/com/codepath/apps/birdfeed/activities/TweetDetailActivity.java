package com.codepath.apps.birdfeed.activities;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.birdfeed.R;
import com.codepath.apps.birdfeed.fragments.ComposeTweetFragment;
import com.codepath.apps.birdfeed.models.Tweet;
import com.codepath.apps.birdfeed.models.User;
import com.codepath.apps.birdfeed.networking.TwitterApplication;
import com.codepath.apps.birdfeed.networking.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class TweetDetailActivity extends BaseActivity
        implements ComposeTweetFragment.RefreshTimelineListener {
    private Tweet tweet;
    private TwitterClient client;
    private boolean favorited;
    private boolean retweeted;

    private ImageView ivDetailProfilePic;
    private ImageView ivDetailBackground;
    private ImageView ivDetailMedia;
    private ImageView ivFavorite;
    private ImageView ivRetweet;

    private TextView tvDetailName;
    private TextView tvDetailUsername;
    private TextView tvDetailTweet;
    private TextView tvDetailTimestamp;
    private TextView tvCountRetweets;
    private TextView tvCountFavorites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_detail);
        setTitle("Tweet Details");
        tweet = (Tweet) getIntent().getSerializableExtra("tweet");
        client = TwitterApplication.getRestClient();
        favorited = false;
        retweeted = false;

        initailizeMemberVariables();
        setImageViews();
        setTextViews();
        setProfileOnClick();
        setRetweetOnClick();
        setFavoriteOnClick();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.tweet_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onReply(View view) {
        Bundle bundle = new Bundle();
        bundle.putString("title", "Reply to " + tweet.getUser().getName());
        bundle.putSerializable("tweet", tweet);

        FragmentManager fm = getSupportFragmentManager();
        ComposeTweetFragment composeTweet = ComposeTweetFragment.newInstance("Reply to " + tweet.getUser().getName());
        composeTweet.setArguments(bundle);
        composeTweet.show(fm, "fragment_compose_tweet");
    }

    private void initailizeMemberVariables() {
        ivDetailProfilePic = (ImageView) findViewById(R.id.ivDetailProfilePic);
        ivDetailBackground = (ImageView) findViewById(R.id.ivDetailBackground);
        ivDetailMedia = (ImageView) findViewById(R.id.ivDetailMedia);
        ivFavorite = (ImageView) findViewById(R.id.ivFavorite);
        ivRetweet = (ImageView) findViewById(R.id.ivRetweet);

        tvDetailName = (TextView) findViewById(R.id.tvDetailName);
        tvDetailUsername = (TextView) findViewById(R.id.tvDetailUsername);
        tvDetailTweet = (TextView) findViewById(R.id.tvDetailTweet);
        tvDetailTimestamp = (TextView) findViewById(R.id.tvDetailTimestamp);
        tvCountRetweets = (TextView) findViewById(R.id.tvCountRetweets);
        tvCountFavorites = (TextView) findViewById(R.id.tvCountFavorites);
    }

    private void setImageViews() {
        ImageLoader loader = ImageLoader.getInstance();
        ivDetailBackground.setImageResource(android.R.color.transparent);
        loader.displayImage(tweet.getUser().getCoverImageUrl(), ivDetailBackground);

        ivDetailProfilePic.setImageResource(android.R.color.transparent);
        loader.displayImage(tweet.getUser().getProfileImageUrl(), ivDetailProfilePic);

        if (tweet.hasMedia()) {
            ivDetailMedia.setVisibility(View.VISIBLE);
            ivDetailMedia.setImageResource(android.R.color.transparent);
            loader.displayImage(tweet.getMediaUrl(), ivDetailMedia);
        } else {
            ivDetailMedia.setVisibility(View.GONE);
        }
    }

    private void setTextViews() {
        tvDetailName.setText(tweet.getUser().getName());
        tvDetailUsername.setText("@" + tweet.getUser().getUsername());
        tvDetailTweet.setText(tweet.getBody());
        tvDetailTimestamp.setText(tweet.getRelativeTimetamp());
        tvCountRetweets.setText(tweet.getRetweetCount());
        tvCountFavorites.setText(tweet.getFavoriteCount());
    }

    public void toastSuccessfulReply() {
        Toast.makeText(this, "Reply sent!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void refreshAfterTweet() {
        Intent intent = new Intent(this, FeedActivity.class);
        startActivity(intent);
    }

    private void setProfileOnClick() {
        final String uid = Long.toString(tweet.getUser().getUid());
        View.OnClickListener clickToProfile = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getProfileInfo(uid);
            }
        };
        tvDetailUsername.setOnClickListener(clickToProfile);
        tvDetailName.setOnClickListener(clickToProfile);
        ivDetailProfilePic.setOnClickListener(clickToProfile);
    }

    private void setRetweetOnClick() {
        ivRetweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (retweeted) {
                    Toast.makeText(TweetDetailActivity.this, "Already retweeted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(TweetDetailActivity.this, "Reweeting tweet...", Toast.LENGTH_SHORT).show();
                    sendRetweetApiCall(tweet.getTweetId());
                    Toast.makeText(TweetDetailActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                    ivRetweet.setImageDrawable(getResources().getDrawable(R.drawable.ic_twitter_retweet_selected));
                    retweeted = true;
                }
            }
        });
    }

    private void setFavoriteOnClick() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (favorited) {
                    Toast.makeText(TweetDetailActivity.this, "Unfavoriting tweet...", Toast.LENGTH_SHORT).show();
                    sendUnfavoriteApiCall(tweet.getTweetId());
                    Toast.makeText(TweetDetailActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                    ivFavorite.setImageDrawable(getResources().getDrawable(R.drawable.ic_twitter_favorite));
                    favorited = true;
                } else {
                    Toast.makeText(TweetDetailActivity.this, "Favoriting tweet...", Toast.LENGTH_SHORT).show();
                    sendFavoriteApiCall(tweet.getTweetId());
                    Toast.makeText(TweetDetailActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                    ivFavorite.setImageDrawable(getResources().getDrawable(R.drawable.ic_twitter_favorite_selected));
                    favorited = true;
                }
            }
        };
        ivFavorite.setOnClickListener(listener);
    }

    private void getProfileInfo(String uid) {
        ArrayList<String> userIds = new ArrayList<String>();
        userIds.add(uid);
        TwitterApplication.getRestClient().getUserInfo(userIds, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONArray jsonArray) {
                try {
                    JSONObject userInfo = (JSONObject) jsonArray.get(0);
                    User user = User.fromJSON(userInfo);
                    Intent profileIntent = new Intent(TweetDetailActivity.this, ProfileActivity.class);
                    profileIntent.putExtra(ProfileActivity.USER, user);
                    startActivity(profileIntent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable throwable, JSONArray jsonArray) {
                Log.d("debug", throwable.getMessage());
            }
        });
    }

    private void sendFavoriteApiCall(long tweetId) {
        client.postFavorite(tweetId, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(JSONObject jsonObject) {
                Toast.makeText(TweetDetailActivity.this, "Success!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Throwable throwable, JSONObject jsonObject) {
                Toast.makeText(TweetDetailActivity.this, "Error favoriting tweet.", Toast.LENGTH_SHORT).show();
            }

            @Override
            protected void handleFailureMessage(Throwable throwable, String s) {
                super.handleFailureMessage(throwable, s);
                Log.d("debug", throwable.getMessage() + " - STRING: "+ s);
            }
        });
    }

    private void sendUnfavoriteApiCall(long tweetId) {
        client.postFavoriteDestroy(tweetId, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(JSONObject jsonObject) {
                Toast.makeText(TweetDetailActivity.this, "Success!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Throwable throwable, JSONObject jsonObject) {
                Toast.makeText(TweetDetailActivity.this, "Error unfavoriting tweet.", Toast.LENGTH_SHORT).show();
            }

            @Override
            protected void handleFailureMessage(Throwable throwable, String s) {
                super.handleFailureMessage(throwable, s);
                Log.d("debug", throwable.getMessage() + " - STRING: " + s);
            }
        });
    }

    private void sendRetweetApiCall(long tweetId) {
        client.postRetweet(tweetId, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                Toast.makeText(TweetDetailActivity.this, "Success!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Throwable throwable, JSONObject jsonObject) {
                Toast.makeText(TweetDetailActivity.this, "Error retweeting", Toast.LENGTH_SHORT).show();
            }

            @Override
            protected void handleFailureMessage(Throwable throwable, String s) {
                super.handleFailureMessage(throwable, s);
                Log.d("debug", throwable.getMessage() + " - STRING: " + s);
            }
        });
    }
}
