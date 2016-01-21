package com.janibanez.midevtest.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

    public VersionsListAdapter(Context context, ArrayList<Version> list) {
        super(context, R.layout.list_item_version, list);
        mData = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Version data = mData.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_version, parent, false);
        }

        TextView id = (TextView) convertView.findViewById(R.id.id);
        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView version = (TextView) convertView.findViewById(R.id.version);
        TextView codename = (TextView) convertView.findViewById(R.id.codename);
        TextView target = (TextView) convertView.findViewById(R.id.target);
        TextView distribution = (TextView) convertView.findViewById(R.id.distribution);

        id.setText(TextUtils.concat("ID: ", String.valueOf(data.id)));
        name.setText(TextUtils.concat("Name: ", data.name));
        version.setText(TextUtils.concat("Version: ", data.version));
        codename.setText(TextUtils.concat("Codename: ", data.codename));
        target.setText(TextUtils.concat("Target: ", data.target));
        distribution.setText(TextUtils.concat("Distribution: ", data.distribution));

        return convertView;
    }

}
