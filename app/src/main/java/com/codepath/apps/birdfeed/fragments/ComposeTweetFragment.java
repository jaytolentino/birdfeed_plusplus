package com.codepath.apps.birdfeed.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.codepath.apps.birdfeed.R;
import com.codepath.apps.birdfeed.activities.BaseActivity;
import com.codepath.apps.birdfeed.activities.TweetDetailActivity;
import com.codepath.apps.birdfeed.models.Tweet;
import com.codepath.apps.birdfeed.networking.TwitterApplication;
import com.codepath.apps.birdfeed.networking.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

public class ComposeTweetFragment extends DialogFragment {
    private TwitterClient client;
    private EditText etComposeTweet;
    private Button btnSendTweet;
    private TextView tvCounter;
    private TextWatcher mComposeWatcher;
    private Tweet tweet;
    private RefreshTimelineListener refreshTimelineListener;

    public static ComposeTweetFragment newInstance(String title) {
        ComposeTweetFragment fragment = new ComposeTweetFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        fragment.setArguments(args);
        return fragment;
    }
    public ComposeTweetFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View currentView = inflater.inflate(R.layout.fragment_compose_tweet, container, false);
        setupViews(currentView);
        client = TwitterApplication.getRestClient();

        if (getArguments() != null) {
            String title = getArguments().getString("title");
            getDialog().setTitle(title);
        }
        if (getArguments().containsKey("tweet")) {
            tweet = (Tweet) getArguments().get("tweet");
            etComposeTweet.setText("@" + tweet.getUser().getUsername() + " ");
            int position = etComposeTweet.length();
            Selection.setSelection(etComposeTweet.getText(), position);
            btnSendTweet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sendReply();
                }
            });
        }
        else {
            btnSendTweet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sendTweet();
                }
            });
        }

        return currentView;

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof RefreshTimelineListener) {
            refreshTimelineListener = (RefreshTimelineListener) activity;
        } else {
            throw new ClassCastException(activity.toString()
                    + " must implement ComposeTweetFragment.RefreshTimelineListener");
        }
    }

    private void setupViews(View view) {
        mComposeWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                tvCounter.setText(String.valueOf(140 - charSequence.length()) + " chars left");
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        };
        btnSendTweet = (Button) view.findViewById(R.id.btnSendTweet);
        etComposeTweet = (EditText) view.findViewById(R.id.etComposeTweet);
        tvCounter = (TextView) view.findViewById(R.id.tvCounter);
        etComposeTweet.addTextChangedListener(mComposeWatcher);
    }

    private void sendTweet() {
        String tweetContent = etComposeTweet.getText().toString();
        ((BaseActivity) getActivity()).showProgressBar();

        client.postNewTweet(tweetContent, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                refreshTimelineListener.refreshAfterTweet();
                getDialog().dismiss();
            }

            @Override
            public void onFailure(Throwable throwable, String s) {
                Log.d("debug", throwable.toString());
                Log.d("debug", s);
            }
        });
    }

    private void sendReply() {
        String tweetOnly = etComposeTweet.getText().toString();
        String tweetContent = "@" + tweet.getUser().getUsername() + " " + tweetOnly;
        ((BaseActivity) getActivity()).showProgressBar();

        client.postReplyTweet(tweetContent, tweet.getTweetId(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                ((TweetDetailActivity) getActivity()).toastSuccessfulReply();
                getDialog().dismiss();
            }

            @Override
            public void onFailure(Throwable throwable, String s) {
                Log.d("debug", throwable.toString());
                Log.d("debug", s);
            }
        });
    }

    public interface RefreshTimelineListener {
        public void refreshAfterTweet();
    }
}
