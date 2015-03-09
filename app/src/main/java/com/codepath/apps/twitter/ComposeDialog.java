package com.codepath.apps.twitter;

import android.content.DialogInterface;
import android.graphics.ComposePathEffect;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.twitter.models.Tweet;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

public class ComposeDialog extends DialogFragment implements DialogInterface.OnClickListener {

    private EditText mEditText;

    public ComposeDialog() {
        // Empty constructor required for DialogFragment
    }

    public static ComposeDialog newInstance() {
        ComposeDialog frag = new ComposeDialog();
        Bundle args = new Bundle();
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

    }

    public interface ComposeDialogListener {
        void onFinishComposeDialog(Tweet tweet);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_compose, container);

        mEditText = (EditText) view.findViewById(R.id.etTweet);

        final TextView tvCharCount = (TextView) view.findViewById(R.id.tvCharCount);
        tvCharCount.setText("140");

        ImageButton btnSend = (ImageButton) view.findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TwitterApplication.getRestClient().submitNewTweet(mEditText.getText().toString(), new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                        Tweet postedTweet = Tweet.fromJson(json);

                        ComposeDialogListener listener = (ComposeDialogListener) getActivity();
                        listener.onFinishComposeDialog(postedTweet);

                        getDialog().dismiss();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Log.e("TAG", "Tweet failed!", throwable);
                        Toast.makeText(getDialog().getContext(), "Tweet failed! " + statusCode, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int remaining = 140 - s.length();
                if (remaining >= 0) {
                    tvCharCount.setTextColor(getResources().getColor(R.color.grey));
                } else {
                    tvCharCount.setTextColor(getResources().getColor(R.color.red));
                }
                tvCharCount.setText("" + remaining);
            }
        });

        // Show soft keyboard automatically
        mEditText.requestFocus();
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        return view;
    }
}
