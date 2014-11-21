package com.codepath.apps.birdfeed.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Window;

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
