package com.infinity.massive.model.api;

import com.infinity.massive.model.pojo.Android;

import java.util.List;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by Ilanthirayan on 26/1/16.
 */
public interface AndroidVersionInfoService {

    @GET("android")
    Call<List<Android>> getAndroidVersions();

    @GET("android/{device_id}")
    Call<Android> getAndroidVersionDetails(
            @Path("device_id") int device_id
    );
}
