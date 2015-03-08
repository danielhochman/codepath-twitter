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
import com.codepath.apps.twitter.transformations.RoundedTransformation;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class TweetArrayAdapter extends ArrayAdapter<Tweet> {
    public TweetArrayAdapter(Context context, ArrayList<Tweet> tweets) {
        super(context, 0, tweets);
    }

    private static class ViewHolder {
        TextView tvUser;
        TextView tvRelativeTimestamp;
        TextView tvText;
        ImageView ivUserAvatar;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Tweet tweet = getItem(position);

        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_item_tweet, parent, false);
            viewHolder.tvUser = (TextView) convertView.findViewById(R.id.tvUser);
            viewHolder.tvRelativeTimestamp = (TextView) convertView.findViewById(R.id.tvRelativeTimestamp);
            viewHolder.tvText = (TextView) convertView.findViewById(R.id.tvText);
            viewHolder.ivUserAvatar = (ImageView) convertView.findViewById(R.id.ivUserAvatar);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Picasso.with(getContext()).load(tweet.profileImageUrl)
                .noFade().fit().transform(new RoundedTransformation()).into(viewHolder.ivUserAvatar);

        viewHolder.tvUser.setText(tweet.userName + " @" + tweet.userScreenName);
        viewHolder.tvRelativeTimestamp.setText(getRelativeTimestamp(tweet.createdAt));
        viewHolder.tvText.setText(tweet.text);

        return convertView;
    }

    protected String getRelativeTimestamp(Date then) {
        Date now = new Date();
        return DateUtils.getRelativeTimeSpanString(
                then.getTime(), now.getTime(), DateUtils.SECOND_IN_MILLIS)
                .toString().replaceAll("(\\d+)\\s(.).+", "$1$2");
    }

}
