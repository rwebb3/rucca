import java.net.*;
import javax.net.ssl.*;
import java.io.*;
import java.util.*;
import java.security.cert.*;
import java.security.*;

public class gatewayTest{
	static{
	  disableSSLVerification();
	}
	private static void disableSSLVerification(){
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
	 catch(NoSuchAlgorithmException e){
		  e.printStackTrace();
	 }
	 catch(KeyManagementException e){
		  e.printStackTrace();
	 }
        }
	public static void main(String[] args){
		try{
		  


			/*
		  URL url = new URL("https://www.google.com");
		  URLConnection con = url.openConnection();
		  System.out.println("original url: " + con.getURL());
		  con.connect();
		  System.out.println("connected url: " + con.getURL());
		  InputStream in = con.getInputStream();
		  System.out.println("redirected url: " + con.getURL());
		  in.close();
		  */

		  HttpURLConnection con = (HttpURLConnection)(new URL("https://www.google.com").openConnection());
		  con.setInstanceFollowRedirects(false);
		  con.setConnectTimeout(100000);
		  con.setReadTimeout(100000);
		  con.connect();
		  con.getInputStream();
		  System.out.println(con.getHeaderField("Location"));
		  
		  System.out.println(con.getURL());
		  //con.connect();
		  //System.out.println(con.getURL());
		  //int responseCode = con.getResponseCode();
		  //System.out.println( responseCode );
		  //String location = con.getHeaderField("Location");
		  //System.out.println(location);
		  
		  //String encoding = con.getContentEncoding();
		  //encoding = encoding == null ? "UTF-8" : encoding;
		  //String body = new Scanner(in, encoding).useDelimiter("\\A").next();
		  //String body = IOUtils.toString(in, encoding);
		  //System.out.println(body);
		}
		catch(IOException e){
			System.err.println(e.getMessage());
		}

	}
}
		
