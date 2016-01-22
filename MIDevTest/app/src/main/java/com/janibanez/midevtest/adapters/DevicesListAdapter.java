package com.janibanez.midevtest.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.janibanez.midevtest.R;
import com.janibanez.midevtest.utilities.ImageCache;
import com.janibanez.server.models.Device;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jwgibanez on 21/01/2016.
 */
public class DevicesListAdapter extends ArrayAdapter<Device> {

    List<Device> mData;
    ImageCache mImageCache;

    // use view holder pattern
    public static class ViewHolder {
        TextView name;
        TextView snippet;
        ProgressBar progress;
        ImageView image;
        String photoUrl;
    }

    public DevicesListAdapter(Context context, ArrayList<Device> list) {
        super(context, R.layout.list_item_device, list);
        mData = list;

        // implement image caching, using 20% of max runtime memory
        ImageCache.ImageCacheParams params = new ImageCache.ImageCacheParams(context, 0.2f, getFileDirectory() + "/cache");
        mImageCache = new ImageCache(params);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Device data = mData.get(position);

        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_device, parent, false);

            holder = new ViewHolder();

            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.snippet = (TextView) convertView.findViewById(R.id.snippet);
            holder.progress = (ProgressBar) convertView.findViewById(R.id.progress);
            holder.image = (ImageView) convertView.findViewById(R.id.image);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.name.setText(data.name);
        holder.snippet.setText(data.snippet);
        holder.photoUrl = data.imageUrl;

        if (holder.photoUrl != null) {
            // look up for cached image
            Bitmap bitmap = mImageCache.getBitmapFromMemCache(holder.photoUrl);

            if (bitmap != null) {
                // show cached image
                holder.image.setImageBitmap(bitmap);
                holder.image.setVisibility(View.VISIBLE);
                holder.progress.setVisibility(View.INVISIBLE);
            } else {
                // try to fetch image, and cache it
                holder.image.setVisibility(View.INVISIBLE);
                holder.progress.setVisibility(View.VISIBLE);
                GetBitmap task = new GetBitmap(holder.photoUrl, holder, new GetBitmapCallback() {
                    @Override
                    public void onSuccess(String url, ViewHolder holder, Bitmap bitmap) {
                        mImageCache.addBitmapToMemCache(url, bitmap);

                        // only apply bitmap to correct view
                        if (TextUtils.equals(url, holder.photoUrl)) {
                            holder.image.setImageBitmap(bitmap);
                            holder.image.setVisibility(View.VISIBLE);
                            holder.progress.setVisibility(View.INVISIBLE);
                        }
                    }
                });
                task.execute();
            }
        } else {
            holder.image.setImageBitmap(null);
            holder.image.setVisibility(View.VISIBLE);
            holder.progress.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

    private static File getFileDirectory() {
        File directory = new File(Environment.getExternalStorageDirectory(), "SpotifyChartsFiles");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        return directory;
    }

    private interface GetBitmapCallback {
        void onSuccess(String url, ViewHolder holder, Bitmap bitmap);
    }

    private static class GetBitmap extends AsyncTask<Void, Void, Bitmap> {

        private String url;
        private ViewHolder holder;
        private GetBitmapCallback callback;

        public GetBitmap(String url, ViewHolder holder, GetBitmapCallback callback) {
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
}
