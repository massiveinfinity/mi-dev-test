package com.dharashah.showcaseandroidapp.network.interfaces;

import com.dharashah.showcaseandroidapp.model.AllData;
import com.dharashah.showcaseandroidapp.model.AndroidHistory;
import com.dharashah.showcaseandroidapp.model.Devices;

import java.util.List;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Dhara Shah on 22/01/2016. </br>
 * Holds all the webservice calls to be made.
 */
public interface ApiService {
    @GET("android")
    Call<List<AndroidHistory>> getAndroidVersions(@Query(value = "_start", encoded = true) int startValue,
                                                  @Query(value="_end", encoded = true) int endValue);

    @GET("devices?_sort=id&_order=DESC")
    Call<List<Devices>> getDevices(@Query(value = "_start", encoded = true) int startValue,
                                   @Query(value="_end", encoded = true) int endValue);

    @GET("db")
    Call<AllData> getAllData();

    @POST("android")
    Call<AndroidHistory> addAndroidVersion(@Body AndroidHistory androidHistory);

    @POST("devices")
    Call<Devices> addDevice(@Body Devices device);

    @DELETE("android/{id}")
    Call<AndroidHistory> deleteVersion(@Path("id") int androidId);

    @DELETE("devices/{id}")
    Call<Devices> deleteDevice(@Path("id") int deviceId);
}
