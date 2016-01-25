package com.sharad.demoapp.component;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
/**
 * This call is used to provide custom Alerts.
 * @author Sharad waghchaure
 *
 */

public class AlertManager {

	public static void showAlert(Context context, String title, String message,
			String okButtonName,
			android.content.DialogInterface.OnClickListener onClickListener) {
		if (context != null) {
			final AlertDialog.Builder mAlert = new AlertDialog.Builder(context);
			mAlert.setTitle(title);
			mAlert.setMessage(message);
			mAlert.setPositiveButton(okButtonName, onClickListener);
			Activity activity = (Activity) context;
			activity.runOnUiThread(new Runnable() {
				  public void run() {
	
					  try {
						  mAlert.show();
					} catch (Exception e) {
						// TODO: handle exception
					}
						
				  }
				});
		}

	}

	public static void showNonCancelableAlert(Context context, String title,
			String message, String okButtonName,
			android.content.DialogInterface.OnClickListener retryClickListener,
			String exitButtonName,
			android.content.DialogInterface.OnClickListener exitClickListener) {
		if (context != null) {
		
			final AlertDialog.Builder mAlert = new AlertDialog.Builder(context);
			mAlert.setTitle(title);
			mAlert.setCancelable(false);
			mAlert.setMessage(message);
			mAlert.setPositiveButton(okButtonName, retryClickListener);
			mAlert.setNegativeButton(exitButtonName, exitClickListener);
			
			Activity activity = (Activity) context;
			activity.runOnUiThread(new Runnable() {
				  public void run() {
	
					  try {
						  mAlert.show();
					} catch (Exception e) {
						// TODO: handle exception
					}
				  }
				});


		}
	}
	
	public static void showCancelableAlert(Context context, String title,
			String message, String positiveButton,
			android.content.DialogInterface.OnClickListener positiveButtonClickListener,
			String negativeButton,
			android.content.DialogInterface.OnClickListener negativeButtonClickListener) {
		if (context != null) {
		
			final AlertDialog.Builder mAlert = new AlertDialog.Builder(context);
			mAlert.setTitle(title);
			mAlert.setCancelable(false);
			mAlert.setMessage(message);
			mAlert.setPositiveButton(positiveButton, positiveButtonClickListener);
			mAlert.setNegativeButton(negativeButton, negativeButtonClickListener);
			
			Activity activity = (Activity) context;
			activity.runOnUiThread(new Runnable() {
				  public void run() {
	
					  try {
						  mAlert.show();
					} catch (Exception e) {
						// TODO: handle exception
					}
				  }
				});


		}
	}
}
