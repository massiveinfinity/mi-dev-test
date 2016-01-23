package com.dharashah.showcaseandroidapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.dharashah.showcaseandroidapp.R;
import com.dharashah.showcaseandroidapp.callback.IDataFunctionListener;
import com.dharashah.showcaseandroidapp.model.AndroidHistory;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

/**
 * Created by user on 23/01/2016.
 */
public class VersionsAdapter extends BaseSwipeAdapter {
    private LinkedList<AndroidHistory> mHistoryList;
    private Context mContext;
    private IDataFunctionListener mListener;

    public VersionsAdapter(Context context, LinkedList<AndroidHistory> list, IDataFunctionListener listener) {
        mContext = context;
        mHistoryList = list;
        mListener = listener;

        Collections.sort(mHistoryList, new Comparator<AndroidHistory>() {
            @Override
            public int compare(AndroidHistory lhs, AndroidHistory rhs) {
                if(lhs.getAndroidId() > rhs.getAndroidId()) {
                    return 1;
                }else {
                    return -1;
                }
            }
        });
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    @Override
    public View generateView(int position, ViewGroup parent) {
        return LayoutInflater.from(mContext).inflate(R.layout.individual_swipe_versions, parent, false);
    }

    @Override
    public void fillValues(final int position, View convertView) {
        final VersionsViewHolder viewHolder = new VersionsViewHolder(convertView);
        viewHolder.txtAndroidName.setText(mHistoryList.get(position).getName());
        viewHolder.txtAndroidVersionNumber.setText(mHistoryList.get(position).getVersion());
        viewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
        viewHolder.swipeLayout.addDrag(SwipeLayout.DragEdge.Right, viewHolder.swipeLayout.findViewById(R.id.bottom_wrapper));
        viewHolder.swipeLayout.getSurfaceView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // show the details or lead to another fragment
            }
        });
        viewHolder.relDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onDeleteSelected(mHistoryList.get(position).getAndroidId());
                mHistoryList.remove(position);
                viewHolder.swipeLayout.close(true);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getCount() {
        return mHistoryList.size();
    }

    @Override
    public Object getItem(int position) {
        return mHistoryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
