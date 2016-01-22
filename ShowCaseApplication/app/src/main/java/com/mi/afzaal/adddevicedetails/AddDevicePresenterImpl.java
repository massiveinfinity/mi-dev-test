package com.mi.afzaal.adddevicedetails;


import com.mi.afzaal.models.DeviceData;
import com.mi.afzaal.utility.UtilityClass;

public class AddDevicePresenterImpl implements IAddDevicePresenter,IAddDeviceListener{

	IAddDeviceView addDeviceView;
	IAddDeviceIntractor addDeviceIntractor;
	
	
	public AddDevicePresenterImpl(IAddDeviceView deviceView) {
        this.addDeviceView = deviceView;
        this.addDeviceIntractor = new IAddDeviceIntractorImpl();
    }
	

	@Override
	public void AddData(DeviceData d) {
		if(addDeviceView!=null){
			addDeviceView.showProgress();
        }
		addDeviceIntractor.addDevice(UtilityClass.URL_ADDDEVICE,d, this);
	}
	
	@Override
	public void setNetworkError() {
		 if(addDeviceView!=null)
			 addDeviceView.setonError();
		
	}

	@Override
	public void onsuccess() {
		// TODO Auto-generated method stub
		 if(addDeviceView!=null)
			 addDeviceView.setonSuccess();
	}

	@Override
	public void onError() {
		 if(addDeviceView!=null)
			 addDeviceView.setonError();
	}


    @Override
    public void onDestroy() {
    	addDeviceView = null;
    }


}
