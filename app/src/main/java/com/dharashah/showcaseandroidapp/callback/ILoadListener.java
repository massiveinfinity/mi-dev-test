package com.dharashah.showcaseandroidapp.callback;

import com.dharashah.showcaseandroidapp.model.AllData;

/**
 * Created by user on 23/01/2016.
 */
public interface ILoadListener {
    void onLoadData();
    void onRefreshData(AllData data);
}
