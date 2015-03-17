package com.codepath.apps.twitter;

import android.media.Image;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.twitter.R;
import com.codepath.apps.twitter.fragments.UserTimelineFragment;
import com.codepath.apps.twitter.models.User;
import com.codepath.apps.twitter.transformations.RoundedTransformation;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONObject;

public class ProfileActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        String screenName = getIntent().getStringExtra("screenName");

        final TextView tvUserScreenName = (TextView) findViewById(R.id.tvProfileUserScreenName);
        final TextView tvUserName = (TextView) findViewById(R.id.tvProfileUserName);
        final TextView tvProfileDescription = (TextView) findViewById(R.id.tvProfileDescription);
        final TextView tvTweetCount = (TextView) findViewById(R.id.tvTweetCount);
        final TextView tvFollowersCount = (TextView) findViewById(R.id.tvFollowers);
        final TextView tvFollowingCount = (TextView) findViewById(R.id.tvFollowing);
        final ImageView profileImage = (ImageView) findViewById(R.id.ivProfileAvatar);


        TwitterApplication.getRestClient().getUser(screenName, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                User user = User.fromJson(response);
                tvUserName.setText(user.userName);
                tvUserScreenName.setText("@" + user.userScreenName);
                tvProfileDescription.setText(user.description);
                tvTweetCount.setText("Tweets\n" + User.numberFormat(user.statusesCount, 0));
                tvFollowersCount.setText("Followers\n" + User.numberFormat(user.followersCount, 0));
                tvFollowingCount.setText("Following\n" + User.numberFormat(user.friendsCount, 0));


                Picasso.with(getApplicationContext()).load(user.profileImageUrl)
                        .noFade().fit().transform(new RoundedTransformation()).into(profileImage);
            }
        });

        if (savedInstanceState == null) {
            UserTimelineFragment fragmentUserTimeline = UserTimelineFragment.newInstance(screenName);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flContainer, fragmentUserTimeline);
            ft.commit();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
