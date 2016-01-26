package com.infinity.massive.model.api;

import com.infinity.massive.model.pojo.Devices;

import java.util.List;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by Ilanthirayan on 26/1/16.
 */
public interface DeviceInfoService {

    @GET("devices")
    Call<List<Devices>> getDevices();

    @GET("devices/{device_id}")
    Call<Devices> getDeviceDetails(
            @Path("device_id") int device_id
    );
}
