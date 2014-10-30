package com.codepath.apps.birdfeed.activities;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import com.codepath.apps.birdfeed.R;
import com.codepath.apps.birdfeed.fragments.ComposeTweetFragment;
import com.codepath.apps.birdfeed.fragments.HomeTimelineFragment;
import com.codepath.apps.birdfeed.fragments.MentionsTimelineFragment;
import com.codepath.apps.birdfeed.fragments.SupportFragmentTabListener;
import com.codepath.apps.birdfeed.fragments.TweetListFragment;

public class FeedActivity extends BaseActivity
        implements ComposeTweetFragment.RefreshTimelineListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        setupTabs();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.feed, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onComposeTweet(MenuItem item) {
        Bundle bundle = new Bundle();
        bundle.putString("title", "Compose New Tweet");

        FragmentManager fm = getSupportFragmentManager();
        ComposeTweetFragment composeTweet = ComposeTweetFragment.newInstance("Write a new tweet");
        composeTweet.setArguments(bundle);
        composeTweet.show(fm, "fragment_compose_tweet");
    }

    private void setupTabs() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowTitleEnabled(true);

        ActionBar.Tab homeTimeline = actionBar
                .newTab()
                .setText(R.string.title_home_fragment)
                .setTabListener(new SupportFragmentTabListener<HomeTimelineFragment>(
                        R.id.fragment_timeline,
                        this,
                        "first",
                        HomeTimelineFragment.class));

        actionBar.addTab(homeTimeline);
        actionBar.selectTab(homeTimeline);

        ActionBar.Tab mentionsTimeline = actionBar
                .newTab()
                .setText(R.string.title_mentions_fragment)
                .setTabListener(new SupportFragmentTabListener<MentionsTimelineFragment>(
                        R.id.fragment_timeline,
                        this,
                        "first",
                        MentionsTimelineFragment.class));

        actionBar.addTab(mentionsTimeline);
    }

    @Override
    public void refreshTimeline() {
        TweetListFragment tweetListFragment = (TweetListFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_timeline);
        tweetListFragment.refreshTimeline();
    }
}