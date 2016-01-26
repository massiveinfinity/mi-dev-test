package com.mi.androidarsenal.network;

import android.app.ActivityManager;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.ImageLoader;
import com.squareup.okhttp.OkHttpClient;

import java.io.File;

/**
 * Volley helper class
 *
 * @author Samir Sarosh
 */
@SuppressWarnings("ALL")
public class VolleyHelper {

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private static VolleyHelper sVolleyHelper;
    private Context mContext;
    private final int DISK_USAGE_BYTES = 60 * 1024 * 1024;
    public final String CACHE_DIR = "mi_photos";
    private File cacheDir;


    private RequestQueue newRequestQueue(Context context) {
        File rootCache = context.getExternalCacheDir();
        if (rootCache == null) {
            rootCache = context.getCacheDir();
        }
        cacheDir = new File(rootCache, CACHE_DIR);
        cacheDir.mkdirs();
        OkHttpStack stack = new OkHttpStack(new OkHttpClient());

        BasicNetwork network = new BasicNetwork(stack);
        DiskBasedCache diskBasedCache = new DiskBasedCache(cacheDir, DISK_USAGE_BYTES);
        RequestQueue queue = new RequestQueue(diskBasedCache, network);

        queue.start();
        return queue;
    }


    private RequestQueue.RequestFilter mRequestFilter = new RequestQueue.RequestFilter() {
        @Override
        public boolean apply(Request<?> request) {
            return true;
        }
    };

    public static synchronized VolleyHelper getInstance(Context context) {
        if (sVolleyHelper == null) {
            sVolleyHelper = new VolleyHelper(context);
        }
        return sVolleyHelper;
    }

    private VolleyHelper(Context context) {
        mContext = context;
        init();
    }

    public void init() {
        initRequestQueue();
        initImageLoader();
    }

    private void initRequestQueue() {
        mRequestQueue = newRequestQueue(mContext);
    }

    private void initImageLoader() {
        int memClass = ((ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
        int cacheSize = 1024 * 1024 * memClass / 8;
        mImageLoader = new ImageLoader(mRequestQueue, new BitmapCache(cacheSize));
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue != null) {
            return mRequestQueue;
        } else {
            init();
            return mRequestQueue;
        }
    }

    public ImageLoader getImageLoader() {
        if (mImageLoader != null) {
            return mImageLoader;
        } else {
            init();
            return mImageLoader;
        }
    }

    public void release() {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(mRequestFilter);
        }

    }


}
