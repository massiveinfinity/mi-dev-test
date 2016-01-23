package com.dharashah.showcaseandroidapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.dharashah.showcaseandroidapp.R;
import com.dharashah.showcaseandroidapp.callback.IDataFunctionListener;
import com.dharashah.showcaseandroidapp.model.Devices;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by USER on 22-01-2016.
 */
public class DeviceAdapter extends  BaseSwipeAdapter {
    private Context mContext;
    private List<Devices> mDeviceList;
    private IDataFunctionListener mListener;

    public DeviceAdapter(Context context, List<Devices> objects, IDataFunctionListener listener) {
        mContext = context;
        mDeviceList = objects;
        mListener = listener;

        Collections.sort(mDeviceList, new Comparator<Devices>() {
            @Override
            public int compare(Devices lhs, Devices rhs) {
                if(lhs.getDeviceId() > rhs.getDeviceId()) {
                    return 1;
                }else {
                    return -1;
                }
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    @Override
    public View generateView(int position, ViewGroup parent) {
        return LayoutInflater.from(mContext).inflate(R.layout.individual_swipe_row, parent, false);
    }

    @Override
    public void fillValues(final int position, View convertView) {
        final DeviceViewHolder viewHolder = new DeviceViewHolder(convertView);
        viewHolder.txtDeviceName.setText(mDeviceList.get(position).getDeviceName());
        viewHolder.txtDeviceDesc.setText(mDeviceList.get(position).getDeviceDesc());
        viewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
        viewHolder.swipeLayout.addDrag(SwipeLayout.DragEdge.Right, viewHolder.swipeLayout.findViewById(R.id.bottom_wrapper));
        viewHolder.swipeLayout.getSurfaceView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // show the details
                mListener.onDisplaySelected(mDeviceList.get(position));
            }
        });
        viewHolder.relDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener != null) mListener.onDeleteSelected(mDeviceList.get(position).getDeviceId());
                mDeviceList.remove(position);
                viewHolder.swipeLayout.close(true);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getCount() {
        return mDeviceList.size();
    }

    @Override
    public Object getItem(int position) {
        return mDeviceList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
