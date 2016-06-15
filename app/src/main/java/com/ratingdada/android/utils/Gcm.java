package com.ratingdada.android.utils;
import android.content.Context;
import android.content.SharedPreferences;

public class Gcm {

	public static SharedPreferences pref;
	private static Context ctx;

	public Gcm(Context context){
		
		pref = context.getSharedPreferences("gcm", context.MODE_PRIVATE);
		ctx = context;
	}
	
	
	public static String getGCM(){
		return pref.getString("gcmid", "none");

	}
	
	
	public static void setGCM(String gcmid)
	{
		System.out.println("gcmid in Gcm.java->"+gcmid);
		pref.edit().putString("gcmid", gcmid).commit();
	}

	public static void removeGCM(){
		
		pref.edit().putString("gcmid", "none").commit();
	}
	
}