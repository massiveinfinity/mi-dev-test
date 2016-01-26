package com.infinity.massive.controller;

import com.infinity.massive.ApplicationMassiveInfinity;
import com.infinity.massive.R;
import com.infinity.massive.model.api.AndroidVersionInfoService;
import com.infinity.massive.model.pojo.Android;

import java.util.List;

import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by Ilanthirayan on 26/1/16.
 */
public class AndroidVersionController {

    private static final String TAG = DeviceController.class.getSimpleName();

    private Retrofit retrofit;
    private AndroidVersionInfoService androidVersionInfoService;

    public AndroidVersionController() {
        retrofit = new Retrofit.Builder()
                .baseUrl(ApplicationMassiveInfinity.getContext().getResources().getString(R.string.massive_infinity_api_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        androidVersionInfoService = retrofit.create(AndroidVersionInfoService.class);
    }

    public Call<List<Android>> getProductDetailsService(){
        Call<List<Android>> response = androidVersionInfoService.getAndroidVersions(
        );

        return response;
    }
}
