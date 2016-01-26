package com.mi.developerTest.sync;

/**
 * This Interface provide call back on success or failure of Data sync.
 * @author Sharad waghchaure
 *
 */
public interface SyncListener{

    void onSyncSuccess(int result, int taskId, Object object);

    void onSyncFailure(int result, int taskId, String message);

    void onSyncProgressUpdate(int progressPercent, String message);

}
