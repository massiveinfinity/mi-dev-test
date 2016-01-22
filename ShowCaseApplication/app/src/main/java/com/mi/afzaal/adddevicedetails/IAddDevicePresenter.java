package com.mi.afzaal.adddevicedetails;


import com.mi.afzaal.models.DeviceData;

public interface IAddDevicePresenter {

	void AddData(DeviceData d);
	void onDestroy();
}
