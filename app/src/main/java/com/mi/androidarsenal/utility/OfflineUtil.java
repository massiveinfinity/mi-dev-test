package com.mi.androidarsenal.utility;

import android.content.Context;
import android.os.AsyncTask;

import com.mi.androidarsenal.database.DatabaseHandler;
import com.mi.androidarsenal.database.SharedPreferenceUtil;
import com.mi.androidarsenal.model.AndroidInfo;
import com.mi.androidarsenal.parser.ParseResponse;

/**
 * Utility class for offline storage
 *
 * @author Samir Sarosh
 */
@SuppressWarnings("ALL")
public class OfflineUtil {

    private final Context mContext;

    public interface RefreshDeviceListObserver {
        public void onRefreshDeviceList();
    }

    private static RefreshDeviceListObserver mRefreshDeviceListObserver;

    public static void setRefreshDeviceListObserver(RefreshDeviceListObserver observer) {
        mRefreshDeviceListObserver = observer;
    }

    public interface RefreshVersionListObserver {
        public void onRefreshVersionList();
    }

    private static RefreshVersionListObserver mRefreshVersionListObserver;

    public static void setRefreshVersionListObserver(RefreshVersionListObserver observer) {
        mRefreshVersionListObserver = observer;
    }

    public OfflineUtil(Context context) {
        mContext = context;
    }

    public void populateResponseInMemory(String response) {
        PopulateDevicesTask populateDevicesTask = new PopulateDevicesTask();
        populateDevicesTask.execute(response);
    }

    class PopulateDevicesTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... response) {
            ParseResponse parseResponse = new ParseResponse();
            AndroidInfo androidInfo = parseResponse.parseAndroidInfoResponse(response[0]);

            if (androidInfo != null) {

                DatabaseHandler.getDatabaseHandlerInstance(mContext)
                        .addAndroidInfo(androidInfo);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            SharedPreferenceUtil.getSharedPreferenceUtilInstance(mContext).saveDbPopulatedSuccess();

            if (mRefreshDeviceListObserver != null) {
                mRefreshDeviceListObserver.onRefreshDeviceList();
            }
            if (mRefreshVersionListObserver != null) {
                mRefreshVersionListObserver.onRefreshVersionList();
            }
        }
    }

}
