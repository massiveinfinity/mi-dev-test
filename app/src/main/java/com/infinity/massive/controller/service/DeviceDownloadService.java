package com.infinity.massive.controller.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.infinity.massive.ApplicationMassiveInfinity;
import com.infinity.massive.controller.DeviceController;
import com.infinity.massive.model.pojo.Devices;
import com.infinity.massive.utils.DataBaseClient;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Ilanthirayan on 25/1/16.
 */
public class DeviceDownloadService extends Service{

    public static final String ACTION_BLOCK_RENDER_DEVICE_DOWNLOAD_FINISHED = DeviceDownloadService.class.getName()
            + ".action.BLOCK_RENDER_DEVICE_DOWNLOAD_FINISHED";

    /**
     * String {@link #ACTION_BLOCK_RENDER_DEVICE_DOWNLOAD_FINISHED} intent extra:
     * the full name of the service class that downloaded
     */
    public static final String EXTRA_SERVICE_NAME = "service_name";

    public static final String EXTRA_REMAINING_BLOCK_RENDER_DEVICE_LIST = "remaining_block_render_device_list";

    static final String TAG = DeviceDownloadService.class.getSimpleName();

    static final AtomicInteger blockRenderDeviceListLayoutLeft = new AtomicInteger(0);


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        final int res = START_STICKY;

        if(intent == null) {
            Log.d(TAG, "Inside " + TAG + " intent is @null");
            return res;
        }else{
            Log.d(TAG, "Inside " + TAG + " intent is NOT @null");
        }
        blockRenderDeviceListLayoutLeft.incrementAndGet();

        startDownload();


        return res;
    }


    protected void startDownload(){
        DeviceController mDeviceController = new DeviceController();
        Call<List<Devices>> response = mDeviceController.getProductDetailsService();
        response.enqueue(new Callback<List<Devices>>() {
            @Override
            public void onResponse(Response<List<Devices>> response, Retrofit retrofit) {
                Log.d(TAG, "Response Raw :: " + response.raw());
                if (response.body().size() > 0) {
                    //SUCCESSFULLY PRODUCTS RECEIVE

                    //SAVE THE PRODUCTS IN TO THE DATABASE
                    DataBaseClient.getInstance().insertDevices(response.body(), true);

                    //SEND THE BROADCAST TO NOTIFY DOWNLOAD COMPLETED
                    final Intent buzzIntent = new Intent(ACTION_BLOCK_RENDER_DEVICE_DOWNLOAD_FINISHED)
                            .putExtra(EXTRA_SERVICE_NAME, getClass().getName())
                            .putExtra(EXTRA_REMAINING_BLOCK_RENDER_DEVICE_LIST, blockRenderDeviceListLayoutLeft.decrementAndGet());
                    ApplicationMassiveInfinity.getLbm().sendBroadcast(buzzIntent);

                } else {
                    //ERROR MESSAGE DIALOG
                }

                Log.d(TAG, "@@blockRenderBuzzFeedsLayoutLeft :: " + blockRenderDeviceListLayoutLeft.get());
                stopSelf();
            }

            @Override
            public void onFailure(Throwable e) {
                Log.d(TAG, "onFailure: " + e.getMessage());
                stopSelf();
            }
        });
    }


    public static int getRemainingBlockRenderProductLayoutDownloads(){
        return blockRenderDeviceListLayoutLeft.get();
    }


    /**
     * Start a download service, if it is not disabled in the config.
     * The config.xml values contains a string value named 'disabled_download_service_simple_names'
     * Add the simple name of the download service class to that string, separated by whitespace in order
     * for that service not to be started.
     *
     * @param startWhenAlreadyRunning force start even if the service is already running
     * @param extras                  extras to start service intent
     */
    public static void start( final boolean startWhenAlreadyRunning, final Bundle extras) {
        final Context mContext = ApplicationMassiveInfinity.getContext();
        if (!startWhenAlreadyRunning) {
            final ActivityManager manager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
            for (ActivityManager.RunningServiceInfo serviceInfo : manager.getRunningServices(Integer.MAX_VALUE)) {
                if (DeviceDownloadService.class.getName().equals(serviceInfo.service.getClassName())) {
                    Log.d(TAG, DeviceDownloadService.class.getSimpleName() + " not started because it is already running");
                    return;
                }
            }
        }

        Log.d(TAG, DeviceDownloadService.class.getSimpleName() + " starting");
        final Intent intent = new Intent(mContext, DeviceDownloadService.class);
        if (extras != null) {
            intent.replaceExtras(extras);
        }
        mContext.startService(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
