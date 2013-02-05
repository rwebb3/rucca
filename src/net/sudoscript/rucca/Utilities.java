package net.sudoscript.rucca;

import java.util.List;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;


class Utilities {
    static String getDecryptSettingsPassword(Context context){
    	String passwordAsEncrypted = Data.getSettings(context).getString("password", "");
        String password = ""; //Decrypted
   			
   		try {
   			password = SimpleCrypto.decrypt(Data.getAeskey(), passwordAsEncrypted);
   		} catch (Exception e) {
   			System.out.println("Decryption Error");
   		}
		return password;
    }
    @SuppressWarnings("deprecation")
	static String getIP(Context context)
    {
    	WifiManager myWifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
    	WifiInfo myWifiInfo = myWifiManager.getConnectionInfo();
    	int ipAddress = myWifiInfo.getIpAddress();
    	return android.text.format.Formatter.formatIpAddress(ipAddress);
    }
    static Boolean contains(List<String> webCode, String findme){
		java.util.regex.Pattern p = java.util.regex.Pattern.compile(findme);
		java.util.regex.Matcher m = p.matcher("");
		for(String s : webCode)
		{
		    m.reset(s);
		    if(m.find()) return true;
		}
		return false;
    }
    static int indexOfList(List<String> webCode, String findme){
		java.util.regex.Pattern p = java.util.regex.Pattern.compile(findme);
		java.util.regex.Matcher m = p.matcher("");
		int i = 0;
		for(String s : webCode)
		{
		    m.reset(s);
		    if(m.find()) return i;
		    i++;
		}
		return i;
    }
    static boolean isConnectedToNetwork(WifiInfo wi){
    	//Apparently in Android 4.2 it seems WifiInfo.getSSID() puts quotes around the SSID name.
    	//if SSID == rusecure OR SSID == "rusecure"
    	if (wi.getSSID() == null) 
    		return false;
    	else if(wi.getSSID().equals(Data.getWifiNetwork()))
    		return true;
    	else if(wi.getSSID().equals("\""+Data.getWifiNetwork()+"\""))
    		return true;
    	else return false;
    }
}
