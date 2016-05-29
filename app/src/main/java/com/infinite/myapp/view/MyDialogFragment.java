package com.infinite.myapp.view;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.infinite.myapp.R;


public class MyDialogFragment extends DialogFragment {

    private String mMessage;
    private static final String KEY_MESSAGE = "key_message";

    private TextView mTvMessage;

    public static MyDialogFragment createDialogFragment(String message) {
        MyDialogFragment kkDialogFragment = new MyDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_MESSAGE, message);
        kkDialogFragment.setArguments(bundle);
        return kkDialogFragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMessage = getArguments().getString(KEY_MESSAGE);
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.LoadingDialogStyle);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.view_loading, null);
        mTvMessage = (TextView) view.findViewById(R.id.tv_msg);
        mTvMessage.setText(mMessage);
        builder.setView(view);
        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        int width = (int) (getActivity().getResources().getDisplayMetrics().widthPixels * 0.8);
        getDialog().getWindow().setLayout(width, getDialog().getWindow().getAttributes().height);
    }

    public void setMessage(String message) {
        mTvMessage.setText(message);
    }

}
