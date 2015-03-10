package com.codepath.apps.twitter;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.activeandroid.query.Delete;
import com.codepath.apps.twitter.adapters.TweetArrayAdapter;
import com.codepath.apps.twitter.listeners.EndlessScrollListener;
import com.codepath.apps.twitter.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class TimelineActivity extends ActionBarActivity implements ComposeDialog.ComposeDialogListener {

    private final int REQUEST_CODE = 20;
    private ArrayList<Tweet> tweets;
    private TweetArrayAdapter adapter;
    private SwipeRefreshLayout swipeContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarTimeline);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.drawable.ic_title_logo_default);


        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getTweets(null, tweets.get(0).id);
            }
        });


        tweets = new ArrayList<Tweet>();
        adapter = new TweetArrayAdapter(this, tweets);
        ListView lvTweets = (ListView) findViewById(R.id.lvTweets);
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
                Intent i = new Intent(TimelineActivity.this, DetailActivity.class);
                i.putExtra("tweetId", tweets.get(position).id);
                startActivityForResult(i, REQUEST_CODE);
            }
        });

        tweets.addAll(Tweet.getAll());
        if (tweets.size() == 0) {
            // no previous cache
            getTweets(null, null);
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            Tweet tweet = Tweet.getTweetById(data.getLongExtra("tweetId", -1L));
            tweets.add(0, tweet);
        }
    }

    @Override
    public void onFinishComposeDialog(Tweet tweet) {
        tweets.add(0, tweet);
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
                Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                swipeContainer.setRefreshing(false);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            new Delete().from(Tweet.class).execute();
            TwitterClient client = TwitterApplication.getRestClient();
            client.clearAccessToken();
            finish();
            return true;
        } else if (id == R.id.miCompose) {
            FragmentManager fm = getSupportFragmentManager();
            ComposeDialog composeDialog = ComposeDialog.newInstance(-1L);
            composeDialog.show(fm, "fragment_compose");
        }

        return super.onOptionsItemSelected(item);
    }
}
