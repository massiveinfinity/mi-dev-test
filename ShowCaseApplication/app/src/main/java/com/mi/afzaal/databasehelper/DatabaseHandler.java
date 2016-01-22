package com.mi.afzaal.databasehelper;


import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mi.afzaal.models.DeviceData;


public class DatabaseHandler extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "deviceinfo";

	// Contacts table name
	private static final String TABLE_DEVICE = "devices";

	// Contacts Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_NAME = "name";
	private static final String KEY_VERSION = "version";
	private static final String KEY_CODENAME = "codename";
	private static final String KEY_TARGET = "target";
	private static final String KEY_DISTRIBUTION="distribution";	   	  




	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_DEVICE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_DEVICE + "("
				+ KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_NAME + " TEXT," + KEY_VERSION + " TEXT,"
				+ KEY_CODENAME + " TEXT," + KEY_TARGET + " TEXT,"+ KEY_DISTRIBUTION +" TEXT"
				+  ")";



		db.execSQL(CREATE_DEVICE_TABLE);

	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_DEVICE);
		// Create tables again
		onCreate(db);
	}


	// Adding new contact
	public void addContact(ArrayList<DeviceData> devicedata) {

		SQLiteDatabase db = this.getWritableDatabase();

		for(int i =0;i<devicedata.size();i++){
			DeviceData data = devicedata.get(i);	   
			ContentValues values = new ContentValues();
			values.put(KEY_ID, data.getId());
			values.put(KEY_NAME, data.getName());
			values.put(KEY_VERSION, data.getVersion()); 
			values.put(KEY_CODENAME, data.getCodename()); 
			values.put(KEY_TARGET, data.getTarget()); 
			values.put(KEY_DISTRIBUTION, data.getDistribution());
			// Inserting Row
			db.insert(TABLE_DEVICE, null, values);
		}

		db.close(); // Closing database connection
	}

	


	
	
}