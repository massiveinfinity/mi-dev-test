package com.mi.developerTest.network;


import java.io.UnsupportedEncodingException;


import com.loopj.android.http.AsyncHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class DownloadHandler extends AsyncHttpResponseHandler {
	private int taskID;

	private DownloadListener downloadListener;

	public DownloadHandler(int taskID, DownloadListener downloadListener) {

		this.taskID = taskID;
		this.downloadListener = downloadListener;
	}

	@Override
	public void onStart() {
		super.onStart();
		// Constants.showLog("SyncManager", "download task with id " + taskID +
		// " started ");
	}

	@Override
	public void onFailure(int arg0, Header[] arg1, byte[] response, Throwable throwable) {
		try {
			String res = null;
			if(response != null)
			{
			 res = new String(response,"utf-8");
			}
			downloadListener.onDownloadFailure(taskID, throwable,res);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

	@Override
	public void onSuccess(int statusCode, Header[] arg1, byte[] response) {
		try {
			if (statusCode != 200) {
				String res = new String(response, "utf-8");
				downloadListener.onDownloadFailure(taskID, new Throwable("statusCode " + statusCode),res);
			} else {

				String res = new String(response, "utf-8");
				downloadListener.onDownloadSuccess(taskID, res);
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	
}