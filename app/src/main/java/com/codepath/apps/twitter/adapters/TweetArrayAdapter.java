package com.codepath.apps.twitter.adapters;

import android.content.Context;
import android.media.Image;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.twitter.R;
import com.codepath.apps.twitter.models.Tweet;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class TweetArrayAdapter extends ArrayAdapter<Tweet> {
    public TweetArrayAdapter(Context context, ArrayList<Tweet> tweets) {
        super(context, 0, tweets);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Tweet tweet = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_item_tweet, parent, false);
        }

        TextView tvUser = (TextView) convertView.findViewById(R.id.tvUser);
        TextView tvText = (TextView) convertView.findViewById(R.id.tvText);
        TextView tvRelativeTimestamp = (TextView) convertView.findViewById(R.id.tvRelativeTimestamp);
        ImageView ivUserAvatar = (ImageView) convertView.findViewById(R.id.ivUserAvatar);

        tvUser.setText(tweet.userName + " @" + tweet.userScreenName);
        tvText.setText(tweet.text);
        tvRelativeTimestamp.setText(getRelativeTimestamp(tweet.createdAt));

        Picasso.with(getContext()).load(tweet.profileImageUrl).noFade().into(ivUserAvatar);

        return convertView;
    }

    protected String getRelativeTimestamp(Date then) {
        Date now = new Date();
        return DateUtils.getRelativeTimeSpanString(
                then.getTime(), now.getTime(), DateUtils.SECOND_IN_MILLIS)
                .toString().replaceAll("(\\d+)\\s(.).+", "$1$2");
    }

}
