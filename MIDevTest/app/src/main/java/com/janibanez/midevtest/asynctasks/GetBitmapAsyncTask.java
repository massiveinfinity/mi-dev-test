package com.janibanez.midevtest.asynctasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.janibanez.midevtest.models.ViewHolder;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by jwgibanez on 22/01/2016.
 */
public class GetBitmapAsyncTask extends AsyncTask<Void, Void, Bitmap> {

    private String url;
    private ViewHolder holder;
    private GetBitmapCallback callback;

    public interface GetBitmapCallback {
        void onSuccess(String url, ViewHolder holder, Bitmap bitmap);
    }

    public GetBitmapAsyncTask(String url, ViewHolder holder, GetBitmapCallback callback) {
        this.url = url;
        this.holder = holder;
        this.callback = callback;
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        Bitmap bitmap = null;

        try {
            InputStream is = (InputStream) new URL(url).getContent();
            bitmap = BitmapFactory.decodeStream(is);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (callback != null) {
            callback.onSuccess(url, holder, bitmap);
        }
    }
}