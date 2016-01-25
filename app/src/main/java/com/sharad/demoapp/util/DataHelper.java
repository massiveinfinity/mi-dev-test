package com.sharad.demoapp.util;


import android.app.Activity;
import android.content.Context;

public class DataHelper {

	private Context mContext;
	private Activity mActivity;
	public static final String APP_TITLE = "Massive Infinity";

	public Activity getActivity() {
		return mActivity;
	}

	public void setActivity(Activity mActivity) {
		this.mActivity = mActivity;
	}

	static DataHelper mDataHelper = new DataHelper();

	public static DataHelper getInstance() {
		if (mDataHelper != null)
			return mDataHelper;
		else {
			mDataHelper = new DataHelper();
		}
		return mDataHelper;
	}

	public Context getContext() {
		return mContext;
	}

	public void setContext(Context mContext) {
		this.mContext = mContext;
	}


}
