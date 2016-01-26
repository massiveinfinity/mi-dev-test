package com.mi.androidarsenal.parser;


import com.mi.androidarsenal.model.AndroidInfo;
import com.mi.androidarsenal.model.Devices;
import com.mi.androidarsenal.model.Versions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Responsible for parsing data
 *
 * @author Samir Sarosh
 */
@SuppressWarnings("ALL")
public class ParseResponse {


    /**
     * @param jsonResponse
     * @return
     */
    public AndroidInfo parseAndroidInfoResponse(String jsonResponse) {
        AndroidInfo androidInfo = null;
        try {

            JSONObject mainObject = new JSONObject(jsonResponse);

            androidInfo = new AndroidInfo();
            ArrayList<Versions> versionPropList = new ArrayList<Versions>();
            ArrayList<Devices> devicePropList = new ArrayList<Devices>();

            if (mainObject.has("android")) {
                JSONArray androidArray = mainObject.getJSONArray("android");

                for (int i = 0; i < androidArray.length(); i++) {
                    Versions versions = new Versions();
                    JSONObject versionObj = androidArray.getJSONObject(i);


                    String id = versionObj.optString("id");
                    String name = versionObj.optString("name");
                    String version = versionObj.optString("version");
                    String codename = versionObj.optString("codename");
                    String target = versionObj.optString("target");
                    String distribution = versionObj.optString("distribution");

                    versions.setId(id);
                    versions.setName(name);
                    versions.setVersion(version);
                    versions.setCodename(codename);
                    versions.setTarget(target);
                    versions.setDistribution(distribution);

                    versionPropList.add(versions);
                }
                androidInfo.setVersionPropList(versionPropList);
            }

            if (mainObject.has("devices")) {
                JSONArray devicesArray = mainObject.getJSONArray("devices");

                for (int i = 0; i < devicesArray.length(); i++) {
                    Devices devices = new Devices();
                    JSONObject devicesObj = devicesArray.getJSONObject(i);

                    String id = devicesObj.optString("id");
                    String androidId = devicesObj.optString("androidId");
                    String carrier = devicesObj.optString("carrier");
                    String imageUrl = devicesObj.optString("imageUrl");
                    String name = devicesObj.optString("name");
                    String snippet = devicesObj.optString("snippet");

                    devices.setId(id);
                    devices.setAndroidId(androidId);
                    devices.setCarrier(carrier);
                    devices.setImageUrl(imageUrl);
                    devices.setName(name);
                    devices.setSnippet(snippet);

                    devicePropList.add(devices);
                }
                androidInfo.setDevicePropList(devicePropList);
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            return androidInfo;
        }
    }

}
