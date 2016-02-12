package com.method76.common.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by Sungjoon Kim on 2016-02-06.
 */
public class SingletonManager {

    private static SingletonManager mInstance;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
//    private Picasso mPicasso;
    private Context mCtx;


    private SingletonManager(Context context) {
        mCtx = context.getApplicationContext();
        mRequestQueue = getRequestQueue();
//        mPicasso = getPicasso();
        mImageLoader = new ImageLoader(mRequestQueue,
            new ImageLoader.ImageCache() {
                private final LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(20);
                @Override
                public Bitmap getBitmap(String url) {
                return cache.get(url);
                }
                @Override
                public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url, bitmap);
                }
        });
    }

    public static synchronized SingletonManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SingletonManager(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    /*public Picasso getPicasso(){
        if (mPicasso == null) {
            Picasso.Builder builder = new Picasso.Builder(mCtx);
            builder.downloader(new OkHttpDownloader(mCtx, Integer.MAX_VALUE));
            mPicasso = builder.build();
//            if()
//            mPicasso.setIndicatorsEnabled(true);
//            mPicasso.setLoggingEnabled(true);
            Picasso.setSingletonInstance(mPicasso);
        }
        return mPicasso;
    }*/

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }
}
