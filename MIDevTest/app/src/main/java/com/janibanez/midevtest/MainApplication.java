package com.janibanez.midevtest;

import android.app.Application;
import android.os.Environment;

import com.janibanez.midevtest.utilities.ImageCache;

import java.io.File;

/**
 * Created by jwgibanez on 22/01/2016.
 */
public class MainApplication extends Application {

    ImageCache mImageCache;

    @Override
    public void onCreate() {
        super.onCreate();

        // implement image caching, using 20% of max runtime memory
        ImageCache.ImageCacheParams params = new ImageCache.ImageCacheParams(this, 0.2f, getFileDirectory() + "/cache");
        mImageCache = new ImageCache(params);
    }

    public ImageCache getImageCache() {
        return mImageCache;
    }

    private static File getFileDirectory() {
        File directory = new File(Environment.getExternalStorageDirectory(), "SpotifyChartsFiles");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        return directory;
    }
}
