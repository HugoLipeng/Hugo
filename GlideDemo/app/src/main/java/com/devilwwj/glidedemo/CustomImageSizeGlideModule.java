package com.devilwwj.glidedemo;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.module.GlideModule;

import java.io.InputStream;

/**
 * com.devilwwj.glidedemo
 * Created by devilwwj on 16/3/15.
 */
public class CustomImageSizeGlideModule implements GlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder glideBuilder) {

    }

    @Override
    public void registerComponents(Context context, Glide glide) {
        glide.register(CustomImageSizeModel.class, InputStream.class, new CustomImageSizeModelFactory());
    }
}
