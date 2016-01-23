package com.dharashah.showcaseandroidapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.dharashah.showcaseandroidapp.R;
import com.dharashah.showcaseandroidapp.model.AndroidHistory;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Dhara Shah on 22/01/2016.
 */
public class AndroidVersionsAdapter extends ArrayAdapter<AndroidHistory> {
    private Context mContext;
    private List<AndroidHistory> mList;
    private int RESOURCE;

    public AndroidVersionsAdapter(Context context, int resource, List<AndroidHistory> objects) {
        super(context, resource, objects);
        mContext = context;
        mList = objects;
        RESOURCE = resource;

        Collections.sort(mList, new Comparator<AndroidHistory>() {
            @Override
            public int compare(AndroidHistory lhs, AndroidHistory rhs) {
                if (rhs.getAndroidId() > lhs.getAndroidId()) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder vh = null;
        if(view == null) {
            view = LayoutInflater.from(mContext).inflate(RESOURCE, parent,false);
            vh = new ViewHolder();
            vh.txtAndroidVersion  = (TextView)view.findViewById(R.id.txtAndroidVersion);
            vh.txtAndroidVersionName = (TextView)view.findViewById(R.id.txtAndroidVersionName);
            view.setTag(vh);
        }else {
            vh = (ViewHolder)view.getTag();
        }

        AndroidHistory history = mList.get(position);
        vh.txtAndroidVersion.setText(history.getVersion());
        vh.txtAndroidVersionName.setText(history.getName());

        return view;
    }

    class ViewHolder {
        TextView txtAndroidVersionName;
        TextView txtAndroidVersion;
    }
}
