package com.infinite.myapp.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.infinite.myapp.R;
import com.infinite.myapp.utils.MyClickListener;
import com.infinite.myapp.utils.MyLogger;

/**
 * Created by Administrator on 2016-05-30.
 */
public class LoadingLayout extends FrameLayout {
    private int emptyView, dataErrorView, loadingView, loadingNetworkErrorView;
    private RetryListener onRetryClickListener;

    public LoadingLayout(Context context) {
        this(context, null);
    }

    public LoadingLayout(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public LoadingLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.LoadingLayout, 0, 0);
        try {
            loadingView = a.getResourceId(R.styleable.LoadingLayout_loadingView, R.layout.view_loading_data);
            dataErrorView = a.getResourceId(R.styleable.LoadingLayout_errorView, R.layout.view_loading_error);
            loadingNetworkErrorView = a.getResourceId(R.styleable.LoadingLayout_networkErrorView, R.layout.view_network_error);
            emptyView = a.getResourceId(R.styleable.LoadingLayout_emptyView, R.layout.view_loading_empty);
            LayoutInflater inflater = LayoutInflater.from(getContext());
            inflater.inflate(loadingView, this, true);
            inflater.inflate(dataErrorView, this, true);
            inflater.inflate(loadingNetworkErrorView, this, true);
            inflater.inflate(emptyView, this, true);
        } finally {
            a.recycle();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initAllViews();
    }

    private void initAllViews() {
        for (int i = 0; i < getChildCount() - 1; i++) {
            getChildAt(i).setVisibility(GONE);
        }

        findViewById(R.id.iv_retry).setOnClickListener(new MyClickListener() {
            @Override
            protected void onNotFastClick(View v) {
                if (null != onRetryClickListener) {
                    onRetryClickListener.onRetryClick(v);
                }
            }
        });


        findViewById(R.id.iv_check_network).setOnClickListener(new MyClickListener() {
            @Override
            protected void onNotFastClick(View v) {
                if (null != onRetryClickListener) {
                    onRetryClickListener.onCheckNetworkClick(v);
                }
            }
        });
    }

    public void setOnRetryClickListener(RetryListener onRetryClickListener) {
        this.onRetryClickListener = onRetryClickListener;
    }

    public void showLoading() {
        showView(0);
        MyLogger.i("------showloading");
    }

    public void showLoadingError() {
        showView(1);
        MyLogger.i("------showLoadingError");
    }

    public void showNetworkError() {
        showView(2);
        MyLogger.i("------showNetworkError");
    }

    public void showEmpty() {
        showView(3);
        MyLogger.i("------showEmpty");
    }

    public void showContent() {
        showView(4);
        MyLogger.i("------showContent");
    }

    private void showView(int type) {
        for (int i = 0; i < this.getChildCount(); i++) {
            View child = this.getChildAt(i);
            if (i == type) {
                child.setVisibility(VISIBLE);
            } else {
                child.setVisibility(GONE);
            }
        }
    }
}
