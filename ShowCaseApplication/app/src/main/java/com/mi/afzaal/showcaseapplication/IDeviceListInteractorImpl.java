package com.mi.afzaal.showcaseapplication;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.mi.afzaal.databasehelper.DatabaseHandler;
import com.mi.afzaal.models.DeviceData;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by F ABBASI on 1/21/2016.
 */
public class IDeviceListInteractorImpl implements IDeviceListIntractor {

	ArrayList<DeviceData> deviceData;

	@Override
	public ArrayList<DeviceData> getDeviceList(String url, final OnDataReceivedListener listener) {

		StringRequest jsObjRequest = new StringRequest(Request.Method.GET,
				url, new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {
				Log.d("VOLLEY", response.toString());
				deviceData= getResponse(response);

				listener.onsuccess();
				listener.setData(deviceData);

			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				VolleyLog.d("VOLLEY", "Error: " + error.getMessage());
				deviceData = null;
				listener.setNetworkError();
			}
		});


		AppController.getInstance().addToRequestQueue(jsObjRequest);
		return deviceData;

	}

	@Override
	public void saveToDb(Context c,ArrayList<DeviceData> devicelist) {
		DatabaseHandler dbHandler = new DatabaseHandler(c);
		dbHandler.addContact(devicelist);
		
	}

	ArrayList<DeviceData> getResponse(String response){
		ArrayList<DeviceData> data = new ArrayList<DeviceData>();
		JSONArray CatjArray = null;
		try {
			CatjArray = new JSONArray(response);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		JSONObject cat_json_data=null;
		for(int i=0;i<CatjArray.length();i++){
			try{
				String id,name,version,code,target,distribution;
				cat_json_data=CatjArray.getJSONObject(i);
				DeviceData deviceData = new DeviceData();

				if(cat_json_data.isNull("id"))
					id = "";
				else
					id=cat_json_data.getString("id").trim();
				deviceData.setId(id);

				if(cat_json_data.isNull("name"))
					name = "";
				else
					name=cat_json_data.getString("name").trim();
				deviceData.setName(name);

				if(cat_json_data.isNull("version"))
					version = "";
				else
					version=cat_json_data.getString("version").trim();
				deviceData.setVersion(version);

				if(cat_json_data.isNull("codename"))
					code = "";
				else
					code=cat_json_data.getString("codename").trim();
				deviceData.setCodename(code);

				if(cat_json_data.isNull("target"))
					target = "";
				else
					target=cat_json_data.getString("target").trim();
				deviceData.setTarget(target);

				if(cat_json_data.isNull("distribution"))
					distribution = "";
				else
					distribution=cat_json_data.getString("distribution").trim();
				deviceData.setDistribution(distribution);


				//                String name=((null != cat_json_data.getString("name").trim())?cat_json_data.getString("name").trim():"");
				//                deviceData.setName(name);
				//                String version=((null != cat_json_data.getString("version").trim())?cat_json_data.getString("version").trim():"");
				//                deviceData.setVersion(version);
				//                String codename = ((null != cat_json_data.getString("codename").trim())?cat_json_data.getString("codename").trim():"");
				//                deviceData.setCodename(codename);
				//                String target= ((null != cat_json_data.getString("target").trim())?cat_json_data.getString("target").trim():"");
				//                deviceData.setTarget(target);
				//                String distribution=((null != cat_json_data.getString("distribution").trim())?cat_json_data.getString("distribution").trim():"");
				//                deviceData.setDistribution(distribution);


				data.add(deviceData);

			}catch (Throwable e) {
				data = null;
				e.printStackTrace();
			}
		}

		return data;

	}



}
