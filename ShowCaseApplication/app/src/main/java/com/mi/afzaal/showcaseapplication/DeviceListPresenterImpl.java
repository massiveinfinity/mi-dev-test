package com.mi.afzaal.showcaseapplication;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.mi.afzaal.models.DeviceData;
import com.mi.afzaal.utility.UtilityClass;


/**
 * Created by F ABBASI on 1/21/2016.
 */
public class DeviceListPresenterImpl implements iDeviceListPresenter,OnDataReceivedListener {
    iDeviceListView deviceView;
    IDeviceListIntractor deviceInteractor;

    public DeviceListPresenterImpl(iDeviceListView deviceView) {
        this.deviceView = deviceView;
        this.deviceInteractor = new IDeviceListInteractorImpl();
    }


    @Override
    public ArrayList<DeviceData> getData() {
        if(deviceView!=null){
            deviceView.showProgress();
        }
        return deviceInteractor.getDeviceList(UtilityClass.URL_DEVICELIST,this);
    }

    @Override
    public void onDestroy() {
        deviceView = null;
    }

    @Override
    public void setNetworkError() {
    if(deviceView!=null)
        deviceView.setonError();
    }

    @Override
    public void onsuccess() {
    	
    }

    @Override
    public void setData(ArrayList<DeviceData> devicedata) {
        if(deviceView != null)
            deviceView.setData(devicedata);
    }


	@Override
	public void saveDataToDB(Context c,ArrayList<DeviceData> data) {
		if(deviceView !=null){
			deviceInteractor.saveToDb(c,data);
		}
	}


}
