package com.sharad.demoapp.parser;

import java.io.IOException;



import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class ParseUtil {

	/*
	 * Method used to get Object from JSON String response
	 */
	public static <T> Object getObject(String data, Class<T> clazz) {
		return parseUsingGson(data, clazz);
//		return parseUsingJackson(data, clazz);
	}
	
	/*
	 * Parse data using GSON Lib
	 */
	private static <T> Object parseUsingGson(String data, Class<T> clazz) {
		try {
			Gson gson = new Gson();
			return gson.fromJson(data, clazz);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			return null;
		}
	}
	

}