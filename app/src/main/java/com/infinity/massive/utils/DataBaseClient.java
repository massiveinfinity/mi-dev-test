package com.infinity.massive.utils;

import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.infinity.massive.model.pojo.Devices;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ilanthirayan on 25/1/16.
 */
public class DataBaseClient {

    private static final String TAG = DataBaseClient.class.getSimpleName();

    enum INSTANCE {
        INSTANCE;
        public final DataBaseClient instance = new DataBaseClient();
    }

    public static DataBaseClient getInstance() {
        return INSTANCE.INSTANCE.instance;
    }

    private DataBaseClient() {}

    public synchronized void insertDevices(final List<Devices> devicesList, boolean fromService){
        final SQLiteDatabase db = DBHelper.getInstance().getWritableDatabase();
        try{
            db.beginTransaction();
            //FROM SERVICE DOWNLOAD IS FIRST SET OF DEVICES SO CLEAR THE TABLE
            if(fromService){
                //clear the table before insert in to the table
                db.delete(Devices.TABLE_NAME, null, null);
            }
            for(Devices devices : devicesList){
                //INSERT INTO tbl_product
                boolean successProduct = db.insert(Devices.TABLE_NAME, null, devices.toContentValues()) > 0 ;
                if(successProduct){
                    Log.d(TAG, "Successfully inserted in to Devices.TABLE_NAME.");
                }else{
                    Log.d(TAG, "Not inserted in to Devices.TABLE_NAME.");
                }
            }
            db.setTransactionSuccessful();
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            db.endTransaction();
            db.close();
        }
    }


    public synchronized List<Devices> getDevices(){
        List<Devices> devicesList = new ArrayList<>();
        final SQLiteDatabase db = DBHelper.getInstance().getReadableDatabase();
        Cursor cursorDeviceList = db.query(Devices.TABLE_NAME,
                null, null, null, null, null, Devices.Columns._ID + " ASC");
        while(cursorDeviceList.moveToNext()){
            devicesList.add(new Devices(cursorDeviceList));
        }
        cursorDeviceList.close();
        return devicesList;
    }


    /**
     * Check if there is at least one Product Exist or Not
     *
     * @return {@link java.lang.Boolean}
     */
    public synchronized boolean hasAnyDevices(){
        final SQLiteDatabase db = DBHelper.getInstance().getReadableDatabase();
        return DatabaseUtils.longForQuery(db, "SELECT COUNT(*) FROM " + Devices.TABLE_NAME
                + " LIMIT 1", null) > 0 ;
    }
}
