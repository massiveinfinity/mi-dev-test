package com.mi.androidarsenal.app;

import android.app.Application;
import android.content.Intent;
import android.content.res.Configuration;

import com.mi.androidarsenal.activity.MainActivity;

/**
 * Application sub class basically to catch any uncaught exceptions.
 *
 * @author Samir Sarosh
 */
@SuppressWarnings("ALL")
public class ApplicationSubClass extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Setup handler for uncaught exceptions.
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable e) {
                handleUncaughtException(thread, e);
            }
        });
    }

    public void handleUncaughtException(Thread thread, Throwable e) {
        //TODO log exception, we can send for analytics

        e.printStackTrace();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // required when starting from Application
        startActivity(intent);

        System.exit(1);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

}
