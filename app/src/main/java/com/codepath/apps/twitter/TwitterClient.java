package com.codepath.apps.twitter;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class;
	public static final String REST_URL = "https://api.twitter.com/1.1";
    // must match manifest
	public static final String REST_CALLBACK_URL = "oauth://twitterclient";

	public TwitterClient(Context context) {
		super(context,
                REST_API_CLASS, REST_URL,
                context.getResources().getString(R.string.rest_consumer_key),
                context.getResources().getString(R.string.rest_consumer_secret),
                REST_CALLBACK_URL);
	}


    public void getHomeTimeline(Long maxId, Long sinceId, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/home_timeline.json");
        RequestParams params = new RequestParams();
        if (maxId != null) {
            params.put("max_id", maxId);
        }
        if (sinceId != null) {
            params.put("since_id", sinceId);
        }
        params.put("count", 25);
        client.get(apiUrl, params, handler);
    }

    public void submitNewTweet(String text, Long replyId, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/update.json");
        RequestParams params = new RequestParams();
        params.put("status", text);

//        if (replyId != null) {
//            params.put("in_reply_to_status_id", replyId);
//        }
        client.post(apiUrl, params, handler);
    }

    public void getMentionsTimeline(Long maxId, Long sinceId, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/mentions_timeline.json");
        RequestParams params = new RequestParams();
        if (maxId != null) {
            params.put("max_id", maxId);
        }
        if (sinceId != null) {
            params.put("since_id", sinceId);
        }
        params.put("count", 25);
        client.get(apiUrl, params, handler);
    }

    public void getUserTimeline(String screenName, Long maxId, Long sinceId, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/user_timeline.json");
        RequestParams params = new RequestParams();
        if (maxId != null) {
            params.put("max_id", maxId);
        }
        if (sinceId != null) {
            params.put("since_id", sinceId);
        }
        if (screenName != null) {
            params.put("screen_name", screenName);
        }
        params.put("count", 25);
        client.get(apiUrl, params, handler);
    }
}
