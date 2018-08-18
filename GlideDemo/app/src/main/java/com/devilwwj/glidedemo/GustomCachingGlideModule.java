package com.devilwwj.glidedemo;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.module.GlideModule;

/**
 * com.devilwwj.glidedemo
 * Created by devilwwj on 16/3/15.
 */
public class GustomCachingGlideModule implements GlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder glideBuilder) {
        // 自定义内存缓存
        MemorySizeCalculator calculator = new MemorySizeCalculator(context);
        int defaultMemoryCacheSize = calculator.getMemoryCacheSize();
        int defaultBitmapPoolSize = calculator.getBitmapPoolSize();

        int customMemoryCacheSize = (int) (1.2 * defaultMemoryCacheSize);
        int customBitmapPoolSize = (int) (1.2 * defaultBitmapPoolSize);


        glideBuilder.setMemoryCache(new LruResourceCache(customMemoryCacheSize));
        glideBuilder.setBitmapPool(new LruBitmapPool(customBitmapPoolSize));


        // 自定义磁盘缓存
        // set size & external vs. internal
        int cacheSize100MegaBytes = 104857600;

        glideBuilder.setDiskCache(new InternalCacheDiskCacheFactory(context, cacheSize100MegaBytes));


//        glideBuilder.setDiskCache(new ExternalCacheDiskCacheFactory(context, cacheSize100MegaBytes));

        // 选一个特定的存储目录
//        String downloadDirectoryPath = Environment.getDownloadCacheDirectory().getPath();

//        glideBuilder.setDiskCache(new DiskLruCacheFactory(downloadDirectoryPath, cacheSize100MegaBytes));


    }

    @Override
    public void registerComponents(Context context, Glide glide) {

    }
}
