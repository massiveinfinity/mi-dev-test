package com.zahid.mitask.devicesinfo.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zahid.mitask.devicesinfo.R;
import com.zahid.mitask.devicesinfo.bean.Device;

import java.util.ArrayList;

/**
 * Created by zahid.r on 1/25/2016.
 */
public class DeviceListAdapter extends BaseAdapter {
    private ArrayList<Device> deviceArrayList;
    private Context mContext;

    public DeviceListAdapter(Context mContext, ArrayList<Device> deviceArrayList) {
        this.mContext = mContext;
        this.deviceArrayList = deviceArrayList;
    }

    @Override
    public int getCount() {
        return deviceArrayList.size();
    }

    @Override
    public Device getItem(int position) {
        return deviceArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(R.layout.devices_list_item, parent, false);

            holder = new ViewHolder();
            holder.txtDeviceName = (TextView) convertView.findViewById(R.id.textView_device_name);
            holder.imgDeviceLogo = (ImageView) convertView.findViewById(R.id.imageView_logo);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtDeviceName.setText(deviceArrayList.get(position).getName());

        String imageURL = deviceArrayList.get(position).getImageUrl();
        if (imageURL != null && !imageURL.isEmpty()) {
            Picasso.with(this.mContext).load(imageURL).fit().placeholder(R.drawable.placeholder_small).error(R.drawable.placeholder_small).into(holder.imgDeviceLogo);
        } else {
            holder.imgDeviceLogo.setImageResource(R.drawable.placeholder_small);
        }
        return convertView;
    }

    public class ViewHolder {
        public TextView txtDeviceName;
        public ImageView imgDeviceLogo;
    }
}
