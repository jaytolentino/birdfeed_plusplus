package com.codepath.apps.birdfeed.networking;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;
import android.util.Log;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;

public class TwitterClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class;
	public static final String REST_URL = "https://api.twitter.com/1.1";
	public static final String REST_CONSUMER_KEY = "4lMCajAT68HMtiGOIFjXph0Kb";
	public static final String REST_CONSUMER_SECRET
            = "Zow9hfIn4b3CSqiJuFPf9tUaoT5wjyePigUH8LmNteBVsXeZwD";
	public static final String REST_CALLBACK_URL = "oauth://cpbasictweet";

    public static enum USER_TYPE {
        FOLLOWING, FOLLOWERS
    }

	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}

    public void getHomeTimeline(AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/home_timeline.json");


        Log.i("INFO", "GET " + apiUrl);
        client.get(apiUrl, null, handler);
    }

    public void getHomeTimeline(String earliestId, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/home_timeline.json");
        RequestParams params = new RequestParams();
        params.put("max_id", earliestId);

        Log.i("INFO", "GET " + apiUrl + ", MAX_ID: " + earliestId);
        client.get(apiUrl, params, handler);
    }

    public void getMentionsTimeline(AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/mentions_timeline.json");

        Log.i("INFO", "GET " + apiUrl);
        client.get(apiUrl, null, handler);
    }

    public void getMentionsTimeline(String earliestId, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/mentions_timeline.json");
        RequestParams params = new RequestParams();
        params.put("max_id", earliestId);

        Log.i("INFO", "GET " + apiUrl);
        client.get(apiUrl, params, handler);
    }

    public void getNewHomeItems(String mostRecentId, AsyncHttpResponseHandler handler){
        String apiUrl = getApiUrl("statuses/home_timeline.json");
        RequestParams params = new RequestParams();
        params.put("since_id", mostRecentId);

        Log.i("INFO", "GET " + apiUrl + ", SINCE_ID: " + mostRecentId);
        client.get(apiUrl, params, handler);
    }

    public void getNewMentionItems(String mostRecentId, AsyncHttpResponseHandler handler){
        String apiUrl = getApiUrl("statuses/mentions_timeline.json");
        RequestParams params = new RequestParams();
        params.put("since_id", mostRecentId);

        Log.i("INFO", "GET " + apiUrl + ", SINCE_ID: " + mostRecentId);
        client.get(apiUrl, params, handler);
    }

    public void getMyInfo(AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("account/verify_credentials.json");

        Log.i("INFO", "GET " + apiUrl);
        client.get(apiUrl, null, handler);
    }

    public void getUserTimeline(AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/user_timeline.json");

        Log.i("INFO", "GET " + apiUrl);
        client.get(apiUrl, null, handler);
    }

    public void getUserTimeline(String earliestId, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/user_timeline.json");
        RequestParams params = new RequestParams();
        params.put("max_id", earliestId);

        Log.i("INFO", "GET " + apiUrl);
        client.get(apiUrl, params, handler);
    }

    public void getUserTimeline(long uid, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/user_timeline.json");
        RequestParams params = new RequestParams();
        params.put("user_id", Long.toString(uid));

        Log.i("INFO", "GET " + apiUrl);
        client.get(apiUrl, params, handler);
    }

    public void postNewTweet(String content, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/update.json");
        RequestParams params = new RequestParams();
        params.put("status", content);

        Log.i("INFO", "PUT " + apiUrl + ", STATUS: " + content);
        client.post(apiUrl, params, handler);
    }

    public void postReplyTweet(String content, long replyToId, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/update.json");
        RequestParams params = new RequestParams();
        params.put("status", content);
        params.put("in_reply_to_status_id", String.valueOf(replyToId));

        Log.i("INFO", "PUT " + apiUrl + ", STATUS: " + content + ", IN_REPLY_TO: " + replyToId);
        client.post(apiUrl, params, handler);
    }

    // TODO batch request for user info in background after doing home timeline and/or mentions request
    public void getUserInfo(ArrayList<String> userIds, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("users/lookup.json");
        RequestParams params = new RequestParams();
        params.put("user_id", userIds);

        Log.i("INFO", "GET " + apiUrl + ", USER_ID: " + userIds.get(0));
        client.get(apiUrl, params, handler);
    }

    public void getUsers(USER_TYPE type, String uid, AsyncHttpResponseHandler handler) {
        String apiUrl = "";
        if (type == USER_TYPE.FOLLOWERS) {
            apiUrl = getApiUrl("followers/list.json");
        } else if (type == USER_TYPE.FOLLOWING) {
            apiUrl = getApiUrl("friends/list.json");
        }
        RequestParams params = new RequestParams();
        params.put("user_id", uid);
        params.put("count", "40");

        Log.i("INFO", "GET " + apiUrl + ", USER_ID: " + uid);
        client.get(apiUrl, params, handler);
    }

    public void postFavorite(long tweetId, AsyncHttpResponseHandler handler) {
        String apiUrl = "favorites/create.json";
        RequestParams params = new RequestParams();
        params.put("id", Long.toString(tweetId));
        params.put("include_entities", "false");

        Log.i("INFO", "POST " + apiUrl + ", TWEET_ID: " + tweetId);
        client.post(apiUrl, params, handler);
    }

    public void postFavoriteDestroy(long tweetId, AsyncHttpResponseHandler handler) {
        String apiUrl = "favorites/destroy.json";
        RequestParams params = new RequestParams();
        params.put("id", Long.toString(tweetId));
        params.put("include_entities", "false");

        Log.i("INFO", "POST " + apiUrl + ", TWEET_ID: " + tweetId);
        client.post(apiUrl, params, handler);
    }

    public void postRetweet(long tweetId, AsyncHttpResponseHandler handler) {
        String apiUrl = "statuses/retweet/" + tweetId + ".json";

        Log.i("INFO", "POST " + apiUrl + ", TWEET_ID: " + tweetId);
        client.post(apiUrl, null, handler);
    }
}