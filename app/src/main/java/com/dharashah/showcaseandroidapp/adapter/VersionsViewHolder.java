package com.dharashah.showcaseandroidapp.adapter;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.dharashah.showcaseandroidapp.R;

/**
 * Created by user on 23/01/2016.
 */
public class VersionsViewHolder {
    public TextView txtAndroidVersionNumber;
    public TextView txtAndroidName;
    public SwipeLayout swipeLayout;
    public RelativeLayout relDelete;

    public VersionsViewHolder(View view) {
        swipeLayout = (SwipeLayout) view.findViewById(R.id.swipe);
        txtAndroidName = (TextView)view.findViewById(R.id.txtAndroidVersionName);
        txtAndroidVersionNumber = (TextView)view.findViewById(R.id.txtAndroidVersion);
        relDelete = (RelativeLayout)view.findViewById(R.id.relDelete);
    }
}
