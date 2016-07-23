package com.infinite.myapp.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.infinite.myapp.R;

/**
 * 模仿微信的底部TAB
 * 可以设置消息红点和消息文字
 * 滑动时可设置TAB图标的透明度
 */
public class TabNavigationButton extends View {

    private static final String TAG = "TabNavigationButton";
    private static final String INSTANCE_STATE = "instance_state";
    private static final String STATE_ALPHA = "state_alpha";
    private static final String STATE_MESSAGE = "state_message";
    private static final String STATE_MESSAGE_MODE = "state_message_mode";

    private Bitmap mBitmap;
    private Canvas mIconCanvas;
    private Paint mIconPaint;

    //Icon颜色
    private int mIconBgColor = 0xFF45C01A;
    //tab文字颜色
    private int mTabTextBgColor = 0xFF000000;
    //Icon透明度 0.0-1.0
    private float mIconBgAlpha = 0f;
    //Icon图标
    private Bitmap mIconBitmap;
    //限制绘制icon的范围
    private Rect mIconRect;
    //icon底部文本
    private String mText = "文字";
    private int mTabTextSize = (int) TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics());
    private Paint mTabTextPaint;
    private Rect mTabTextBound = new Rect();

    //红点消息
    private String mMessage = "88";
    private int mMsgTextSize = (int) TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics());

    private int mRedDotSize = (int) TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics());
    private Paint mLinePaint;
    private Rect mMessageTextBound = new Rect();
    private Paint mMsgTextPaint;
    private float mMsgTextWidth;
    private float mMsgDotRadius;
    private int mMsgMode = 1;

    public TabNavigationButton(Context context) {
        super(context);
    }

    public TabNavigationButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.TabNavigationButton);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.TabNavigationButton_nav_icon:
                    BitmapDrawable drawable = (BitmapDrawable) a.getDrawable(attr);
                    mIconBitmap = drawable.getBitmap();
                    break;
                case R.styleable.TabNavigationButton_nav_color:
                    mIconBgColor = a.getColor(attr, mIconBgColor);
                    break;
                case R.styleable.TabNavigationButton_nav_text_color:
                    mTabTextBgColor = a.getColor(attr, mIconBgColor);
                    break;
                case R.styleable.TabNavigationButton_nav_text:
                    mText = a.getString(attr);
                    break;
                case R.styleable.TabNavigationButton_nav_msg_text:
                    mMessage = a.getString(attr);
                    break;
                case R.styleable.TabNavigationButton_nav_msg_text_size:
                    mMsgTextSize = (int) a.getDimension(attr, TypedValue
                            .applyDimension(TypedValue.COMPLEX_UNIT_SP, 10,
                                    getResources().getDisplayMetrics()));
                    break;
                case R.styleable.TabNavigationButton_nav_text_size:
                    mTabTextSize = (int) a.getDimension(attr, TypedValue
                            .applyDimension(TypedValue.COMPLEX_UNIT_SP, 10,
                                    getResources().getDisplayMetrics()));
                    break;
                case R.styleable.TabNavigationButton_nav_red_dot_radius:
                    mRedDotSize = (int) a.getDimension(attr, TypedValue
                            .applyDimension(TypedValue.COMPLEX_UNIT_SP, 6,
                                    getResources().getDisplayMetrics()));
                    break;
                case R.styleable.TabNavigationButton_nav_msg_mode:
                    mMsgMode = a.getInt(attr, 1);
                    break;

            }
        }
        a.recycle();

        mTabTextPaint = new Paint();
        mTabTextPaint.setTextSize(mTabTextSize);
        mTabTextPaint.setColor(mTabTextBgColor);
        mTabTextPaint.setAntiAlias(true);
        // 得到text绘制范围
        mTabTextPaint.getTextBounds(mText, 0, mText.length(), mTabTextBound);

        mMsgTextPaint = new Paint();
        mMsgTextPaint.setColor(Color.WHITE);
        mMsgTextPaint.setTextSize(mMsgTextSize);
        mMsgTextPaint.setAntiAlias(true);
        mMsgTextPaint.setDither(true);
        mMsgTextPaint.getTextBounds(mMessage, 0, mMessage.length(), mMessageTextBound);

        mLinePaint = new Paint();
        mLinePaint.setTextSize(mTabTextSize);
        mLinePaint.setColor(Color.BLACK);
        mLinePaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // 得到绘制icon的宽
        int bitmapWidth = Math.min(getMeasuredWidth() - getPaddingLeft()
                - getPaddingRight(), getMeasuredHeight() - getPaddingTop()
                - getPaddingBottom() - mTabTextBound.height());

        int left = getMeasuredWidth() / 2 - bitmapWidth / 2;
        int top = (getMeasuredHeight() - mTabTextBound.height()) / 2 - bitmapWidth
                / 2;
        // 设置icon的绘制范围
        mIconRect = new Rect(left, top, left + bitmapWidth, top + bitmapWidth);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        int alpha = (int) Math.ceil((255 * mIconBgAlpha));
        canvas.drawBitmap(mIconBitmap, null, mIconRect, null);
        setupTargetBitmap(alpha);
        drawSourceText(canvas, alpha);
        drawTargetText(canvas, alpha);
        canvas.drawBitmap(mBitmap, 0, 0, null);
        drawMessageText(canvas);
    }


    //绘制红点文字
    private void drawMessageText(Canvas canvas) {
        //获取文字的宽度
        mMsgTextWidth = mMsgTextPaint.measureText(mMessage);
        Log.i(TAG, "----mMsgTextWidth:" + mMsgTextWidth);
        drawMessageDot(mMsgMode, canvas);
        if (mMsgMode == 1) {
            canvas.drawText(mMessage, mIconRect.right - mMsgTextWidth / 2, mIconRect.top + mMsgDotRadius + mMessageTextBound.height() / 2, mMsgTextPaint);
        }
    }

    //绘制红点背景
    private void drawMessageDot(int mode, Canvas canvas) {
        Paint p = new Paint();
        p.setColor(Color.RED);
        p.setAntiAlias(true);
        p.setDither(true);
        if (mode == 1) {
            if (mMessage.length() == 1) {
                mMsgDotRadius = mMsgTextWidth * 1.4f;
            } else {
                mMsgDotRadius = mMsgTextWidth * 0.7f;
            }
        } else {
            mMsgDotRadius = mRedDotSize * 1.0f / 2;
        }
        canvas.drawCircle(mIconRect.right, mIconRect.top + mMsgDotRadius, mMsgDotRadius, p);
    }

    //设置红点消息文字
    public void setmMessageText(String s) {
        mMessage = s;
        invalidateView();
    }

    //移除红点消息文字
    public void removeMessage() {
        setmMessageText("");
    }


    private void setupTargetBitmap(int alpha) {
        mBitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Config.ARGB_8888);
        mIconPaint = new Paint();
        mIconPaint.setAntiAlias(true);
        mIconPaint.setDither(true);
        mIconCanvas = new Canvas(mBitmap);
        mIconPaint.setColor(mIconBgColor);
        mIconPaint.setAlpha(alpha);
        mIconCanvas.drawRect(mIconRect, mIconPaint);
        mIconPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        mIconPaint.setAlpha(255);
        mIconCanvas.drawBitmap(mIconBitmap, null, mIconRect, mIconPaint);
    }

    private void drawSourceText(Canvas canvas, int alpha) {
        mTabTextPaint.setTextSize(mTabTextSize);
        mTabTextPaint.setColor(mTabTextBgColor);
        mTabTextPaint.setAlpha(255 - alpha);
        canvas.drawText(mText, mIconRect.left + mIconRect.width() / 2
                        - mTabTextBound.width() / 2,
                mIconRect.bottom + mTabTextBound.height(), mTabTextPaint);
    }

    private void drawTargetText(Canvas canvas, int alpha) {
        mTabTextPaint.setColor(mIconBgColor);
        mTabTextPaint.setAlpha(alpha);
        canvas.drawText(mText, mIconRect.left + mIconRect.width() / 2
                        - mTabTextBound.width() / 2,
                mIconRect.bottom + mTabTextBound.height(), mTabTextPaint);

    }


    //设置Icon的透明度
    public void setIconAlpha(float alpha) {
        this.mIconBgAlpha = alpha;
        invalidateView();
    }

    private void invalidateView() {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            invalidate();
        } else {
            postInvalidate();
        }
    }

    public void setIconColor(int color) {
        mIconBgColor = color;
    }

    public void setIcon(int resId) {
        this.mIconBitmap = BitmapFactory.decodeResource(getResources(), resId);
        if (mIconRect != null)
            invalidateView();
    }

    public void setIcon(Bitmap iconBitmap) {
        this.mIconBitmap = iconBitmap;
        if (mIconRect != null)
            invalidateView();
    }


    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(INSTANCE_STATE, super.onSaveInstanceState());
        bundle.putFloat(STATE_ALPHA, mIconBgAlpha);
        bundle.putString(STATE_MESSAGE, mMessage);
        bundle.putInt(STATE_MESSAGE_MODE, mMsgMode);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            mIconBgAlpha = bundle.getFloat(STATE_ALPHA);
            mMessage = bundle.getString(STATE_MESSAGE);
            mMsgMode = bundle.getInt(STATE_MESSAGE_MODE);
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE_STATE));
        } else {
            super.onRestoreInstanceState(state);
        }
    }
}
