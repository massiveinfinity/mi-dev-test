package com.zahid.mitask.devicesinfo.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zahid.mitask.devicesinfo.R;
import com.zahid.mitask.devicesinfo.bean.Version;

import java.util.ArrayList;

/**
 * Created by zahid.r on 1/25/2016.
 */
public class VersionListAdapter extends BaseAdapter {
    private ArrayList<Version> versionArrayList;
    private Context mContext;

    public VersionListAdapter(Context mContext, ArrayList<Version> versionArrayList) {
        this.mContext = mContext;
        this.versionArrayList = versionArrayList;
    }

    @Override
    public int getCount() {
        return versionArrayList.size();
    }

    @Override
    public Version getItem(int position) {
        return versionArrayList.get(position);
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
            convertView = inflater.inflate(R.layout.versions_list_item, parent, false);

            holder = new ViewHolder();
            holder.txtVersionName = (TextView) convertView.findViewById(R.id.textView_version_name);
            holder.txtVersionCode = (TextView) convertView.findViewById(R.id.textView_version_code);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtVersionName.setText(versionArrayList.get(position).getName());
        holder.txtVersionCode.setText(versionArrayList.get(position).getVersion());

        return convertView;
    }

    public class ViewHolder {
        public TextView txtVersionName, txtVersionCode;
    }
}
