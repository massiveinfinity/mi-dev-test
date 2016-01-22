package com.mi.afzaal.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mi.afzaal.models.DeviceData;
import com.mi.afzaal.showcaseapplication.R;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by F ABBASI on 1/21/2016.
 */
public class DeviceListAdapter extends ArrayAdapter<DeviceData> {

    private final Activity context;
    private final ArrayList<DeviceData> devicelist;

    public DeviceListAdapter(Activity context,ArrayList<DeviceData> list)
    {
        super(context, R.layout.device_view,list);
        this.context = context;
        this.devicelist = list;
    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        DeviceData devicedata = devicelist.get(position);
        View rowView= inflater.inflate(R.layout.device_view, null, true);
        ImageView image = (ImageView)rowView.findViewById(R.id.image);
        TextView text = (TextView) rowView.findViewById(R.id.text);
        if(devicedata.toString() != null)
            text.setText(devicedata.toString());
        return rowView;
    }
}
