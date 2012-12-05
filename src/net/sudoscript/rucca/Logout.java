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
import android.os.AsyncTask;
import android.widget.Toast;

    class Logout extends AsyncTask<Void, Void, List<String>> {
		 //ProgressDialog dialog = ProgressDialog.show(MainActivity.this, "", "Logging out. Please wait...", true);
    	Context context = null;
    	
      public Logout(Context c){
    	  context = c;
      }
		 
      @Override
      protected void onPreExecute() {
     	//create Logging in dialog
      	//dialog.show();	
      }
		 
      @Override
      protected List<String> doInBackground(Void... voids) {
   	// Create a new HttpClient and Post Header
      		List<String> webCode = new ArrayList<String>();
      	
   	    HttpClient httpclient = new DefaultHttpClient();
   	    HttpPost httppost = new HttpPost("https://cca-svr-40.radford.edu/auth/perfigo_logout.jsp?user_key="+Data.getLogout());

   	    try {
   	        // Add your data
   	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
   	        
   	        nameValuePairs.add(new BasicNameValuePair("reqFrom", "perfigo_login.jsp"));
   	        nameValuePairs.add(new BasicNameValuePair("uri", "https://cca-svr-40.radford.edu/"));
   	        nameValuePairs.add(new BasicNameValuePair("cm", "ws32vklm"));
   	        

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
			if(webCode.size() >= 1 && webCode.get(1).contains("<!--error=0-->")){
				Toast.makeText(context, "You have been logged out!", Toast.LENGTH_SHORT).show();
				context.getSharedPreferences("MainActivity", Context.MODE_PRIVATE).edit().putString("enabled", "false").commit();
			} else {
				Toast.makeText(context, "Error Logging out. Please try re-authenticating.", Toast.LENGTH_SHORT).show();
			}
			Data.setLogout(null);
      }
}