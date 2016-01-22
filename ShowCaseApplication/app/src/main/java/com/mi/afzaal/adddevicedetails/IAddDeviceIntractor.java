package com.mi.afzaal.adddevicedetails;


import com.mi.afzaal.models.DeviceData;

public interface IAddDeviceIntractor {

	void addDevice(String url, DeviceData data, IAddDeviceListener listener);
	
}
