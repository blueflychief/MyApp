package com.infinite.myapp.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.CheckResult;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.annotation.StyleableRes;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.infinite.myapp.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


public class AppBar extends Toolbar {

    private String mTitle = null;
    private int mTitleSize = 20;
    private int mTitleColor = Color.WHITE;
    private static final String TAG = "AppBar";

    private LayoutParams MENU_LP;

    private final Context context;
    private View mLeftView = null;
    private View mRightView = null;
    private View mCenterView = null;

    private static final List<View> MENUS = new ArrayList<>();
    private TextView mToolbarTitle;

    public AppBar(Context context) {
        this(context, null);
    }

    public AppBar(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.toolbarStyle);
    }

    public AppBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        MENU_LP = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, Gravity.CENTER);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AppBar, defStyleAttr, 0);
        final String navBtnGravity = a.getString(R.styleable.AppBar_navigationGravity);
        mTitle = a.getString(R.styleable.AppBar_center_title);
        mTitleSize = a.getDimensionPixelSize(R.styleable.AppBar_center_title_size, mTitleSize);
        mTitleColor = a.getColor(R.styleable.AppBar_center_title_color, mTitleColor);
        final int[] styleableResIds = {
                R.styleable.AppBar_menu_left,
                R.styleable.AppBar_menu_right,
                R.styleable.AppBar_menu_center,
        };
        List<Integer> menuIds = getResIds(styleableResIds, a);
        a.recycle();

        if (!TextUtils.isEmpty(mTitle)) {
            setToolbarTitle(mTitle);
        }

        // 1.set nav button
        ImageButton navButton = getNavButton();
        if (navButton != null) {
            Toolbar.LayoutParams lp = (LayoutParams) navButton.getLayoutParams();
            if (!TextUtils.equals(navBtnGravity, "0")) {
                lp.gravity = Gravity.CENTER_VERTICAL;
            }
            getNavButton().setLayoutParams(lp);
        }

        // 2.set menu views
        for (int i = 0; i < menuIds.size(); i++) {
            int menuId = menuIds.get(i);
            if (menuId == 0) {
                continue;
            }
            if (i == 0) {
                setLeftMenu(context, menuId);
                continue;
            }
            if (i == 1) {
                setRightMenu(context, menuId);
                continue;
            }

            if (i == 2) {
                setCenterMenu(context, menuId);
            }
        }
    }


    /**
     * 设置中间的Menu
     *
     * @param context
     * @param menuId
     */
    public void setCenterMenu(Context context, int menuId) {
        setMenu(context, Gravity.CENTER, menuId);
    }


    /**
     * 设置右边的Menu
     *
     * @param context
     * @param menuId
     */
    public void setRightMenu(Context context, int menuId) {
        setMenu(context, Gravity.RIGHT, menuId);
    }


    /**
     * 设置左边的Menu
     *
     * @param context
     * @param menuId
     */
    public void setLeftMenu(Context context, int menuId) {
        setMenu(context, Gravity.LEFT, menuId);
    }

    private void setMenu(Context context, int gravity, int menuId) {
        ActionBar.LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, gravity | Gravity.CENTER_VERTICAL);
        switch (gravity) {
            case Gravity.LEFT:
                mLeftView = initMenuVIew(context, menuId);
                addView(mLeftView, lp);
                break;
            case Gravity.RIGHT:
                mRightView = initMenuVIew(context, menuId);
                addView(mRightView, lp);
                break;
            case Gravity.CENTER:
                mCenterView = initMenuVIew(context, menuId);
                addView(mCenterView, lp);
                break;
        }
    }

    public String getToolbarTitle() {
        return mTitle;
    }

    //设置居中的Title
    public void setToolbarTitle(int string_id) {
        setToolbarTitle(context.getResources().getString(string_id));
    }

    public void setToolbarTitle(String title) {
        mTitle = title;
        if (mToolbarTitle == null) {
            mToolbarTitle = new TextView(context);
            mToolbarTitle.setTextColor(mTitleColor);
            mToolbarTitle.setTextSize(mTitleSize);
            mToolbarTitle.setGravity(Gravity.CENTER);
            mToolbarTitle.setMaxEms(12);
            mToolbarTitle.setMaxLines(1);
            mToolbarTitle.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            mToolbarTitle.setHorizontallyScrolling(true);
            mToolbarTitle.setFocusableInTouchMode(true);
            TextPaint tp = mToolbarTitle.getPaint();
            tp.setFakeBoldText(true);
            LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, Gravity.CENTER);
            addView(mToolbarTitle, layoutParams);
        }
        mToolbarTitle.setText(title);
    }

    public AppBar addMenu(View v) {
        addView(v, MENU_LP);
        return this;
    }

    /**
     * 通过资源的id添加menu
     *
     * @param menuId [layoutResId,DrawableResId,StringResId]
     */
    public <T extends View> T addMenu(@LayoutRes @DrawableRes @StringRes int menuId) {
        final View menuV = initMenuVIew(context, menuId);
        addView(menuV, MENU_LP);
        return (T) menuV;
    }

    @NonNull
    private View initMenuVIew(Context context, int menuId) {
        final View menuV;
        String str = getResources().getString(menuId);
        if (str.startsWith("res/drawable") || str.startsWith("res/mipmap")) {
            // 是图片
            menuV = new ImageView(context, null, R.attr.toolbarMenuImageStyle);
            ((ImageView) menuV).setImageResource(menuId);
        } else if (str.startsWith("res/layout")) {
            // 是view的布局文件
            menuV = LayoutInflater.from(context).inflate(menuId, null);
        } else {
            // 是文本
            menuV = new TextView(context, null, R.attr.toolbarMenuTextStyle);
            ((TextView) menuV).setText(str);
        }
        return menuV;
    }

    /**
     * @return menu视图的id数组
     */
    private List<Integer> getResIds(@StyleableRes int[] styleResIds, TypedArray a) {
        List<Integer> ids = new ArrayList<>();
        for (int resId : styleResIds) {
            ids.add(a.getResourceId(resId, 0));
        }
        return ids;
    }


    public void canFinishActivity() {
        if (context instanceof Activity) {
            setNavigationOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((Activity) context).finish();
                }
            });
        }
    }

    public
    @CheckResult
    View getLfetMenu() {
        return mLeftView != null ? mLeftView : null;
    }

    public
    @CheckResult
    View getRighMenut() {
        return mRightView != null ? mRightView : null;
    }

    public
    @CheckResult
    View getCenterMenu() {
        return mCenterView != null ? mCenterView : null;
    }


    /**
     * 得到标题按钮
     */
    public
    @CheckResult
    TextView getTitleView() {
        return (TextView) getSubView("mTitleTextView");
    }

    /**
     * 得到子标题
     */
    public
    @CheckResult
    TextView getSubtitleView() {
        return ((TextView) getSubView("mSubtitleTextView"));
    }

    /**
     * 得到左边的导航按钮
     */
    public
    @CheckResult
    ImageButton getNavButton() {
        return (ImageButton) getSubView("mNavButtonView");
    }

    /**
     * 得到logo的视图
     */
    public
    @CheckResult
    ImageView getLogoView() {
        return ((ImageView) getSubView("mLogoView"));
    }

    /**
     * 得到最右边的可折叠按钮视图
     */
    public
    @CheckResult
    ImageButton getCollapseButton() {
        return (ImageButton) getSubView("mCollapseButtonView");
    }

    private View getSubView(String name) {
        Field field;
        try {
            field = Toolbar.class.getDeclaredField(name);
            field.setAccessible(true);
            View v = (View) field.get(this);
            field.setAccessible(false);
            return v;
        } catch (Exception e) {
            Log.e(TAG, "getSubView: 反射错误，请尽快上报给开发者", e);
        }
        return null;
    }

}