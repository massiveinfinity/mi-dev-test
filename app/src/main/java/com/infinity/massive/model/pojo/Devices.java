package com.infinity.massive.model.pojo;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;

/**
 * Created by Ilanthirayan on 25/1/16.
 */
public class Devices {

    public static final String TABLE_NAME = "tbl_device";

    private int id;
    private String imageUrl;
    private String name;
    private String androidId;
    private String snippet;

    public Devices(){}

    public interface Columns extends BaseColumns {
        String DEVICE_ID = "id";
        String IMAGE_URL = "image_url";
        String NAME = "name";
        String ANDROID_ID = "androidId";
        String SNIPPET = "snippet";
    }

    public Devices(Cursor cursor){
        fromCursor(cursor);
    }

    private void fromCursor(Cursor cursor) {
        int i = cursor.getColumnIndexOrThrow(Columns.DEVICE_ID);
        setId(cursor.getInt(i));

        i = cursor.getColumnIndexOrThrow(Columns.IMAGE_URL);
        setImageUrl(cursor.getString(i));

        i = cursor.getColumnIndexOrThrow(Columns.NAME);
        setName(cursor.getString(i));

        i = cursor.getColumnIndexOrThrow(Columns.ANDROID_ID);
        setAndroidId(cursor.getString(i));

        i = cursor.getColumnIndexOrThrow(Columns.SNIPPET);
        setSnippet(cursor.getString(i));

    }

    public ContentValues toContentValues() {
        final ContentValues values = new ContentValues();
        values.put(Columns.DEVICE_ID, getId());
        values.put(Columns.IMAGE_URL, getImageUrl());
        values.put(Columns.NAME, getName());
        values.put(Columns.ANDROID_ID, getAndroidId());
        values.put(Columns.SNIPPET, getSnippet());
        return values;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAndroidId() {
        return androidId;
    }

    public void setAndroidId(String androidId) {
        this.androidId = androidId;
    }

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    @Override
    public String toString() {
        return "ClassPojo [id = " + id + ", imageUrl = " + imageUrl + ", name = " + name + ", androidId = " + androidId + ", snippet = " + snippet + "]";
    }
}
