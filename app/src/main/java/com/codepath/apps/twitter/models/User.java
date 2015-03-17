package com.codepath.apps.twitter.models;

import org.json.JSONException;
import org.json.JSONObject;

// TODO: use when caching
public class User {

    public Long id;
    public String userName;
    public String userScreenName;
    public String description;
    public String location;
    public String url;
    public String profileImageUrl;
    public Long followersCount;
    public Long friendsCount;
    public Long statusesCount;
    public Boolean following;
    public Boolean verified;
    public String profileBackgroundImageUrl;

    public static User fromJson(JSONObject json) {
        User user = new User();

        try {
            user.description = json.getString("description");
            user.userName = json.getString("name");
            user.url = json.getString("url");
            user.location = json.getString("location");
            user.userScreenName = json.getString("screen_name");
            user.profileImageUrl = json.getString("profile_image_url");
            user.followersCount = json.getLong("followers_count");
            user.friendsCount = json.getLong("friends_count");
            user.statusesCount = json.getLong("statuses_count");
            user.following = json.getBoolean("following");
            user.profileBackgroundImageUrl = json.getString("profile_background_image_url");
            user.verified = json.getBoolean("verified");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return user;
    }

    /**
     * Taken from stackoverflow
     * @param n the number to format
     * @param iteration in fact this is the class from the array c
     * @return a String representing the number n formatted in a cool looking way.
     */
    public static String numberFormat(double n, int iteration) {
        if (n < 1000) {
            return "" + ((Double) n).intValue();
        }
        char[] c = new char[]{'k', 'm', 'b', 't'};
        double d = ((long) n / 100) / 10.0;
        boolean isRound = (d * 10) %10 == 0;//true if the decimal part is equal to 0 (then it's trimmed anyway)
        return (d < 1000? //this determines the class, i.e. 'k', 'm' etc
                ((d > 99.9 || isRound || (!isRound && d > 9.99)? //this decides whether to trim the decimals
                        (int) d * 10 / 10 : d + "" // (int) d * 10 / 10 drops the decimal
                ) + "" + c[iteration])
                : numberFormat(d, iteration+1));

    }
}
