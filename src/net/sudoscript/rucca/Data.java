package net.sudoscript.rucca;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.format.Time;

public class Data {
	private static String logout = null;
	private static int logoutTime = Time.EPOCH_JULIAN_DAY;
	private static final String AESKEY = "wouldn't you like to know?";
	private static final String WIFI_NETWORK = "rusecure";
	 
    protected static String getAeskey() {
		return AESKEY;
	}
    protected static String getWifiNetwork() {
		return WIFI_NETWORK;
	}
    protected static SharedPreferences getSettings(Context context) {
		return context.getSharedPreferences("MainActivity", Context.MODE_PRIVATE);
    }
    protected static void setLogout(String l) {
		logout = l;
	}
    protected static String getLogout() {
		return logout;
	}
    protected static int getLogoutTime() {
		return logoutTime;
	}
    protected static void setLogoutTime(int logoutTime) {
		Data.logoutTime = logoutTime;
	}
}
