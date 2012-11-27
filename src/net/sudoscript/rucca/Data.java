package net.sudoscript.rucca;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.format.Time;

public class Data {
	private static String logout = null;
	private static int logoutTime = Time.EPOCH_JULIAN_DAY;
	private static final String AESKEY = "wouldn't you like to know?";
	private static final String WIFI_NETWORK = "rusecure";
	 
    public static String getAeskey() {
		return AESKEY;
	}
	public static String getWifiNetwork() {
		return WIFI_NETWORK;
	}
	public static SharedPreferences getSettings(Context context) {
		return context.getSharedPreferences("MainActivity", Context.MODE_PRIVATE);
    }
	public static void setLogout(String l) {
		logout = l;
	}
    public static String getLogout() {
		return logout;
	}
	public static int getLogoutTime() {
		return logoutTime;
	}
	public static void setLogoutTime(int logoutTime) {
		Data.logoutTime = logoutTime;
	}
}
