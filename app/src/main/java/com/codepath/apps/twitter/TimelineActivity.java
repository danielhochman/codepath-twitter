package com.codepath.apps.twitter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.activeandroid.query.Delete;
import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.twitter.fragments.HomeTimelineFragment;
import com.codepath.apps.twitter.fragments.MentionsTimelineFragment;
import com.codepath.apps.twitter.fragments.TweetsListFragment;
import com.codepath.apps.twitter.models.Tweet;

public class TimelineActivity extends ActionBarActivity implements ComposeDialog.ComposeDialogListener {

    private TweetsListFragment fragmentTweetsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarTimeline);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.drawable.ic_title_logo_default);

        ViewPager vpPager = (ViewPager) findViewById(R.id.viewpager);
        vpPager.setAdapter(new TweetsPagerAdapter(getSupportFragmentManager()));

        PagerSlidingTabStrip pstsTabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        pstsTabs.setViewPager(vpPager);
    }

    /**
     * Reply passed from detail view since it launches the compose dialog fragment
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == TweetsListFragment.REQUEST_CODE) {
            fragmentTweetsList.addTweet(data.getLongExtra("tweetId", -1L));
        }
    }

    /**
     * Compose from toolbar button
     */
    @Override
    public void onFinishComposeDialog(Tweet tweet) {
        fragmentTweetsList.addTweet(tweet);
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

    public class TweetsPagerAdapter extends FragmentPagerAdapter {
        private String tabTitles[] = {"Home", "Mentions"};

        public TweetsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new HomeTimelineFragment();
            } else if (position == 1) {
                return new MentionsTimelineFragment();
            } else {
                return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }
    }
}
