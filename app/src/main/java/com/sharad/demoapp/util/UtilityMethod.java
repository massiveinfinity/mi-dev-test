package com.sharad.demoapp.util;

import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;


public class UtilityMethod {
	static ProgressDialog progressDialog;

	// method for bitmap to base64
	public static String encodeTobase64(Bitmap image) {
		Bitmap immage = image;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		immage.compress(Bitmap.CompressFormat.PNG, 100, baos);
		byte[] b = baos.toByteArray();
		String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

		Log.d("Image Log:", imageEncoded);
		return imageEncoded;
	}

	// method for base64 to bitmap
	public static Bitmap decodeBase64(String input) {
		byte[] decodedByte = Base64.decode(input, 0);
		return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
	}

	public static void showHashKey(Context context) {
		try {
			PackageInfo info = context.getPackageManager().getPackageInfo("com.recipe.activities", PackageManager.GET_SIGNATURES); // Your
																																		// package
																																		// name
																																		// here
			for (Signature signature : info.signatures) {
				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				Log.i("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
			}
		} catch (NameNotFoundException e) {
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	public static void progressDialogShow(Context mContext) {

		progressDialog = new ProgressDialog(mContext);

		progressDialog.setTitle(DataHelper.APP_TITLE);
		progressDialog.setMessage("Please wait...");
		progressDialog.setCancelable(false);

		//progressDialog.setIndeterminate(true);
		//progressDialog.setIndeterminateDrawable(mContext.getResources().getDrawable(R.anim.progressbar_animation));

		// progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.show();
	}

	public static void progressDialogDismiss(Activity mContext) {

		mContext.runOnUiThread(new Runnable() {
			public void run() {

				try {

					if (progressDialog != null) {
						progressDialog.dismiss();
					}

					progressDialog = null;
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});

	}
	public static boolean isNumber(String string) {
	    if (string == null || string.isEmpty()) {
	        return false;
	    }
	    int i = 0;
	    if (string.charAt(0) == '-') {
	        if (string.length() > 1) {
	            i++;
	        } else {
	            return false;
	        }
	    }
	    for (; i < string.length(); i++) {
	        if (!Character.isDigit(string.charAt(i))) {
	            return false;
	        }
	    }
	    return true;
	}
}
