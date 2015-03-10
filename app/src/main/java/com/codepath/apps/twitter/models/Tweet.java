package com.codepath.apps.twitter.models;

import android.text.format.DateUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name = "Tweets", id = "_id")
public class Tweet extends Model {

    @Column(name = "id", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public Long id;
    @Column(name = "userName")
    public String userName;
    @Column(name = "userScreenName")
    public String userScreenName;
    @Column(name = "text")
    public String text;
    @Column(name = "createdAt")
    public Date createdAt;
    @Column(name = "profileImageUrl")
    public String profileImageUrl;
    @Column(name = "mediaUrl")
    public String mediaUrl;

    public Tweet() {
        super();
    }

    public Tweet(Long id, String userName, String userScreenName, String text, Date createdAt, String profileImageUrl, String mediaUrl){
        super();
        this.id = id;
        this.userName = userName;
        this.userScreenName = userScreenName;
        this.text = text;
        this.createdAt = createdAt;
        this.profileImageUrl = profileImageUrl;
        this.mediaUrl = mediaUrl;
    }

    public static List<Tweet> getAll() {
        return new Select()
                .from(Tweet.class)
                .orderBy("id DESC")
                .limit(1000)
                .execute();
    }

    public static Tweet getTweetById(Long id) {
        return new Select()
                .from(Tweet.class)
                .where("id = ?", id)
                .executeSingle();
    }

    public String getRelativeTimestamp(boolean abbreviated) {
        Date now = new Date();
        String relativeTimestamp = DateUtils.getRelativeTimeSpanString(
                this.createdAt.getTime(), now.getTime(), DateUtils.SECOND_IN_MILLIS).toString();

        if (abbreviated) {
            return relativeTimestamp.replaceAll("(\\d+)\\s(.).+", "$1$2");
        } else {
            return relativeTimestamp;
        }
    }



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

            try {
                tweet.mediaUrl = json.getJSONObject("entities").getJSONArray("media").getJSONObject(0).getString("media_url");
            } catch (JSONException e) {
                // nothing
            }

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
                if (tweet != null) {
                    tweets.add(tweet);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
        }

        ActiveAndroid.beginTransaction();
        try {
            for (Tweet tweet : tweets) {
                tweet.save();
            }
            ActiveAndroid.setTransactionSuccessful();
        } finally {
            ActiveAndroid.endTransaction();
        }

        return tweets;
    }
}
