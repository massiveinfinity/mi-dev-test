package com.sharad.demoapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sharad.demoapp.R;
import com.sharad.demoapp.entity.AndroidEntity;
import com.sharad.demoapp.entity.DeviceEntity;
import com.squareup.picasso.Picasso;

/**
 * Created by SharadW on 25-01-2016.
 */
public class PhoneDetailsFragment extends Fragment {

    private TextView titleTextView,androidNameTextView,distributionTextView,targetTextView,versionTextView,detailTextView;
    private ImageView mImageView;
    private AndroidEntity mAndroidEntity;
    private DeviceEntity mDeviceEntity;

    public PhoneDetailsFragment(AndroidEntity mAndroidEntity,DeviceEntity mDeviceEntity)
    {
        this.mAndroidEntity = mAndroidEntity;
        this.mDeviceEntity = mDeviceEntity;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_phone_details, container,false);
        titleTextView = (TextView)rootView.findViewById(R.id.tv_name);
        androidNameTextView = (TextView)rootView.findViewById(R.id.tv_name_android);
        distributionTextView = (TextView)rootView.findViewById(R.id.tv_distribution);
        targetTextView = (TextView)rootView.findViewById(R.id.tv_target);
        versionTextView = (TextView)rootView.findViewById(R.id.tv_version);
        detailTextView = (TextView)rootView.findViewById(R.id.tv_details);
        mImageView = (ImageView)rootView.findViewById(R.id.iv_detail);

        String url = mDeviceEntity.getImageUrl();
        if (url != null && url.length() > 0) {
            Picasso.with(getContext()).load(url).into(mImageView);
        } else {
            mImageView.setBackgroundResource(R.drawable.motorolaxoom);
        }

        titleTextView.setText(mDeviceEntity.getName());
        androidNameTextView.setText(mAndroidEntity.getName());
        distributionTextView.setText("Distribution  "+mAndroidEntity.getDistribution());
        targetTextView.setText("Target  "+mAndroidEntity.getTarget());
        versionTextView.setText("Version  "+mAndroidEntity.getVersion());
        detailTextView.setText(mDeviceEntity.getSnippet());
        return  rootView;
    }
}
