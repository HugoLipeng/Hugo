package com.devilwwj.glidedemo;

import android.content.Context;

import com.bumptech.glide.load.model.GenericLoaderFactory;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;

import java.io.InputStream;

/**
 * com.devilwwj.glidedemo
 * Created by devilwwj on 16/3/15.
 */
public class CustomImageSizeModelFactory implements ModelLoaderFactory<CustomImageSizeModel, InputStream> {

    @Override
    public ModelLoader<CustomImageSizeModel, InputStream> build(Context context, GenericLoaderFactory genericLoaderFactory) {
        return new CustomImageSizeUrlLoader(context);
    }

    @Override
    public void teardown() {

    }
}
