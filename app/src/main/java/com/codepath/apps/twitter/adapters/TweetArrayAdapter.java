package com.codepath.apps.twitter.adapters;

import android.content.Context;
import android.media.Image;
import android.text.Html;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.format.DateUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
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
        TextView tvUserName;
        TextView tvUserScreenName;
        TextView tvRelativeTimestamp;
        TextView tvText;
        ImageView ivUserAvatar;
        ImageView ivMedia;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Tweet tweet = getItem(position);

        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_item_tweet, parent, false);
            viewHolder.tvUserName = (TextView) convertView.findViewById(R.id.tvUserName);
            viewHolder.tvUserScreenName = (TextView) convertView.findViewById(R.id.tvUserScreenName);
            viewHolder.tvRelativeTimestamp = (TextView) convertView.findViewById(R.id.tvRelativeTimestamp);
            viewHolder.tvText = (TextView) convertView.findViewById(R.id.tvText);
            viewHolder.ivUserAvatar = (ImageView) convertView.findViewById(R.id.ivUserAvatar);
            viewHolder.ivMedia = (ImageView) convertView.findViewById(R.id.ivMedia);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Picasso.with(getContext()).load(tweet.profileImageUrl)
                .noFade().fit().transform(new RoundedTransformation()).into(viewHolder.ivUserAvatar);

        if (tweet.mediaUrl != null) {
            viewHolder.ivMedia.setVisibility(View.VISIBLE);
            Picasso.with(getContext()).load(tweet.mediaUrl + ":thumb")
                    .noFade().fit().centerCrop().into(viewHolder.ivMedia);
        } else {
            viewHolder.ivMedia.setVisibility(View.GONE);
        }

        viewHolder.tvUserName.setText(tweet.userName);
        viewHolder.tvUserScreenName.setText("@" + tweet.userScreenName);
        viewHolder.tvRelativeTimestamp.setText(getRelativeTimestamp(tweet.createdAt));

        viewHolder.tvText.setText(formatHashtagsAndNames(tweet.text));
        viewHolder.tvText.setMovementMethod(LinkMovementMethod.getInstance());

        return convertView;
    }

    protected String getRelativeTimestamp(Date then) {
        Date now = new Date();
        return DateUtils.getRelativeTimeSpanString(
                then.getTime(), now.getTime(), DateUtils.SECOND_IN_MILLIS)
                .toString().replaceAll("(\\d+)\\s(.).+", "$1$2");
    }

    private Spannable formatHashtagsAndNames(String text) {
        String htmlCommentText = text.replaceAll("([#@][A-Za-z0-9_\\.]+)", "<a>$1</a>");
        htmlCommentText = htmlCommentText.replaceAll("(https?:\\S*)", "<a href=\"$1\">$1</a> ");

        // Prevent underlining of links
        Spannable s = (Spannable) Html.fromHtml(htmlCommentText);
        for (URLSpan u: s.getSpans(0, s.length(), URLSpan.class)) {
            s.setSpan(new UnderlineSpan() {
                public void updateDrawState(TextPaint tp) {
                    tp.setUnderlineText(false);
                }
            }, s.getSpanStart(u), s.getSpanEnd(u), 0);
        }

        return s;
    }

}
