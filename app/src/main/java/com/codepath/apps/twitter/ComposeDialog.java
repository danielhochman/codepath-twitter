package com.codepath.apps.twitter;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class ComposeDialog extends DialogFragment {

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_compose, container);

        final TextView tvCharCount = (TextView) view.findViewById(R.id.tvCharCount);
        tvCharCount.setText("140");

        ImageButton btnSend = (ImageButton) view.findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "submitting!", Toast.LENGTH_SHORT).show();

                TwitterApplication.getRestClient().
            }
        });

        mEditText = (EditText) view.findViewById(R.id.txt_your_name);
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tvCharCount.setText("" + (140 - s.length()));
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
