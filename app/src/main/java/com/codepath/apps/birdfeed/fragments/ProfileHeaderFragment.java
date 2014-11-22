package com.codepath.apps.birdfeed.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.birdfeed.R;
import com.codepath.apps.birdfeed.activities.LoginActivity;
import com.codepath.apps.birdfeed.activities.UserListActivity;
import com.codepath.apps.birdfeed.models.User;
import com.codepath.apps.birdfeed.networking.TwitterApplication;
import com.codepath.apps.birdfeed.networking.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by jay on 10/31/14.
 */
public class ProfileHeaderFragment extends Fragment {
    private Context mContext;
    private ProfileListener listener;
    private User currentUser;

    private TextView tvHeaderFullName;
    private TextView tvHeaderUsername;
    private TextView tvHeaderTagline;
    private TextView tvHeaderTweetCount;
    private TextView tvHeaderFollowerCount;
    private TextView tvHeaderFollowingCount;
    private ImageView ivHeaderProfile;
    private ImageView ivHeaderBackground;

    public ProfileHeaderFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof ProfileListener) {
            listener = (ProfileListener) activity;
        } else {
            throw new ClassCastException(activity.toString()
                    + " must implement ProfileHeaderFragment.ProfileListener");
        }
        mContext = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (listener.getUser() != null) {
            currentUser = listener.getUser();
        } else {
            currentUser = LoginActivity.currentUser;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_header, container, false);
        instantiateViews(view);
        populateViews();
        setOnclicks();
        return view;
    }

    private void instantiateViews(View view) {
        tvHeaderFullName = (TextView) view.findViewById(R.id.tvHeaderFullName);
        tvHeaderUsername = (TextView) view.findViewById(R.id.tvHeaderUsername);
        tvHeaderTagline = (TextView) view.findViewById(R.id.tvHeaderTagline);
        tvHeaderTweetCount = (TextView) view.findViewById(R.id.tvHeaderTweetCount);
        tvHeaderFollowerCount = (TextView) view.findViewById(R.id.tvHeaderFollowerCount);
        tvHeaderFollowingCount = (TextView) view.findViewById(R.id.tvHeaderFollowingCount);
        ivHeaderProfile = (ImageView) view.findViewById(R.id.ivHeaderProfile);
        ivHeaderBackground = (ImageView) view.findViewById(R.id.ivHeaderBackground);
    }

    private void populateViews() {
        tvHeaderFullName.setText(currentUser.getName());
        tvHeaderUsername.setText("@" + currentUser.getUsername());
        tvHeaderTagline.setText(currentUser.getTagline());
        tvHeaderTweetCount.setText(currentUser.getPrettyTweetCount());
        tvHeaderFollowerCount.setText(currentUser.getPrettyFollowerCount());
        tvHeaderFollowingCount.setText(currentUser.getPrettyFollowingCount());

        ImageLoader imageLoader = ImageLoader.getInstance();
        ivHeaderProfile.setImageResource(android.R.color.transparent);
        imageLoader.displayImage(currentUser.getProfileImageUrl(), ivHeaderProfile);
        ivHeaderBackground.setImageResource(android.R.color.transparent);
        imageLoader.displayImage(currentUser.getCoverImageUrl(), ivHeaderBackground);
    }

    public interface ProfileListener {
        public User getUser();
    }

    private void setOnclicks() {
        tvHeaderFollowerCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFollowers();
            }
        });
        tvHeaderFollowingCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFollowing();
            }
        });
    }

    private void getFollowers() {
        TwitterApplication.getRestClient().getUsers(
                TwitterClient.USER_TYPE.FOLLOWERS,
                Long.toString(currentUser.getUid()),
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        try {
                            Intent openFollowersList = new Intent(mContext, UserListActivity.class);
                            Bundle extras = new Bundle();
                            extras.putString(UserListActivity.TITLE, getString(R.string.title_followers));
                            extras.putString(UserListActivity.USERS_JSON, jsonObject.getJSONArray("users").toString());

                            openFollowersList.putExtras(extras);
                            mContext.startActivity(openFollowersList);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Throwable throwable, JSONObject jsonObject) {
                        super.onFailure(throwable, jsonObject);
                        Log.d("debug", throwable.getMessage());
                    }
                });
    }

    private void getFollowing(){
        TwitterApplication.getRestClient().getUsers(
                TwitterClient.USER_TYPE.FOLLOWING,
                Long.toString(currentUser.getUid()),
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        try {
                            Intent openFollowersList = new Intent(mContext, UserListActivity.class);
                            Bundle extras = new Bundle();
                            extras.putString(UserListActivity.TITLE, getString(R.string.title_following));
                            extras.putString(UserListActivity.USERS_JSON, jsonObject.getJSONArray("users").toString());

                            openFollowersList.putExtras(extras);
                            mContext.startActivity(openFollowersList);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Throwable throwable, JSONObject jsonObject) {
                        super.onFailure(throwable, jsonObject);
                        Log.d("debug", throwable.getMessage());
                    }
                });
    }
}
