package com.codepath.apps.twitter;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.twitter.R;
import com.codepath.apps.twitter.models.Tweet;
import com.codepath.apps.twitter.transformations.RoundedTransformation;
import com.squareup.picasso.Picasso;

public class DetailActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Long id = getIntent().getLongExtra("tweetId", 0);
        final Tweet tweet = Tweet.getTweetById(id);

        TextView tvText = (TextView) findViewById(R.id.tvDetailText);
        TextView tvRelativeTimestamp = (TextView) findViewById(R.id.tvDetailRelativeTimestamp);
        TextView tvUserName = (TextView) findViewById(R.id.tvDetailUserName);
        TextView tvUserScreenName = (TextView) findViewById(R.id.tvDetailUserScreenName);
        ImageView ivProfile = (ImageView) findViewById(R.id.ivDetailProfile);
        ImageView ivMedia = (ImageView) findViewById(R.id.ivDetailImagePreview);

        tvText.setText(tweet.text);
        tvUserName.setText(tweet.userName);
        tvUserScreenName.setText("@" + tweet.userScreenName);
        tvRelativeTimestamp.setText(tweet.getRelativeTimestamp(false));

        Picasso.with(this).load(tweet.profileImageUrl)
                .noFade().fit().transform(new RoundedTransformation()).into(ivProfile);

        if (tweet.mediaUrl != null) {
            ivMedia.setVisibility(View.VISIBLE);
            Picasso.with(this).load(tweet.mediaUrl)
                    .noFade().into(ivMedia);
            ivMedia.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(DetailActivity.this, FullscreenActivity.class);
                    i.putExtra("mediaUrl", tweet.mediaUrl);
                    startActivity(i);
                }
            });
        } else {
            ivMedia.setVisibility(View.GONE);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
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
