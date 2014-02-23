package net.sudoscript.rucca;

import java.util.List;
import java.util.Scanner;

import java.net.*;

import java.io.InputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.net.ssl.*;
import java.security.cert.*;

import java.security.NoSuchAlgorithmException;
import java.security.KeyManagementException;

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

    /**disable SSL certificate validation, needed in order to accept the CCA interception
     */
    static void disableSSLVerification(){
	try{
	  //Create a trust manager that does not validate certificate chains
 	  TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager(){
   	    public java.security.cert.X509Certificate[] getAcceptedIssuers(){
		return null;
	    }
	    public void checkClientTrusted(X509Certificate[] certs, String authType){}
	    public void checkServerTrusted(X509Certificate[] certs, String authType){}
	    }
	  };

	  // Install the all-trusting trust manager
	  SSLContext sc = SSLContext.getInstance("SSL");
	  sc.init(null, trustAllCerts, new java.security.SecureRandom());
	  HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

	  //Create all trusting host name verifier
	  HostnameVerifier allHostsValid = new HostnameVerifier(){
	    public boolean verify(String hostname, SSLSession session){
		return true;
	    }
	  };

	  //Install all-trusting host verifier
	  HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
  	}
	catch(Exception e){
	}
   }
   /**check if there is a captive portal blocking outside connections*/
   static boolean captivePortal(){
	   HttpURLConnection con = null;
	   try{
		   URL url = new URL("http://clients3.google.com/generate_204");
		   con = (HttpURLConnection) url.openConnection();
		   con.setInstanceFollowRedirects(false);
		   con.setConnectTimeout(10000);
		   con.setReadTimeout(10000);
		   con.setUseCaches(false);
		   con.getInputStream();
		   return con.getResponseCode() != 204;
	   }
	   catch (IOException e){
		   return false;
	   }
	   finally{
		   if (con != null){
			   con.disconnect();
		   }
	   }
   }

   static String getStackTrace(final Throwable throwable) {
     final StringWriter sw = new StringWriter();
     final PrintWriter pw = new PrintWriter(sw, true);
     throwable.printStackTrace(pw);
     return sw.getBuffer().toString();
   }
   

   /**get the URL of the captive portal*/
   static String getURL() {
	disableSSLVerification();
        HttpURLConnection con = null;
        try {
          URL url = new URL("https://www.google.com/blank.html");
          con = (HttpURLConnection) url.openConnection();          
	  InputStream in = con.getInputStream();
	  String encoding = con.getContentEncoding();
	  encoding = encoding == null ? "UTF-8" : encoding;
	  String body = new Scanner(in, encoding).useDelimiter("\\A").next();
	  
	  //get URL here
	  String URL = body.substring(body.indexOf("URL=")+4,body.indexOf("edu")+4);
	  dbg(URL);

	  return URL;
        } 
	catch (Exception e) {
          	dbg(getStackTrace(e));
		return "fail";
        } 
	finally {
          if (con != null) {
            con.disconnect();
          }
        }
    }

   static void dbg(String output){
	   System.out.println("Debug: " + output);
   }

}
