package com.codepath.apps.birdfeed.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by jay on 10/17/14.
 */
@Table(name = "User")
public class User extends Model implements Parcelable {
    @Column(name = "name")
    private String name;

    @Column(name = "uid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private long uid;

    @Column(name = "username")
    private String username;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Column(name = "cover_image_url")
    private String coverImageUrl;

    @Column(name = "tagline")
    private String tagline;

    @Column(name = "tweet_count")
    private int tweetCount;

    @Column(name = "follower_count")
    private int followerCount;

    @Column(name = "following_count")
    private int followingCount;

    public User() {
        super();
    }

    public static User fromJSON(JSONObject jsonObject) {
        User user = new User();
        try {
            user.name = jsonObject.getString("name");
            user.uid = jsonObject.getLong("id");
            user.username = jsonObject.getString("screen_name");
            user.profileImageUrl = jsonObject.getString("profile_image_url");
            user.coverImageUrl = jsonObject.getString("profile_background_image_url");
            user.tagline = jsonObject.getString("description");
            user.tweetCount = jsonObject.getInt("statuses_count");
            user.followerCount = jsonObject.getInt("followers_count");
            user.followingCount = jsonObject.getInt("friends_count");
            long result = user.save();
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return user;
    }

    public String getName() {
        return name;
    }

    public long getUid() {
        return uid;
    }

    public String getUsername() {
        return username;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public String getTagline() {
        return tagline;
    }

    public int getTweetCount() {
        return tweetCount;
    }

    public int getFollowerCount() {
        return followerCount;
    }

    public int getFollowingCount() {
        return followingCount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeLong(uid);
        parcel.writeString(username);
        parcel.writeString(profileImageUrl);
        parcel.writeString(coverImageUrl);
    }
}
