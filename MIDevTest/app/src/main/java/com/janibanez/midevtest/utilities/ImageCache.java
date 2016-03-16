package com.janibanez.midevtest.utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

/**
 * Created by jwgibanez on 22/01/2016.
 */
public class ImageCache {
    private static final String TAG = "ImageCache";

    private static final int DEFAULT_MEM_CACHE_SIZE = 1024 * 1024 * 5; // 5MB
    private LruCache<String, Bitmap> mMemoryCache;
    private ImageCacheParams mCacheParams;

    public ImageCache(ImageCacheParams cacheParams) {
        init(cacheParams);
    }

    private void init(ImageCacheParams cacheParams) {
        mCacheParams = cacheParams;

        Log.d(TAG, "Memory cache created (size = " + mCacheParams.memCacheSize + ")");

        mMemoryCache = new LruCache<String, Bitmap>(mCacheParams.memCacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return getBitmapSize(bitmap);
            }
        };
    }

    public void addBitmapToMemCache(String data, Bitmap bitmap) {
        if (data == null || bitmap == null) {
            return;
        }

        // Add to memory cache
        if (mMemoryCache != null && mMemoryCache.get(data) == null) {
            mMemoryCache.put(data, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String data) {
        Bitmap bitmap = null;
        if (mMemoryCache != null) {
            bitmap = mMemoryCache.get(data);
        }

        if (bitmap != null && !bitmap.isRecycled())
            return bitmap;

        return null;
    }

    public void clearCache() {
        if (mMemoryCache != null) {
            mMemoryCache.evictAll();
            Log.d(TAG, "Memory cache cleared");
        }
    }

    public void removeCache(String data) {
        if (mMemoryCache != null && mMemoryCache.get(data) != null)
            mMemoryCache.remove(data);
    }

    public static class ImageCacheParams {
        public int memCacheSize = DEFAULT_MEM_CACHE_SIZE;
        public String diskCachePath;

        public ImageCacheParams(Context context, float percent, String diskCachePath) {
            this.diskCachePath = diskCachePath;
            setMemCacheSizePercent(context, percent);
        }

        public void setMemCacheSizePercent(Context context, float percent) {
            if (percent < 0.05f || percent > 0.8f) {
                throw new IllegalArgumentException("setMemCacheSizePercent - percent must be "
                        + "between 0.05 and 0.8 (inclusive)");
            }
            this.memCacheSize = Math.round(percent * Runtime.getRuntime().maxMemory());
        }
    }

    public static int getBitmapSize(Bitmap bitmap) {
        return bitmap.getByteCount();
    }
}
