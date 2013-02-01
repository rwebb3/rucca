package net.sudoscript.rucca;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

class Login extends AsyncTask<Void, Void, List<String>> {
	Context context = null;
	String token = null;
	//ProgressDialog dialog = ProgressDialog.show(MainActivity.getAppContext(), "", "Logging in. Please wait...", true);
	
	Login(Context context){
		   this.context = context;
		   token = context.getSharedPreferences("MainActivity", Context.MODE_PRIVATE).getString("logoutToken", "null"); //get token from preferences
	}
	
   @Override
   protected void onPreExecute() {
	SharedPreferences settings = context.getSharedPreferences("MainActivity", Context.MODE_PRIVATE);
	settings.edit().putString("logoutToken", "busy").commit();
	//create Logging in dialog
   	//dialog.show();	
   }
	 
   @Override
   protected List<String> doInBackground(Void... voids) {
	// Create a new HttpClient and Post Header
	   
	    String username = context.getSharedPreferences("MainActivity", Context.MODE_PRIVATE).getString("username", ""); //get username from preferences
   		String password = Utilities.getDecryptSettingsPassword(context); //get password from preferences
   		List<String> webCode = new ArrayList<String>();
   	
	    HttpClient httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost("https://cca-svr-40.radford.edu/auth/perfigo_cm_validate.jsp");

	    try {
	        // Add your data
	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	        
	        nameValuePairs.add(new BasicNameValuePair("reqFrom", "perfigo_login.jsp"));
	        nameValuePairs.add(new BasicNameValuePair("uri", "https://cca-svr-40.radford.edu/"));
	        nameValuePairs.add(new BasicNameValuePair("cm", "ws32vklm"));
	        nameValuePairs.add(new BasicNameValuePair("userip", Utilities.getIP(context)));
	        nameValuePairs.add(new BasicNameValuePair("session", ""));
	        nameValuePairs.add(new BasicNameValuePair("pm", "Win32"));
	        nameValuePairs.add(new BasicNameValuePair("index", "5"));
	        nameValuePairs.add(new BasicNameValuePair("pageid", "-1"));
	        nameValuePairs.add(new BasicNameValuePair("compact", "false"));
	        nameValuePairs.add(new BasicNameValuePair("registerGuest", "NO"));
	        nameValuePairs.add(new BasicNameValuePair("userNameLabel", "Username"));
	        nameValuePairs.add(new BasicNameValuePair("passwordLabel", "Password"));
	        nameValuePairs.add(new BasicNameValuePair("guestUserNameLabel", "Guest ID"));
	        nameValuePairs.add(new BasicNameValuePair("guestPasswordLabel", "Password"));
	        nameValuePairs.add(new BasicNameValuePair("username", username));
	        nameValuePairs.add(new BasicNameValuePair("password", password));
	        nameValuePairs.add(new BasicNameValuePair("provider", "RU Network Login"));
	        nameValuePairs.add(new BasicNameValuePair("submit", "Continue"));
	        

	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	        

	        // Execute HTTP Post Request
	        HttpResponse response = httpclient.execute(httppost);
	        HttpEntity entity = response.getEntity();
	        if(entity!=null) {
	        	
				BufferedReader reader = new BufferedReader(new StringReader(EntityUtils.toString(entity)));
				  for (String line; (line = reader.readLine()) != null;) {
				    webCode.add(line);
				  }
	        }

	    } catch (ClientProtocolException e) {
	        System.out.println("ClientProtocolException");
	    } catch (IOException e) {
	        System.out.println("IOException");
	    }
	    return webCode;
   }
   
   @Override
   protected void onPostExecute(List<String> webCode) {
  	 	//dialog.dismiss();
		if(Utilities.contains(webCode, "You have been successfully logged on the network")){
			SharedPreferences settings = context.getSharedPreferences("MainActivity", Context.MODE_PRIVATE);
	    	Toast.makeText(context, "Logged in!", Toast.LENGTH_SHORT).show();
	    	settings.edit().putInt("loginTimestamp", ((int) (System.currentTimeMillis() / 1000L))).commit();
	    	//get logout key
			settings.edit().putString("logoutToken", webCode.get(39).substring(48)).commit();
			settings.edit().putString("logoutToken", settings.getString("logoutToken", "null").substring(0, settings.getString("logoutToken", "null").length()-2)).commit();
	    	/* Why was this two lines?
	    	Data.setLogout(webCode.get(39).substring(48));
	    	Data.setLogout(token.substring(0, token.length()-2));\
	    	*/
		} else if(Utilities.contains(webCode, "Invalid")){
			Toast.makeText(context, "Invalid Username/Password.", Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(context, "Network Error. Will try again later", Toast.LENGTH_SHORT).show();
		}

		
   }
}