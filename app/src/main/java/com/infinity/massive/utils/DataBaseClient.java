package com.infinity.massive.utils;

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
}
