package com.janibanez.midevtest.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.janibanez.midevtest.R;
import com.janibanez.server.models.Device;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jwgibanez on 21/01/2016.
 */
public class DevicesListAdapter extends ArrayAdapter<Device> {

    List<Device> mData;

    public DevicesListAdapter(Context context, ArrayList<Device> list) {
        super(context, R.layout.list_item_device, list);
        mData = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Device data = mData.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_device, parent, false);
        }

        TextView id = (TextView) convertView.findViewById(R.id.id);
        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView androidId = (TextView) convertView.findViewById(R.id.androidId);
        TextView carrier = (TextView) convertView.findViewById(R.id.carrier);
        TextView imageUrl = (TextView) convertView.findViewById(R.id.imageUrl);

        TextView snippet = (TextView) convertView.findViewById(R.id.snippet);

        id.setText(TextUtils.concat("ID: ", String.valueOf(data.id)));
        name.setText(TextUtils.concat("Name: ", data.name));
        androidId.setText(TextUtils.concat("AndroidId: ", String.valueOf(data.androidId)));
        imageUrl.setText(TextUtils.concat("Codename: ", data.imageUrl));
        carrier.setText(TextUtils.concat("Target: ", data.carrier));
        snippet.setText(TextUtils.concat("Distribution: ", data.snippet));

        return convertView;
    }

}
