package com.devilwwj.glidedemo;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.module.GlideModule;

/**
 * com.devilwwj.glidedemo
 * Created by devilwwj on 16/3/15.
 */
public class SimpleGlideModule implements GlideModule {

    @Override
    public void applyOptions(Context context, GlideBuilder glideBuilder) {
        // todo
        glideBuilder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
    }

    @Override
    public void registerComponents(Context context, Glide glide) {
        // todo
    }
}
