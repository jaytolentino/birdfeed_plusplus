package com.codepath.apps.birdfeed.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.birdfeed.R;
import com.codepath.apps.birdfeed.fragments.ProfileHeaderFragment;
import com.codepath.apps.birdfeed.models.User;


public class ProfileActivity extends BaseActivity
        implements ProfileHeaderFragment.ProfileListener {

    public static final String USER = "user";

    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().hasExtra(USER)){
            mUser = (User) getIntent().getSerializableExtra(USER);
            setTitle(getString(R.string.title_profile));
        }
        setContentView(R.layout.activity_profile);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.profile, menu);
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

    @Override
    public User getUser() {
        return mUser;
    }
}
