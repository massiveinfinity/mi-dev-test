package com.sharad.demoapp.network;

/**
 * This Interface provide call back on success or failure of download task.
 * @author Sharad waghchaure
 *
 */

public interface DownloadListener{

    void onDownloadSuccess(int taskID, String response);

    void onDownloadFailure(int taskID, Throwable throwable, String response);
}
