package com.codepath.apps.birdfeed.activities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Window;

/**
 * Created by jay on 10/29/14.
 */
public class BaseActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
    }

    public void showProgressBar() {
        this.setProgressBarIndeterminateVisibility(true);
    }

    public void hideProgressBar() {
        this.setProgressBarIndeterminateVisibility(false);
    }
    // TODO catches NoInternetConnectivity and displays alert to user?

}
