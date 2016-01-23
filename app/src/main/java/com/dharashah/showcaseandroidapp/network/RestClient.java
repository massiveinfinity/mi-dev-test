package com.dharashah.showcaseandroidapp.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;

import com.dharashah.showcaseandroidapp.ShowCaseApp;
import com.dharashah.showcaseandroidapp.network.interfaces.ApiService;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;


public class RestClient {
    public static final String BASE_URL = "http://mobilesandboxdev.azurewebsites.net/";
    private ApiService mApiService;

    /**
     * Initializes Retrofit and sets the base point of connection
     */
    public RestClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mApiService = retrofit.create(ApiService.class);
    }

    /**
     * returns the instance of ApiService needed to make service calls
     * @return
     */
    public ApiService getApiService() {
        return mApiService;
    }

    /**
     * Checks the availability of internet
     * @return
     */
    public static boolean isNetworkAvailable() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm =
                (ConnectivityManager) ShowCaseApp.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Network[] networks = cm.getAllNetworks();
            NetworkInfo networkInfo;
            for (Network network : networks) {
                networkInfo = cm.getNetworkInfo(network);

                if (networkInfo.getTypeName().equalsIgnoreCase("WIFI"))
                    if (networkInfo.isConnected())
                        haveConnectedWifi = true;
                if (networkInfo.getTypeName().equalsIgnoreCase("MOBILE"))
                    if (networkInfo.isConnected())
                        haveConnectedMobile = true;

                if (networkInfo.getState().equals(NetworkInfo.State.CONNECTED)) {
                    return true;
                }
            }
        }else {
            //noinspection deprecation
            NetworkInfo[] info = cm.getAllNetworkInfo();
            if (info != null) {
                for (NetworkInfo anInfo : info) {
                    if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                        if (anInfo.getTypeName().equalsIgnoreCase("WIFI"))
                            if (anInfo.isConnected())
                                haveConnectedWifi = true;
                        if (anInfo.getTypeName().equalsIgnoreCase("MOBILE"))
                            if (anInfo.isConnected())
                                haveConnectedMobile = true;
                    }
                }
            }
        }
        return haveConnectedWifi || haveConnectedMobile;
    }
}
