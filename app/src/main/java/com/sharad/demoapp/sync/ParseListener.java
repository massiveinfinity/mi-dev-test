package com.sharad.demoapp.sync;

/**
 * This Interface provide call back on Parse success or failure.
 * @author Sharad waghchaure
 *
 */
public interface ParseListener{

    void onNewParseJob(int taskID, String response);

    void onParseSuccess(int taskID, Object object);

    void onParseFailed(int taskID);
}