package com.codepath.apps.twitter.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.apps.twitter.DetailActivity;
import com.codepath.apps.twitter.R;
import com.codepath.apps.twitter.TimelineActivity;
import com.codepath.apps.twitter.TwitterApplication;
import com.codepath.apps.twitter.adapters.TweetArrayAdapter;
import com.codepath.apps.twitter.listeners.EndlessScrollListener;
import com.codepath.apps.twitter.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class TweetsListFragment extends Fragment {

    public final static int REQUEST_CODE = 20;

    private ArrayList<Tweet> tweets;
    private TweetArrayAdapter adapter;
    private SwipeRefreshLayout swipeContainer;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tweets_list, container, false);

        ListView lvTweets = (ListView) v.findViewById(R.id.lvTweets);
        lvTweets.setAdapter(adapter);
        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                getTweets(tweets.get(totalItemsCount - 1).id, null);
            }
        });
        lvTweets.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getActivity(), DetailActivity.class);
                i.putExtra("tweetId", tweets.get(position).id);
                startActivityForResult(i, REQUEST_CODE);
            }
        });

        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getTweets(null, tweets.get(0).id);
            }
        });

        return v;
    }

    public void addTweet(Tweet tweet) {
        tweets.add(0, tweet);
    }

    public void addTweet(Long id) {
        Tweet tweet = Tweet.getTweetById(id);
        addTweet(tweet);
    }

    protected void getTweets(Long maxId, final Long sinceId) {

        TwitterApplication.getRestClient().getHomeTimeline(maxId, sinceId, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                ArrayList<Tweet> tweetsFromJson = Tweet.fromJson(response);
                if (sinceId != null) {
                    tweets.addAll(0, tweetsFromJson);
                } else {
                    tweets.addAll(tweetsFromJson);
                }
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tweets = new ArrayList<Tweet>();
        adapter = new TweetArrayAdapter(getActivity(), tweets);

        tweets.addAll(Tweet.getAll());
        if (tweets.size() == 0) {
            // no previous cache
            getTweets(null, null);
        }
    }
}
