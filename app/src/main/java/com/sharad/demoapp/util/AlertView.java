package com.sharad.demoapp.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

public class AlertView
{	
	public static void showError(String message, Context ctx)
	{
		showAlert("Error", message, ctx);
	}
	
	public static void showAlert(String message, Context ctx)
	{
		showAlert("Alert", message, ctx);
	}
	
	public static void showAlert(String title, String message, Context ctx)
	{
		//Create a builder	
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		builder.setTitle(title);
		builder.setMessage(message);
		//add buttons and listener
		EmptyListener pl = new EmptyListener();
		builder.setPositiveButton("OK", pl);
		//Create the dialog
		AlertDialog ad = builder.create();
		//show
		ad.show();	
	}
	
	public static void showShortToast(Context mContext,String msg)
	{
		Toast toast = Toast.makeText(mContext, msg, Toast.LENGTH_SHORT);
		toast.show();
	}
	public static void showLongToast(Context mContext,String msg)
	{
		Toast toast = Toast.makeText(mContext, msg, Toast.LENGTH_LONG);
		toast.show();
	}
}

class EmptyListener implements DialogInterface.OnClickListener
{
	@Override
	public void onClick(DialogInterface dialog, int which)
	{
	}	
}
