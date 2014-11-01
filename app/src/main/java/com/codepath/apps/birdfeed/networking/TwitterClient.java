package com.codepath.apps.birdfeed.networking;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.FlickrApi;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;
import android.util.Log;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class;
	public static final String REST_URL = "https://api.twitter.com/1.1";
	public static final String REST_CONSUMER_KEY = "4lMCajAT68HMtiGOIFjXph0Kb";
	public static final String REST_CONSUMER_SECRET
            = "Zow9hfIn4b3CSqiJuFPf9tUaoT5wjyePigUH8LmNteBVsXeZwD";
	public static final String REST_CALLBACK_URL = "oauth://cpbasictweet";

	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}

    public void getHomeTimeline(AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/home_timeline.json");


        Log.d("DEBUG", "GET " + apiUrl);
        client.get(apiUrl, null, handler);
    }

    public void getHomeTimeline(String earliestId, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/home_timeline.json");
        RequestParams params = new RequestParams();
        params.put("max_id", earliestId);

        Log.d("DEBUG", "GET " + apiUrl + ", MAX_ID: " + earliestId);
        client.get(apiUrl, params, handler);
    }

    public void getMentionsTimeline(AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/mentions_timeline.json");

        Log.d("DEBUG", "GET " + apiUrl);
        client.get(apiUrl, null, handler);
    }

    public void getMentionsTimeline(String earliestId, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/mentions_timeline.json");
        RequestParams params = new RequestParams();
        params.put("max_id", earliestId);

        Log.d("DEBUG", "GET " + apiUrl);
        client.get(apiUrl, params, handler);
    }

    public void getNewHomeItems(String mostRecentId, AsyncHttpResponseHandler handler){
        String apiUrl = getApiUrl("statuses/home_timeline.json");
        RequestParams params = new RequestParams();
        params.put("since_id", mostRecentId);

        Log.d("DEBUG", "GET " + apiUrl + ", SINCE_ID: " + mostRecentId);
        client.get(apiUrl, params, handler);
    }

    public void getNewMentionItems(String mostRecentId, AsyncHttpResponseHandler handler){
        String apiUrl = getApiUrl("statuses/mentions_timeline.json");
        RequestParams params = new RequestParams();
        params.put("since_id", mostRecentId);

        Log.d("DEBUG", "GET " + apiUrl + ", SINCE_ID: " + mostRecentId);
        client.get(apiUrl, params, handler);
    }

    public void getMyInfo(AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("account/verify_credentials.json");
        client.get(apiUrl, null, handler);
    }

    public void getUserTimeline(AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/user_timeline.json");
        client.get(apiUrl, null, handler);
    }

    public void postNewTweet(String content, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/update.json");
        RequestParams params = new RequestParams();
        params.put("status", content);

        Log.d("DEBUG", "PUT " + apiUrl + ", STATUS: " + content);
        client.post(apiUrl, params, handler);
    }

    public void postReplyTweet(String content, long replyToId, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/update.json");
        RequestParams params = new RequestParams();
        params.put("status", content);
        params.put("in_reply_to_status_id", String.valueOf(replyToId));

        Log.d("DEBUG", "PUT " + apiUrl + ", STATUS: " + content + ", IN_REPLY_TO: " + replyToId);
        client.post(apiUrl, params, handler);
    }

	/* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
	 * 	  i.e getApiUrl("statuses/home_timeline.json");
	 * 2. Define the parameters to pass to the request (query or body)
	 *    i.e RequestParams params = new RequestParams("foo", "bar");
	 * 3. Define the request method and make a call to the client
	 *    i.e client.get(apiUrl, params, handler);
	 *    i.e client.post(apiUrl, params, handler);
	 */
}