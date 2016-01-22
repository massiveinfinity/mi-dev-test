package com.janibanez.midevtest.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.janibanez.midevtest.MainApplication;
import com.janibanez.midevtest.R;
import com.janibanez.midevtest.asynctasks.GetBitmapAsyncTask;
import com.janibanez.midevtest.models.ViewHolder;
import com.janibanez.midevtest.utilities.ImageCache;
import com.janibanez.server.models.Device;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jwgibanez on 21/01/2016.
 */
public class DevicesListAdapter extends ArrayAdapter<Device> {

    List<Device> mData;
    ImageCache mImageCache;

    // use view holder pattern
    public static class DeviceViewHolder extends ViewHolder {
        TextView name, snippet;
        ProgressBar progress;
        ImageView image;
        String photoUrl;
    }

    public DevicesListAdapter(Context context, ArrayList<Device> list) {
        super(context, R.layout.list_item_device, list);
        mData = list;

        MainApplication application = (MainApplication) ((Activity) context).getApplication();
        mImageCache = application.getImageCache();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Device data = mData.get(position);

        DeviceViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_device, parent, false);

            holder = new DeviceViewHolder();

            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.snippet = (TextView) convertView.findViewById(R.id.snippet);
            holder.progress = (ProgressBar) convertView.findViewById(R.id.progress);
            holder.image = (ImageView) convertView.findViewById(R.id.image);

            convertView.setTag(holder);
        } else {
            holder = (DeviceViewHolder) convertView.getTag();
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
                GetBitmapAsyncTask task = new GetBitmapAsyncTask(holder.photoUrl, holder, new GetBitmapAsyncTask.GetBitmapCallback() {
                    @Override
                    public void onSuccess(String url, ViewHolder holder, Bitmap bitmap) {
                        mImageCache.addBitmapToMemCache(url, bitmap);

                        DeviceViewHolder deviceViewHolder = (DeviceViewHolder) holder;

                        // only apply bitmap to correct view
                        if (TextUtils.equals(url, deviceViewHolder.photoUrl)) {
                            deviceViewHolder.image.setImageBitmap(bitmap);
                            deviceViewHolder.image.setVisibility(View.VISIBLE);
                            deviceViewHolder.progress.setVisibility(View.INVISIBLE);
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

}
