package com.infinity.massive.model.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.infinity.massive.ApplicationMassiveInfinity;
import com.infinity.massive.R;
import com.infinity.massive.model.pojo.Devices;
import com.infinity.massive.view.activity.AndroidVersionDetailsActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Ilanthirayan on 25/1/16.
 */
public class DeviceListViewAdapter extends RecyclerView.Adapter{

    private static final String TAG = DeviceListViewAdapter.class.getSimpleName();

    private Activity mActivity;
    private List<Devices> devicesList;

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    public DeviceListViewAdapter(Activity mActivity, List<Devices> devicesList) {
        this.mActivity = mActivity;
        this.devicesList = devicesList;
    }

    @Override
    public int getItemCount() {
        return devicesList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return devicesList.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;
        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar)v.findViewById(R.id.progressBar);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        RecyclerView.ViewHolder viewHolder;
        if(position==VIEW_ITEM) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_item_device_view, viewGroup, false);
            viewHolder = new DeviceViewHolder(viewGroup, view);
        }else{
            View view = LayoutInflater.from(viewGroup.getContext()) .inflate(R.layout.progress_item, viewGroup, false);
            viewHolder = new ProgressViewHolder(view);
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof  ProgressViewHolder){
            ((ProgressViewHolder)holder).progressBar.setIndeterminate(true);
        }else {
            final Devices item = devicesList.get(position);
            ((DeviceViewHolder) holder).nameTxt.setText(item.getName());
            ((DeviceViewHolder) holder).snippetTxt.setText(item.getSnippet());
            ((DeviceViewHolder) holder).androidIdTxt.setText(item.getAndroidId());

            //Get the product image
            if(item.getImageUrl() != null) {
                Picasso.with(mActivity).load(item.getImageUrl()).into(((DeviceViewHolder) holder).deviceImg);
            }

            ((DeviceViewHolder) holder).deviceCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //START THE PRODUCT DETAILS ACTIVITY
                    Intent intent = new Intent(ApplicationMassiveInfinity.getContext(), AndroidVersionDetailsActivity.class);
                    intent.putExtra(AndroidVersionDetailsActivity.EXTRAS_MASSIVE_INFINITY_DEVICE_ID, item.getId());
                    mActivity.startActivity(intent);
                }
            });
        }
    }

    public class DeviceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView deviceImg;
        TextView snippetTxt;
        TextView androidIdTxt;
        TextView nameTxt;
        ViewGroup viewGroup;
        CardView deviceCardView;

        public DeviceViewHolder(ViewGroup viewGroup, View itemView) {
            super(itemView);
            this.viewGroup = viewGroup;
            deviceCardView = (CardView) itemView.findViewById(R.id.device_card_layout);
            deviceImg = (ImageView) itemView.findViewById(R.id.device_image);
            nameTxt = (TextView) itemView.findViewById(R.id.device_name_txt);
            androidIdTxt = (TextView) itemView.findViewById(R.id.android_id_txt);
            snippetTxt = (TextView) itemView.findViewById(R.id.snippet_txt);
        }

        @Override
        public void onClick(final View view){
            //TODO
        }
    }

}
