package com.infinite.myapp.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.infinite.myapp.MyApplication;
import com.infinite.myapp.R;
import com.infinite.myapp.utils.glide.CircleTransform;
import com.infinite.myapp.utils.glide.GlideRoundTransform;

/**
 * Created by Administrator on 2016-06-18.
 */
public class ImageLoader {
    /**
     * 加载圆形图片
     *
     * @param mContext
     * @param url
     */
    public synchronized static void loadCircleImage(Context mContext, String url, int defaultId, ImageView image, boolean isUser) {

        if (TextUtils.isEmpty(url)) {
            if(isUser){
                image.setImageResource(R.mipmap.default_user_avatar);
                return;
            }
            image.setImageResource(R.mipmap.ic_launcher);
            return;
        }

        Glide.with(mContext)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .transform(new CircleTransform(mContext))
                .placeholder(defaultId == -1 ? R.drawable.bg_transparent_shape : defaultId)
                .error(defaultId == -1 ? R.drawable.bg_transparent_shape : defaultId)
                .into(image);
    }

    /**
     * 加载圆角矩形图片
     *
     * @param mContext
     * @param url
     */
    public synchronized static void loadRoundCornerImage(Context mContext, String url, int defaultId, int radiusDp, ImageView image) {

        if (TextUtils.isEmpty(url)) {
            image.setImageResource(R.mipmap.ic_launcher);
            return;
        }

        Glide.with(mContext)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .transform(new GlideRoundTransform(mContext, radiusDp))
                .placeholder(defaultId == -1 ? R.mipmap.ic_launcher : defaultId)
                .error(defaultId == -1 ? R.mipmap.ic_launcher : defaultId)
                .into(image);
    }

    /**
     * 加载普通图片,加载中、加载失败时指定图片时使用该函数
     *
     * @param mContext
     * @param url
     * @param iv
     */
    public synchronized static void loadNormalImage(Context mContext, String url, int defaultId, ImageView iv) {
        if (TextUtils.isEmpty(url)) {
            iv.setImageResource(defaultId);
            return;
        }

        Glide.with(mContext)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(defaultId == -1 ? R.drawable.bg_transparent_shape : defaultId)
                .error(defaultId == -1 ? R.drawable.bg_transparent_shape : defaultId)
                .into(iv);
    }

    public synchronized static void loadImageCenterCrop(Context mContext, String url, int defaultId, ImageView iv) {
        if (TextUtils.isEmpty(url)) {
            iv.setImageResource(defaultId);
            return;
        }

        Glide.with(mContext)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .centerCrop()
                .placeholder(defaultId == -1 ? R.drawable.bg_transparent_shape : defaultId)
                .error(defaultId == -1 ? R.drawable.bg_transparent_shape : defaultId)
                .into(iv);
    }





    /**
     * 加载指定宽高的图片。
     *
     * @param mContext
     * @param url
     * @param defaultId
     * @param iv
     */
    public synchronized static void loadImageOrderWH(final Context mContext, String url, final int defaultId, final ImageView iv){
        if (TextUtils.isEmpty(url)) {
            iv.setImageResource(defaultId);
            return;
        }

        Glide.with(mContext).load(url).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {

                int max = (int) MyApplication.getInstance().getResources().getDimension(R.dimen.public_max_size);

                int width = bitmap.getWidth();
                int height = bitmap.getHeight();

                float img_yx_rate = (float) height / width;//16:9=1.78, 9:16=0.56

                float width_rate = (float) width / max;
                float height_rate = (float) height / max;

                if(width_rate >= height_rate){//横图
                    int height_result = 0;
                    if(img_yx_rate > 0.5 && img_yx_rate < 1){
                        height_result = (int) (height / width_rate);
                    } else {
                        height_result = max;
                    }
                    ViewGroup.LayoutParams layoutParams = iv.getLayoutParams();
                    layoutParams.height = height_result;
                    iv.setLayoutParams(layoutParams);
                    iv.setImageBitmap(bitmap);
                } else {//竖图
                    int width_result = 0;
                    if(img_yx_rate > 1.5 && img_yx_rate < 1.8){
                        width_result = (int) (width / height_rate);
                    } else {
                        width_result = max;
                    }
                    ViewGroup.LayoutParams layoutParams = iv.getLayoutParams();
                    layoutParams.width = width_result;
                    iv.setLayoutParams(layoutParams);
                    iv.setImageBitmap(bitmap);
                }
            }

            @Override
            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                super.onLoadFailed(e, errorDrawable);
                iv.setImageResource(defaultId == -1 ? R.drawable.bg_transparent_shape : defaultId);
            }
        });
    }

    /**
     * 加载指定宽高的图片，使用时只注意ImageView的宽高即可，底层自动获取需要加载的宽高
     *
     * @param mContext
     * @param url
     * @param defaultId
     * @param iv
     */
    public synchronized static void loadFullWidthImage(final Context mContext, String url, final int defaultId, final ImageView iv) {
        if (TextUtils.isEmpty(url)) {
            iv.setImageResource(defaultId);
            return;
        }

        Glide.with(mContext).load(url).asBitmap().dontAnimate().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                int swidth = ScreenUtil.getScreenWH(mContext)[0];
                float width_rate = (float) width / swidth;
                ViewGroup.LayoutParams layoutParams = iv.getLayoutParams();
                layoutParams.height = (int) (height / width_rate);
                iv.setLayoutParams(layoutParams);
                iv.setImageBitmap(bitmap);
            }

            @Override
            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                super.onLoadFailed(e, errorDrawable);
                iv.setImageResource(defaultId == -1 ? R.drawable.bg_transparent_shape : defaultId);
            }
        });
    }

    public synchronized static void loadFullWidthImage2(final Context mContext, String url, final int defaultId, final ImageView iv) {
        if (TextUtils.isEmpty(url)) {
            iv.setImageResource(defaultId);
            return;
        }

        Glide.with(mContext).load(url).asBitmap().placeholder(R.drawable.bg_transparent_shape).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {

                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                int swidth = ScreenUtil.getScreenWH(mContext)[0];
                float width_rate = (float) width / swidth;
                ViewGroup.LayoutParams layoutParams = iv.getLayoutParams();
                layoutParams.width = swidth;
                layoutParams.height = (int) (height / width_rate);
                iv.setLayoutParams(layoutParams);
                iv.setImageBitmap(bitmap);
            }

            @Override
            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                super.onLoadFailed(e, errorDrawable);
                iv.setImageResource(defaultId == -1 ? R.drawable.bg_transparent_shape : defaultId);
            }

        });
    }


}
