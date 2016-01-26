package com.infinity.massive.controller;

import com.infinity.massive.ApplicationMassiveInfinity;
import com.infinity.massive.R;
import com.infinity.massive.model.api.DeviceInfoService;
import com.infinity.massive.model.pojo.Devices;

import java.util.List;

import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by Ilanthirayan on 25/1/16.
 */
public class DeviceController {

    private static final String TAG = DeviceController.class.getSimpleName();

    private Retrofit retrofit;
    private DeviceInfoService deviceInfoService;

    public DeviceController() {
        retrofit = new Retrofit.Builder()
                .baseUrl(ApplicationMassiveInfinity.getContext().getResources().getString(R.string.massive_infinity_api_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        deviceInfoService = retrofit.create(DeviceInfoService.class);
    }

    public Call<List<Devices>> getProductDetailsService(){
        Call<List<Devices>> response = deviceInfoService.getDevices(
        );

        return response;
    }
}
