package com.mi.androidarsenal.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mi.androidarsenal.R;
import com.mi.androidarsenal.utility.AppConstants;

/**
 * This fragment shows the version detail
 *
 * @author Samir Sarosh
 */
@SuppressWarnings("ALL")
public class VersionDetailFragment extends Fragment implements AppConstants {



    public VersionDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_version_detail, container, false);


        Bundle bundle = getArguments();

        String id = null;
        String name = NOT_AVAILABLE_TEXT;
        String version = null;
        String codename = NOT_AVAILABLE_TEXT;
        String target = NOT_AVAILABLE_TEXT;
        String distribution = NOT_AVAILABLE_TEXT;

        if (bundle != null) {
            id = bundle.getString(KEY_VERSION_ID);
            name = bundle.getString(KEY_VERSION_NAME);
            version = bundle.getString(KEY_VERSION);
            codename = bundle.getString(KEY_CODENAME);
            target = bundle.getString(KEY_TARGET);
            distribution = bundle.getString(KEY_DESTRIBUTION);
        }

        TextView nameView = (TextView)view.findViewById(R.id.version_detail_name);
        TextView versionView = (TextView)view.findViewById(R.id.version_detail_version);
        TextView codenameView = (TextView)view.findViewById(R.id.version_detail_codename);
        TextView targetView = (TextView)view.findViewById(R.id.version_detail_target);
        TextView distributionView = (TextView)view.findViewById(R.id.version_detail_distribution);


        nameView.setText(name);
        versionView.setText(version);
        codenameView.setText(codename);
        targetView.setText(target);
        distributionView.setText(distribution);

        return view;
    }

}
