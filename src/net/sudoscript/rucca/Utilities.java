package net.sudoscript.rucca;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;


public class Utilities {
    protected static String getDecryptSettingsPassword(Context context){
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
	protected static String getIP(Context context)
    {
    	WifiManager myWifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
    	WifiInfo myWifiInfo = myWifiManager.getConnectionInfo();
    	int ipAddress = myWifiInfo.getIpAddress();
    	return android.text.format.Formatter.formatIpAddress(ipAddress);
    }
}
