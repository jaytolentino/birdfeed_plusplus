package com.codepath.apps.birdfeed.models;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.codepath.apps.birdfeed.utils.TimestampParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jay on 10/17/14.
 */
@Table(name = "tweets")
public class Tweet extends Model implements Serializable {

    @Column(name = "body")
    private String body;

    @Column(name = "tweet_id", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private long tweetId;

    @Column(name = "created_at")
    private String createdAt;

    @Column(name = "user", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    private User user;

    @Column(name = "retweet_count")
    private String retweetCount;

    @Column(name = "favorite_count")
    private String favoriteCount;

    @Column(name = "media_url")
    private String mediaUrl;

    public Tweet() {
        super();
    }

    public Tweet(String body, long tweetId, String createdAt, User user, String retweetCount, String favoriteCount) {
        super();
        this.body = body;
        this.tweetId = tweetId;
        this.createdAt = createdAt;
        this.user = user;
        this.retweetCount = retweetCount;
        this.favoriteCount = favoriteCount;
        this.save();
    }

    public static Tweet fromJSON(JSONObject jsonObject) {
        Tweet tweet = new Tweet();

        try {
            tweet.body = jsonObject.getString("text");
            tweet.tweetId = jsonObject.getLong("id");
            tweet.createdAt = jsonObject.getString("created_at");
            tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
            tweet.retweetCount = jsonObject.getString("retweet_count");
            tweet.favoriteCount =  jsonObject.getString("favorite_count");
            if (jsonObject.has("extended_entities")) {
                tweet.setMediaUrl(jsonObject.getJSONObject("extended_entities"));
            } else {
                tweet.mediaUrl = null;
            }
            long result = tweet.save();
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return tweet;
    }

    public static ArrayList<Tweet> fromJSONArray(JSONArray jsonArray) {
        ArrayList<Tweet> tweets = new ArrayList<Tweet>();

        ActiveAndroid.beginTransaction();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject tweetJson = null;
                try {
                    tweetJson = jsonArray.getJSONObject(i);
                }
                catch (JSONException e) {
                    e.printStackTrace();
                    continue;
                }
                Tweet tweet = Tweet.fromJSON(tweetJson);
                if (tweet != null) {
                    tweets.add(tweet);
                }
            }
            ActiveAndroid.setTransactionSuccessful();
        }
        finally {
            ActiveAndroid.endTransaction();
        }


        return tweets;
    }

    @Override
    public String toString() {
        return getBody();
    }

    public String getBody() {
        return body;
    }

    public long getTweetId() {
        return tweetId;
    }

    public String getRelativeTimetamp() {
        return TimestampParser.getRelativeTimeAgo(createdAt);
    }

    public User getUser() {
        return user;
    }

    public String getRetweetCount() {
        return retweetCount;
    }

    public String getFavoriteCount() {
        return favoriteCount;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public boolean hasMedia() {
        return mediaUrl != null;
    }

    private void setMediaUrl(JSONObject extEntities) {
        try {
            JSONArray media = extEntities.getJSONArray("media");
            JSONObject mediaObject = media.getJSONObject(0);
            mediaUrl = (String) mediaObject.get("media_url");
        }
        catch (JSONException e) {
            e.printStackTrace();
            mediaUrl = null;
        }
    }

    public static List<Tweet> getRecentlySaved() {
        return new Select()
                .from(Tweet.class)
                .limit(20)
                .orderBy("tweet_id DESC")
                .execute();
    }
}