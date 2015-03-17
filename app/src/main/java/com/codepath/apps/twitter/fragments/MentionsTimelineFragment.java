package com.codepath.apps.twitter.fragments;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.codepath.apps.twitter.TwitterApplication;
import com.codepath.apps.twitter.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MentionsTimelineFragment extends TweetsListFragment {

    private final String tweetContext = "mention";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tweets.addAll(Tweet.getAllForContext(tweetContext));
        if (tweets.size() == 0) {
            // no previous cache
            getTweets(null, null);
        }
    }

    protected void getTweets(Long maxId, final Long sinceId) {

        TwitterApplication.getRestClient().getMentionsTimeline(maxId, sinceId, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                ArrayList<Tweet> tweetsFromJson = Tweet.fromJson(response);
                if (sinceId != null) {
                    tweets.addAll(0, tweetsFromJson);
                } else {
                    tweets.addAll(tweetsFromJson);
                }

                for (Tweet tweet : tweets) {
                    tweet.context = tweetContext;
                }
                Tweet.saveTweets(tweets);
                adapter.notifyDataSetChanged();
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e("TAG", "failure", throwable);
                Log.e("TAG", errorResponse.toString());
                Toast.makeText(getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                swipeContainer.setRefreshing(false);
            }
        });
    }
}
