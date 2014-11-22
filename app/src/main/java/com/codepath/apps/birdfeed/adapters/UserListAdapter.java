package com.codepath.apps.birdfeed.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.birdfeed.R;
import com.codepath.apps.birdfeed.models.User;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by jay on 11/21/14.
 */
public class UserListAdapter extends ArrayAdapter<User> {
    private Context mContext;

    public UserListAdapter(Context context, List<User> users) {
        super(context, 0, users);
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        User user = getItem(position);
        UserViewHolder holder;
        if (convertView == null) {
            holder = new UserViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_user, parent, false);
            initializeViews(holder, convertView);
            convertView.setTag(holder);
        }
        else {
            holder = (UserViewHolder) convertView.getTag();
        }
        setViewContent(user, holder);
        return convertView;
    }

    private class UserViewHolder {
        ImageView ivProfileImage;
//        TextView tvFullName;
//        TextView tvUsername;
    }

    private void initializeViews(UserViewHolder holder, View view) {
        holder.ivProfileImage = (ImageView) view.findViewById(R.id.ivProfileImage);
//        holder.tvFullName = (TextView) view.findViewById(R.id.tvFullName);
//        holder.tvUsername = (TextView) view.findViewById(R.id.tvUsername);
    }

    private void setViewContent(User user, UserViewHolder holder) {
        ImageLoader imageLoader = ImageLoader.getInstance();
        holder.ivProfileImage.setImageResource(android.R.color.transparent);
        String largerImageUrl = user.getProfileImageUrl().replace("_normal", "_bigger");
        imageLoader.displayImage(largerImageUrl, holder.ivProfileImage);
//        holder.tvUsername.setText("@" + user.getUsername());
//        holder.tvFullName.setText(user.getName());
    }
}
