package com.dharashah.showcaseandroidapp.adapter;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.dharashah.showcaseandroidapp.R;

/**
 * Created by user on 23/01/2016.
 */
public class DeviceViewHolder {
    public TextView txtDeviceName;
    public TextView txtDeviceDesc;
    public SwipeLayout swipeLayout;
    public RelativeLayout relDelete;

    public DeviceViewHolder(View view) {
        swipeLayout = (SwipeLayout) view.findViewById(R.id.swipe);
        txtDeviceName = (TextView)view.findViewById(R.id.txtDeviceName);
        txtDeviceDesc = (TextView)view.findViewById(R.id.txtDeviceDesc);
        relDelete = (RelativeLayout)view.findViewById(R.id.relDelete);
    }

    public TextView getTxtDeviceName() {
        return txtDeviceName;
    }

    public void setTxtDeviceName(TextView txtDeviceName) {
        this.txtDeviceName = txtDeviceName;
    }

    public TextView getTxtDeviceDesc() {
        return txtDeviceDesc;
    }

    public void setTxtDeviceDesc(TextView txtDeviceDesc) {
        this.txtDeviceDesc = txtDeviceDesc;
    }
}
