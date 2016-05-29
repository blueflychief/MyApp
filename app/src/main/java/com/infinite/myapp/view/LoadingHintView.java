package com.infinite.myapp.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.infinite.myapp.R;

/**
 * Created by Administrator on 2016-05-30.
 */
public class LoadingHintView extends FrameLayout {
    public enum LoadingStatus{
        LOADING,
        LOADING_ERROR,
        NETWORK_ERROR;
    }

    private View mLoadingView;
    private View mLoadingErrorView;
    private View mLoadingNetworkErrorView;
    private Context mContext;

    public LoadingHintView(Context context) {
        this(context, null);
    }


    public LoadingHintView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }


    /**
     * @param type 1、正在加载   2、加载失败      3、网络错误
     * @return
     */
    public View showHintView(LoadingStatus type) {
        switch (type) {
            case LOADING:
                if (mLoadingView != null) {
                    mLoadingView.setVisibility(VISIBLE);
                } else {
                    mLoadingView = View.inflate(mContext, R.layout.view_loading, null);
                    addView(mLoadingView,new LayoutParams(LayoutParams.MATCH_PARENT,
                            LayoutParams.MATCH_PARENT));
                }
                if (mLoadingErrorView!=null) {
                    mLoadingErrorView.setVisibility(INVISIBLE);
                }
                if (mLoadingNetworkErrorView!=null) {
                    mLoadingNetworkErrorView.setVisibility(INVISIBLE);
                }
                return mLoadingView;
            case LOADING_ERROR:
                if (mLoadingErrorView != null) {
                    mLoadingErrorView.setVisibility(VISIBLE);
                } else {
                    mLoadingErrorView = View.inflate(mContext, R.layout.view_loading, null);
                    addView(mLoadingErrorView,new LayoutParams(LayoutParams.MATCH_PARENT,
                            LayoutParams.MATCH_PARENT));
                }
                if (mLoadingView!=null) {
                    mLoadingView.setVisibility(INVISIBLE);
                }
                if (mLoadingNetworkErrorView!=null) {
                    mLoadingNetworkErrorView.setVisibility(INVISIBLE);
                }
                return mLoadingErrorView;
            case NETWORK_ERROR:
                if (mLoadingNetworkErrorView != null) {
                    mLoadingNetworkErrorView.setVisibility(VISIBLE);
                } else {
                    mLoadingNetworkErrorView = View.inflate(mContext, R.layout.view_loading, null);
                    addView(mLoadingNetworkErrorView,new LayoutParams(LayoutParams.MATCH_PARENT,
                            LayoutParams.MATCH_PARENT));
                }
                if (mLoadingView!=null) {
                    mLoadingView.setVisibility(INVISIBLE);
                }
                if (mLoadingErrorView!=null) {
                    mLoadingErrorView.setVisibility(INVISIBLE);
                }
                return mLoadingNetworkErrorView;
        }
        return null;
    }


    public void hideAllHintView() {
        if (mLoadingView!=null) {
            mLoadingView.setVisibility(INVISIBLE);
        }
        if (mLoadingErrorView!=null) {
            mLoadingErrorView.setVisibility(INVISIBLE);
        }
        if (mLoadingNetworkErrorView!=null) {
            mLoadingNetworkErrorView.setVisibility(INVISIBLE);
        }
    }
}
