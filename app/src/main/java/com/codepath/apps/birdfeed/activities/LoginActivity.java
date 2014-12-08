package com.codepath.apps.birdfeed.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.codepath.apps.birdfeed.R;
import com.codepath.apps.birdfeed.models.User;
import com.codepath.apps.birdfeed.networking.TwitterClient;
import com.codepath.oauth.OAuthLoginActivity;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

public class LoginActivity extends OAuthLoginActivity<TwitterClient> {
    public static User currentUser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	}

	// Inflate the menu; this adds items to the action bar if it is present.
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	// OAuth authenticated successfully, launch primary authenticated activity
	// i.e Display application "homepage"
	@Override
	public void onLoginSuccess() {
        loadProfileInfo();
		Intent i = new Intent(this, FeedActivity.class);
		startActivity(i);
	}

	// OAuth authentication flow failed, handle the error
	// i.e Display an error dialog or toast
	@Override
	public void onLoginFailure(Exception e) {
		e.printStackTrace();
	}

	// Click handler method for the button used to start OAuth flow
	// Uses the client to initiate OAuth authorization
	// This should be tied to a button used to login
	public void loginToRest(View view) {
		getClient().connect();
	}

    private void loadProfileInfo() {
        getClient().getMyInfo(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                currentUser = User.fromJSON(jsonObject);
                getActionBar().setTitle("@" + currentUser.getUsername());
            }
        });
    }

}
