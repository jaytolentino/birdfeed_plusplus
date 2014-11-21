package com.codepath.apps.birdfeed.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codepath.apps.birdfeed.R;
import com.codepath.apps.birdfeed.activities.ProfileActivity;
import com.codepath.apps.birdfeed.models.Tweet;
import com.codepath.apps.birdfeed.models.User;
import com.codepath.apps.birdfeed.networking.TwitterApplication;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by jay on 10/17/14.
 */
public class TweetsAdapter extends ArrayAdapter<Tweet> {
    private Tweet mTweet;
    private Context mContext;

    public TweetsAdapter(Context context, List<Tweet> tweets) {
        super(context, 0, tweets);
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        mTweet = getItem(position);
        TweetViewHolder holder;
        if (convertView == null) {
            holder = new TweetViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_tweet, parent, false);
            initializeViews(holder, convertView);
            convertView.setTag(holder);
        }
        else {
            holder = (TweetViewHolder) convertView.getTag();
        }
        setViewContent(holder);
        setProfileOnClick(holder);
        return convertView;
    }

    private void initializeViews(TweetViewHolder holder, View convertView) {
//        holder.ivCoverImage = (ImageView) convertView.findViewById(R.id.ivCoverImage);
        holder.ivProfileImage = (ImageView) convertView.findViewById(R.id.ivProfileImg);
        holder.ivMedia = (ImageView) convertView.findViewById(R.id.ivMedia);

        holder.tvUsername = (TextView) convertView.findViewById(R.id.tvUsername);
        holder.tvFullName = (TextView) convertView.findViewById(R.id.tvFullName);
        holder.tvBody = (TextView) convertView.findViewById(R.id.tvBody);
        holder.tvTimestamp = (TextView) convertView.findViewById(R.id.tvTimestamp);
    }

    private void setViewContent(TweetViewHolder holder) {
        setImageViews(holder);

        holder.tvFullName.setText(mTweet.getUser().getName());

        holder.tvUsername.setText("@" + mTweet.getUser().getUsername());
        holder.tvUsername.setTextColor(getContext().getResources().getColor(R.color.gray_font));

        holder.tvBody.setText(mTweet.getBody());

        holder.tvTimestamp.setText(mTweet.getRelativeTimetamp());
        holder.tvTimestamp.setTextColor(Color.LTGRAY);
    }

    private void setImageViews(TweetViewHolder holder) {
        ImageLoader imageLoader = ImageLoader.getInstance();
        holder.ivProfileImage.setImageResource(android.R.color.transparent);
        imageLoader.displayImage(mTweet.getUser().getProfileImageUrl(), holder.ivProfileImage);

//        holder.ivCoverImage.setImageResource(android.R.color.transparent);
//        imageLoader.displayImage(mTweet.getUser().getCoverImageUrl(), holder.ivCoverImage);

        if (mTweet.hasMedia()) {
            holder.ivMedia.setVisibility(View.VISIBLE);
            holder.ivMedia.setImageResource(android.R.color.transparent);
            imageLoader.displayImage(mTweet.getMediaUrl(), holder.ivMedia);
        } else {
            holder.ivMedia.setVisibility(View.GONE);
        }
    }

    private void setProfileOnClick(TweetViewHolder holder) {
        final String uid = Long.toString(mTweet.getUser().getUid());
        View.OnClickListener clickToProfile = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getProfileInfo(uid);
            }
        };
        holder.tvFullName.setOnClickListener(clickToProfile);
        holder.tvUsername.setOnClickListener(clickToProfile);
        holder.ivProfileImage.setOnClickListener(clickToProfile);
    }

    private class TweetViewHolder {
//        public ImageView ivCoverImage;
        public ImageView ivProfileImage;
        public ImageView ivMedia;
        public TextView tvFullName;
        public TextView tvUsername;
        public TextView tvBody;
        public TextView tvTimestamp;
    }

    public Tweet getLastTweet() {
        return getItem(getCount() - 1);
    }

    private void getProfileInfo(String uid) {
        ArrayList<String> userIds = new ArrayList<String>();
        userIds.add(uid);
        TwitterApplication.getRestClient().getUserInfo(userIds, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONArray jsonArray) {
                try {
                    JSONObject userInfo = (JSONObject) jsonArray.get(0);
                    User user = User.fromJSON(userInfo);
                    Intent profileIntent = new Intent(mContext, ProfileActivity.class);
                    profileIntent.putExtra(ProfileActivity.USER, user);
                    mContext.startActivity(profileIntent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable throwable, JSONArray jsonArray) {
                Log.d("debug", throwable.getMessage());
            }
        });
    }

    private void getProfileBanner(String uid, User user) {

    }
}
