package com.mi.developerTest.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mi.developerTest.R;
import com.mi.developerTest.entity.AndroidEntity;
import com.mi.developerTest.entity.DeviceEntity;
import com.mi.developerTest.entity.PhoneDetailEntity;
import com.mi.developerTest.fragments.PhoneDetailsFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SharadW on 25-01-2016.
 */
public class PhoneDetailAdapter extends RecyclerView.Adapter<PhoneDetailAdapter.PhoneViewHolder> {

    private List<DeviceEntity> mDeviceEntityArrayList;
    private PhoneDetailEntity mPhoneDetailEntity;
    private FragmentActivity mContext;

    public PhoneDetailAdapter(FragmentActivity mContext, PhoneDetailEntity mPhoneDetailEntity) {
        this.mContext = mContext;
        this.mPhoneDetailEntity = mPhoneDetailEntity;
        this.mDeviceEntityArrayList = mPhoneDetailEntity.getDevices();
    }

    @Override
    public PhoneViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.custom_cardview_row, parent, false);

        return new PhoneViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PhoneViewHolder holder, int position) {
        final DeviceEntity mDeviceEntity = mDeviceEntityArrayList.get(position);
        if (mDeviceEntity != null) {
            String url = mDeviceEntity.getImageUrl();
            if (url != null && url.length() > 0) {
                Picasso.with(mContext).load(url).into(holder.phoneImageView);
            } else {
                holder.phoneImageView.setBackgroundResource(R.drawable.motorolaxoom);
            }
            holder.snippetTextView.setText(mDeviceEntity.getSnippet());
            holder.phoneNameTextView.setText(mDeviceEntity.getName());
            holder.carrierTextView.setText(mDeviceEntity.getCarrier());

            holder.phoneImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AndroidEntity mAndroidEntity = filterArrayByID(mDeviceEntity);
                    if(mAndroidEntity != null)
                    selectItem(new PhoneDetailsFragment(mAndroidEntity,mDeviceEntity));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDeviceEntityArrayList.size();
    }

    public static class PhoneViewHolder extends RecyclerView.ViewHolder {
        protected ImageView phoneImageView;
        protected TextView phoneNameTextView;
        protected TextView carrierTextView;
        protected TextView snippetTextView;

        public PhoneViewHolder(View v) {
            super(v);
            phoneImageView = (ImageView) v.findViewById(R.id.iv_phone_img);
            phoneNameTextView = (TextView) v.findViewById(R.id.tv_phone_title);
            carrierTextView = (TextView) v.findViewById(R.id.tv_carrier);
            snippetTextView = (TextView) v.findViewById(R.id.tv_snippet);
        }
    }

    public void selectItem(Fragment fragment) {
        FragmentManager fragmentManager = mContext.getSupportFragmentManager();
        fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.fl_content_frame, fragment).commit();
    }

    private AndroidEntity filterArrayByID(DeviceEntity mDeviceEntity) {
        int androidId = mDeviceEntity.getAndroidId();
        for (AndroidEntity mAndroidEntity : mPhoneDetailEntity.getAndroid()) {
            if (mAndroidEntity.getId() == androidId) {
                return mAndroidEntity;
            }

        }

        return null;
    }
}
