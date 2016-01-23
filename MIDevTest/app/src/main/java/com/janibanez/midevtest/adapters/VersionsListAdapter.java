package com.janibanez.midevtest.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.janibanez.midevtest.R;
import com.janibanez.server.models.Version;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jwgibanez on 21/01/2016.
 */
public class VersionsListAdapter extends ArrayAdapter<Version> {

    List<Version> mData;

    // use view holder pattern
    public static class ViewHolder {
        TextView name, codename, version;
    }

    public VersionsListAdapter(Context context, ArrayList<Version> list) {
        super(context, R.layout.list_item_version, list);
        mData = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Version data = mData.get(position);

        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_version, parent, false);

            holder = new ViewHolder();

            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.version = (TextView) convertView.findViewById(R.id.version);
            holder.codename = (TextView) convertView.findViewById(R.id.codename);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.name.setText(TextUtils.concat("Name: ", data.name));
        holder.version.setText(TextUtils.concat("DbVersion: ", data.version));
        holder.codename.setText(TextUtils.concat("Codename: ", data.codename));

        return convertView;
    }

}
