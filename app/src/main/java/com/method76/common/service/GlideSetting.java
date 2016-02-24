package com.method76.common.service;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.module.GlideModule;
import com.method76.common.constant.CommonConst;

/**
 * Created by Sungjoon Kim on 2016-02-16.
 */
public class GlideSetting implements GlideModule, CommonConst {

    @Override public void applyOptions(Context context, GlideBuilder builder) {
        // Apply options to the builder here.
        builder.setDiskCache(
                new InternalCacheDiskCacheFactory(context, GLIDE_DISK_CACHE));
    }

    @Override public void registerComponents(Context context, Glide glide) {
        // register ModelLoaders here.
    }
}