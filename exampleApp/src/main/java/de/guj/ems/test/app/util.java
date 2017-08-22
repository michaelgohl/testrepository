package de.guj.ems.test.app;

import android.app.Activity;
import android.content.SharedPreferences;

public class util {
	
	private static final String PREFS_NAME = "gujEmsTestPrefsFile";
	private static SharedPreferences settingsStorage = null;
	private static SharedPreferences.Editor editor = null;
	
	public static void init(Activity activ) {
		settingsStorage = activ.getSharedPreferences(PREFS_NAME, 0);
		editor = settingsStorage.edit();
	}
	
	public static boolean getBooleanSettingByKey(String key) throws Exception {
		if (settingsStorage == null) {
			throw new Exception("please call init first");
		}
		
		return settingsStorage.getBoolean(key, false);
	}
	
	public static void setBooleanSetting(String key, boolean value) throws Exception {
		if (settingsStorage == null) {
			throw new Exception("please call init first");
		}
		
		editor.putBoolean(key, value);
		editor.commit();
	}
	
	public static String getStringSettingByKey(String key) throws Exception {
		if (settingsStorage == null) {
			throw new Exception("please call init first");
		}
		
		return settingsStorage.getString(key, "");
	}
	
	public static void setStringSetting(String key, String value) throws Exception {
		if (settingsStorage == null) {
			throw new Exception("please call init first");
		}
		
		editor.putString(key, value);
		editor.commit();
	}
	
	public static int getIntegerSettingByKey(String key) throws Exception {
		if (settingsStorage == null) {
			throw new Exception("please call init first");
		}
		
		return settingsStorage.getInt(key, 0);
	}
	
	public static void setIntegerSetting(String key, int value) throws Exception {
		if (settingsStorage == null) {
			throw new Exception("please call init first");
		}
		
		editor.putInt(key, value);
		editor.commit();
	}
	
	public static float getFloatIntegerByKey(String key) throws Exception {
		if (settingsStorage == null) {
			throw new Exception("please call init first");
		}
		
		return settingsStorage.getFloat(key, 0);
	}
	
	public static void setFloatSetting(String key, float value) throws Exception {
		if (settingsStorage == null) {
			throw new Exception("please call init first");
		}
		
		editor.putFloat(key, value);
		editor.commit();
	}
}
