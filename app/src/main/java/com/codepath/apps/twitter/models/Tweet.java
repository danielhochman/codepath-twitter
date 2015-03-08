package com.codepath.apps.twitter.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Tweet {

    public Long id;
    public String userName;
    public String userScreenName;
    public String text;
    public Date createdAt;
    public String profileImageUrl;

    public static Tweet fromJson(JSONObject json) {
        Tweet tweet = new Tweet();

        SimpleDateFormat twitterDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy", Locale.US);

        try {
            tweet.id = json.getLong("id");
            tweet.text = json.getString("text");
            try {
                tweet.createdAt = twitterDateFormat.parse(json.getString("created_at"));
            } catch (Exception e) {
                Log.e("TAG", "Could not parse date", e);
            }
            tweet.userName = json.getJSONObject("user").getString("name");
            tweet.userScreenName = json.getJSONObject("user").getString("screen_name");
            tweet.profileImageUrl = json.getJSONObject("user").getString("profile_image_url");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return tweet;
    }

    public static ArrayList<Tweet> fromJson(JSONArray json) {
        ArrayList<Tweet> tweets = new ArrayList<Tweet>(json.length());
        for (int i = 0; i < json.length(); i++) {
            try {
                Tweet tweet = fromJson(json.getJSONObject(i));
                if(tweet != null) {
                    tweets.add(tweet);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
        }
        return tweets;
    }
}
