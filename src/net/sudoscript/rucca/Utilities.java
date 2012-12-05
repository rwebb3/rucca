package net.sudoscript.rucca;

import java.util.List;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;


public class Utilities {
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
	public static String getIP(Context context)
    {
    	WifiManager myWifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
    	WifiInfo myWifiInfo = myWifiManager.getConnectionInfo();
    	int ipAddress = myWifiInfo.getIpAddress();
    	return android.text.format.Formatter.formatIpAddress(ipAddress);
    }
    static Boolean contains(List<String> webCode, String findme){
		java.util.regex.Pattern p = java.util.regex.Pattern.compile("");
		java.util.regex.Matcher m = p.matcher("");
		for(String s : webCode)
		{
		    m.reset(s);
		    if(m.find()) return true;
		}
		return false;
    }
}
