package net.sudoscript.rucca;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.format.Time;

class Data {
	private static String logout = null;
	private static int logoutTime = Time.EPOCH_JULIAN_DAY;
	private static final String AESKEY = "wouldn't you like to know?";
	private static final String WIFI_NETWORK = "rusecure";
	 
    static String getAeskey() {
		return AESKEY;
	}
	static String getWifiNetwork() {
		return WIFI_NETWORK;
	}
	static SharedPreferences getSettings(Context context) {
		return context.getSharedPreferences("MainActivity", Context.MODE_PRIVATE);
    }
	static void setLogout(String l) {
		logout = l;
	}
    static String getLogout() {
		return logout;
	}
	static int getLogoutTime() {
		return logoutTime;
	}
	static void setLogoutTime(int logoutTime) {
		Data.logoutTime = logoutTime;
	}
}
