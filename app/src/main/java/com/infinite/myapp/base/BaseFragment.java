package com.infinite.myapp.base;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.infinite.myapp.utils.MyLogger;
import com.infinite.myapp.view.LoadingLayout;

public abstract class BaseFragment extends Fragment {
    private boolean isVisible = false;//当前Fragment是否可见
    private boolean isInitView = false;//是否与View建立起映射关系
    private boolean isFirstLoad = true;//是否是第一次加载数据

    private View convertView;
    private SparseArray<View> mViews;
    protected LoadingLayout mContentPanel = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MyLogger.i("*****onCreateView   " + this.getClass().getSimpleName());
        mContentPanel = new LoadingLayout(getActivity());
        convertView = inflater.inflate(getLayoutId(), container, false);
        mContentPanel.addView(convertView);
        mViews = new SparseArray<>();
        initView();
        isInitView = true;
        lazyLoadData();
        return mContentPanel;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MyLogger.i("*****onViewCreated   " + this.getClass().getSimpleName());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        MyLogger.i("*****onAttach" + "   " + this.getClass().getSimpleName());

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        MyLogger.i("*****setUserVisibleHint " + isVisibleToUser + "   " + this.getClass().getSimpleName());
        if (isVisibleToUser) {
            isVisible = true;
            lazyLoadData();
        } else {
            isVisible = false;
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    private void lazyLoadData() {
        MyLogger.i("*****lazyLoadData*****" + this.getClass().getSimpleName());
        if (mContentPanel == null) {
            return;
        }
        if (isVisible) {
            if (isInitView) {
                if (isFirstLoad) {
                    mContentPanel.showLoading();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            initData();
                            isFirstLoad = false;
                            mContentPanel.showContent();
                            MyLogger.i("*****lazyLoadData--finish first load*****" + this.getClass().getSimpleName());
                        }
                    }, 2000);
                } else {
                    MyLogger.i("*****lazyLoadData--not first load*****" + this.getClass().getSimpleName());
                    mContentPanel.showContent();
                }
            } else {
                MyLogger.i("*****lazyLoadData--not isInitView*****" + this.getClass().getSimpleName());
                mContentPanel.showLoading();
            }
        } else {
            MyLogger.i("*****lazyLoadData--inVisible" + this.getClass().getSimpleName());
            mContentPanel.showEmpty();
        }

        MyLogger.i("*****&&&&&&&&&&&isFirstLoad"+isFirstLoad+"-----" + this.getClass().getSimpleName());
    }

    /**
     * 加载页面布局文件
     *
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 让布局中的view与fragment中的变量建立起映射
     */
    protected abstract void initView();

    /**
     * 加载要显示的数据
     */
    protected abstract void initData();

    /**
     * fragment中可以通过这个方法直接找到需要的view，而不需要进行类型强转
     *
     * @param viewId
     * @param <E>
     * @return
     */
    protected <E extends View> E findView(int viewId) {
        if (convertView != null) {
            E view = (E) mViews.get(viewId);
            if (view == null) {
                view = (E) convertView.findViewById(viewId);
                mViews.put(viewId, view);
            }
            return view;
        }
        return null;
    }
}
