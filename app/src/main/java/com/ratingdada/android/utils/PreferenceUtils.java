package com.ratingdada.android.utils;

import android.content.Context;
import android.content.SharedPreferences;


public class PreferenceUtils {
	
	public static SharedPreferences pref; 
	private static Context ctx;
	
	public  PreferenceUtils(Context context){
		
		pref = context.getSharedPreferences("ratings", context.MODE_PRIVATE);
		ctx = context;
	}
	
	
	public static String getMoives(){
		
		String moiveslist = pref.getString("moiveslist", "");
		//ld.password =
		return moiveslist;

	}
	
	
	public static void setMovie(String Moiveid)
	{
		//String oldlist = getMoives() + ":" + Moiveid;
		pref.edit().putString("moiveslist", Moiveid).commit();
	}
}