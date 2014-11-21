package com.codepath.apps.birdfeed.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.birdfeed.R;
import com.codepath.apps.birdfeed.activities.LoginActivity;
import com.codepath.apps.birdfeed.models.User;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by jay on 10/31/14.
 */
public class ProfileHeaderFragment extends Fragment {
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
}
