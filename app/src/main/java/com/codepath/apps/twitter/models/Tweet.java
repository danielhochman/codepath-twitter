package com.codepath.apps.twitter.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Tweet {

    public Long id;
    public String text;
    public String createdAt;

    public static Tweet fromJson(JSONObject json) {
        Tweet tweet = new Tweet();
        try {
            tweet.id = json.getLong("id");
            tweet.text = json.getString("text");
            tweet.createdAt = json.getString("created_at");
//            tweet.user = User.fromJson(json.getJSONObject("user"));
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
