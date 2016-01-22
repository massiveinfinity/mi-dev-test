package com.mi.afzaal.adddevicedetails;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.mi.afzaal.models.DeviceData;
import com.mi.afzaal.showcaseapplication.AppController;

public class IAddDeviceIntractorImpl implements IAddDeviceIntractor{

	@Override
	public void addDevice(String url, DeviceData data,
			final IAddDeviceListener listener) {

		JSONObject dataobj = createJsonObject(data);
		JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url,dataobj ,
				new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
               if(response !=null){
            	   listener.onsuccess();
            	   
               }
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				listener.onError();
				listener.setNetworkError();
			}
		});

		AppController.getInstance().addToRequestQueue(jsObjRequest);
	}

	JSONObject createJsonObject(DeviceData data){
		JSONObject jObj = new JSONObject();
		try{
			jObj.put("name", data.getName());
			jObj.put("version", data.getVersion());
			jObj.put("codename", data.getCodename());
			jObj.put("target", data.getTarget());
			jObj.put("distribution", data.getDistribution());
		}catch(JSONException ex){

		}
		return jObj;
	}

}
