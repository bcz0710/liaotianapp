package com.example.chat.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.luck.picture.lib.engine.ImageEngine;
import com.luck.picture.lib.listener.OnImageCompleteCallback;
import com.luck.picture.lib.tools.MediaUtils;
import com.luck.picture.lib.widget.longimage.ImageSource;
import com.luck.picture.lib.widget.longimage.ImageViewState;
import com.luck.picture.lib.widget.longimage.SubsamplingScaleImageView;

public class GlideEngine implements ImageEngine {

    private static final int GRID_IMAGE_SIZE = 200;
    private static final int FOLDER_IMAGE_SIZE = 180;

    // 单例模式，确保全局只有一个实例
    private static GlideEngine instance;

    private GlideEngine() {
    }

    public static GlideEngine getInstance() {
        if (instance == null) {
            synchronized (GlideEngine.class) {
                if (instance == null) {
                    instance = new GlideEngine();
                }
            }
        }
        return instance;
    }

    /**
     * 通用图片加载
     *
     * @param context   上下文
     * @param url       图片路径
     * @param imageView 承载图片的ImageView
     */
    private void loadImageWithGlide(@NonNull Context context, @NonNull String url, @NonNull ImageView imageView) {
        Glide.with(context)
                .load(url)
                .into(imageView);
    }

    /**
     * 判断是否为长图并加载到相应的控件
     */
    private void handleLongImage(Bitmap resource, ImageView imageView, SubsamplingScaleImageView longImageView) {
        boolean isLongImage = MediaUtils.isLongImg(resource.getWidth(), resource.getHeight());
        longImageView.setVisibility(isLongImage ? View.VISIBLE : View.GONE);
        imageView.setVisibility(isLongImage ? View.GONE : View.VISIBLE);

        if (isLongImage) {
            // 设置长图显示属性
            longImageView.setQuickScaleEnabled(true);
            longImageView.setZoomEnabled(true);
            longImageView.setDoubleTapZoomDuration(100);
            longImageView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CENTER_CROP);
            longImageView.setDoubleTapZoomDpi(SubsamplingScaleImageView.ZOOM_FOCUS_CENTER);
            longImageView.setImage(ImageSource.bitmap(resource), new ImageViewState(0, new PointF(0, 0), 0));
        } else {
            // 普通图片
            imageView.setImageBitmap(resource);
        }
    }

    @Override
    public void loadImage(@NonNull Context context, @NonNull String url, @NonNull ImageView imageView) {
        loadImageWithGlide(context, url, imageView);
    }

    @Override
    public void loadImage(@NonNull Context context, @NonNull String url, @NonNull ImageView imageView,
                          SubsamplingScaleImageView longImageView, OnImageCompleteCallback callback) {
        Glide.with(context)
                .asBitmap()
                .load(url)
                .into(new ImageViewTarget<Bitmap>(imageView) {
                    @Override
                    public void onLoadStarted(@Nullable Drawable placeholder) {
                        if (callback != null) callback.onShowLoading();
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        if (callback != null) callback.onHideLoading();
                    }

                    @Override
                    protected void setResource(@Nullable Bitmap resource) {
                        if (callback != null) callback.onHideLoading();
                        if (resource != null) {
                            handleLongImage(resource, imageView, longImageView);
                        }
                    }
                });
    }

    @Override
    public void loadImage(@NonNull Context context, @NonNull String url, @NonNull ImageView imageView,
                          SubsamplingScaleImageView longImageView) {
        Glide.with(context)
                .asBitmap()
                .load(url)
                .into(new ImageViewTarget<Bitmap>(imageView) {
                    @Override
                    protected void setResource(@Nullable Bitmap resource) {
                        if (resource != null) {
                            handleLongImage(resource, imageView, longImageView);
                        }
                    }
                });
    }

    @Override
    public void loadFolderImage(@NonNull Context context, @NonNull String url, @NonNull ImageView imageView) {
        Glide.with(context)
                .asBitmap()
                .load(url)
                .override(FOLDER_IMAGE_SIZE, FOLDER_IMAGE_SIZE)
                .centerCrop()
                .sizeMultiplier(0.5f)
                .apply(new RequestOptions().placeholder(com.luck.picture.lib.R.drawable.picture_image_placeholder))
                .into(new BitmapImageViewTarget(imageView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable roundedBitmap = RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        roundedBitmap.setCornerRadius(8);
                        imageView.setImageDrawable(roundedBitmap);
                    }
                });
    }

    @Override
    public void loadAsGifImage(@NonNull Context context, @NonNull String url, @NonNull ImageView imageView) {
        Glide.with(context)
                .asGif()
                .load(url)
                .into(imageView);
    }

    @Override
    public void loadGridImage(@NonNull Context context, @NonNull String url, @NonNull ImageView imageView) {
        Glide.with(context)
                .load(url)
                .override(GRID_IMAGE_SIZE, GRID_IMAGE_SIZE)
                .centerCrop()
                .apply(new RequestOptions().placeholder(com.luck.picture.lib.R.drawable.picture_image_placeholder))
                .into(imageView);
    }
}
