package com.mi.androidarsenal.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.mi.androidarsenal.R;
import com.mi.androidarsenal.network.VolleyHelper;
import com.mi.androidarsenal.utility.AppConstants;

/**
 * This fragment shows the device detail
 *
 * @author Samir Sarosh
 */
@SuppressWarnings("ALL")
public class DeviceDetailFragment extends Fragment implements AppConstants {

    public DeviceDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_device_detail, container, false);


        Bundle bundle = getArguments();

        String id = null;
        String name = NOT_AVAILABLE_TEXT;
        String imageUrl = null;
        String description = NOT_AVAILABLE_TEXT;
        String carrier = NOT_AVAILABLE_TEXT;
        String android_id = NOT_AVAILABLE_TEXT;

        if (bundle != null) {
            id = bundle.getString(KEY_DEVICE_ID);
            name = bundle.getString(KEY_NAME);
            imageUrl = bundle.getString(KEY_IMAGE_URL);
            description = bundle.getString(KEY_SNIPPET);
            carrier = bundle.getString(KEY_CARRIER);
            android_id = bundle.getString(KEY_ANDROID_ID);
        }

        TextView nameView = (TextView) view.findViewById(R.id.device_detail_name);
        TextView androidIdView = (TextView) view.findViewById(R.id.device_detail_androidid);
        TextView carrierView = (TextView) view.findViewById(R.id.device_detail_carrier);
        TextView descriptionView = (TextView) view.findViewById(R.id.device_detail_description);
        NetworkImageView imageView = (NetworkImageView) view.findViewById(R.id.device_detail_image);


        nameView.setText(name);
        androidIdView.setText(android_id);
        carrierView.setText(carrier);
        descriptionView.setText(description);

        if (!TextUtils.isEmpty(imageUrl)) {
            imageView.setImageUrl(imageUrl,
                    VolleyHelper.getInstance(getActivity()).getImageLoader());
        } else {
            imageView.setVisibility(View.GONE);
        }
        return view;
    }

}
