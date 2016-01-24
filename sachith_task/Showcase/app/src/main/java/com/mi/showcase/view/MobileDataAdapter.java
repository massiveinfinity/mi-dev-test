package com.mi.showcase.view;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mi.showcase.R;
import com.mi.showcase.view.dto.MobileDataListModel;

import java.util.List;

/**
 * @author Sachith Dickwella
 */
public class MobileDataAdapter extends ArrayAdapter<MobileDataListModel> {

    private Activity context;
    private List<MobileDataListModel> items;

    public MobileDataAdapter(Context context, List<MobileDataListModel> items) {
        super(context, R.layout.entry_list_view, items);
        this.context = (Activity) context;
        this.items = items;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        if (view == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            view = inflater.inflate(R.layout.entry_list_view, null, true);

            viewHolder = new ViewHolder();
            viewHolder.deviceName = (TextView) view.findViewById(R.id.deviceName);
            viewHolder.androidName = (TextView) view.findViewById(R.id.osName);
            viewHolder.version = (TextView) view.findViewById(R.id.version);

            view.setTag(R.id.deviceName, viewHolder.deviceName);
            view.setTag(R.id.osName, viewHolder.androidName);
            view.setTag(R.id.version, viewHolder.version);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        MobileDataListModel mobileDataListModel = getItem(position);

        viewHolder.deviceName.setText(mobileDataListModel.getDeviceName());
        viewHolder.androidName.setText(mobileDataListModel.getAndroidName());
        viewHolder.version.setText("ver. " + mobileDataListModel.getVersion());

        return view;
    }

    @Override
    public MobileDataListModel getItem(int position) {
        return this.items.get(position);
    }

    private static class ViewHolder {

        private TextView deviceName;
        private TextView androidName;
        private TextView version;
    }
}
