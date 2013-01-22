package net.sudoscript.rucca;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends Activity {
	EditText usernameEditText, passwordEditText;
	ToggleButton toggleButton;
	SharedPreferences settings;
	final private String AESKEY = Data.getAeskey();
    private static Context context;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        context = getApplicationContext();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //Retrieve preferences
        settings = getPreferences(MODE_PRIVATE);
        String username = settings.getString("username", "");
        String password = Utilities.getDecryptSettingsPassword(context);
        String enabled = settings.getString("enabled", "false");
        
        usernameEditText = (EditText)findViewById(R.id.editText2);
        passwordEditText = (EditText)findViewById(R.id.editText1);
        toggleButton = (ToggleButton)findViewById(R.id.toggleButton1);
        usernameEditText.setHint(username);
        if(password != ""){
        String asterisk = "";
			for (int i=2; i<password.length(); i++){
				asterisk += "*";
			}
			passwordEditText.setHint(password.substring(0,1)+asterisk+password.substring(password.length()-1));
        }        
        //if(setting_LoginOnLaunch) new loginTo().execute();
        if(enabled.equals("true")){
        	toggleButton.setChecked(true);
        } else {
        	toggleButton.setChecked(false);
        }
        if(getIntent().getData() != null){
        	//if (getIntent().getData().getHost().equals("clients3.google.com")){
        	Login l = new Login(context);
        	l.execute();
        	//}
        }
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        
            if(item.getTitle().equals("Logout")){
            	if(!(Data.getLogout() == null) && !(Data.getLogout().equals(""))){
            		new Logout(context).execute();
            		toggleButton = (ToggleButton) findViewById(R.id.toggleButton1);
            		toggleButton.setChecked(false);
            	} else {
            		Toast.makeText(context, "Nothing to log out of. Please login first.", Toast.LENGTH_SHORT).show();
            	}
        		return true;
            }
            else if(item.getTitle().equals("Advanced")){
                //TODO: show new screen
            	
            	//TODO: add wifi network info to screen
            	
            	//TODO: Allow changing of SSID 
            	
            	//TODO: Remove me
            	Toast.makeText(context, "1337 M0D3 Enabled", Toast.LENGTH_SHORT).show();
        		return true;
            }
            else if(item.getTitle().equals("Help")){
            	final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Help");
                builder.setIcon(android.R.drawable.ic_menu_help);
                builder.setNegativeButton(android.R.string.ok, null);
                final View v = LayoutInflater.from(this).inflate(R.layout.about, null);
                final TextView tv = (TextView)v.findViewById(R.id.aboutText);
                
                tv.setText(getString(R.string.helpText) + "\n\n" + new changeLogBuilder().toString());
                builder.setView(v);
                builder.create().show();
        		return true;
            }
            else if(item.getTitle().equals("About")){
            	final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("About "+ getString(R.string.app_name));
                builder.setIcon(android.R.drawable.ic_menu_info_details);
                builder.setNegativeButton(android.R.string.ok, null);
                final View v = LayoutInflater.from(this).inflate(R.layout.about, null);
                final TextView tv = (TextView)v.findViewById(R.id.aboutText);
                tv.setText(getString(R.string.aboutText));
                builder.setView(v);
                builder.create().show();
        		return true;
            }
            else if(item.getTitle().equals("Re-Authenticate")){
            	ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService( Context.CONNECTIVITY_SERVICE );
        		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
            	if(activeNetInfo != null && activeNetInfo.getTypeName().equals("WIFI")){
	            	WifiManager wfManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
	    		    WifiInfo wifiinfo = wfManager.getConnectionInfo();  
	    		    if (wifiinfo.getSSID().equals(Data.getWifiNetwork())){
	    		      Login l = new Login(context);
	    		      l.execute();
	    		    } 
            	} else {
    		    	Toast.makeText(context, "Please connect to " + Data.getWifiNetwork() + ".", Toast.LENGTH_LONG).show();
    		    }
        		return true;
            }
            	
            else return super.onOptionsItemSelected(item);        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    public void updateCredentials(View view){
    	if(!(usernameEditText.getText().toString().equals(""))){
    		settings.edit().putString("username", usernameEditText.getText().toString()).commit();
    		usernameEditText.setHint(usernameEditText.getText().toString());
    		usernameEditText.setText("");
    	}
    	if(!(passwordEditText.getText().toString().equals(""))){
    		String passwordPlainText = passwordEditText.getText().toString();
    		String passwordEnc = "";
    		try {
    			passwordEnc = SimpleCrypto.encrypt(AESKEY, passwordPlainText);
    		} catch (Exception e) {
    			System.out.println("Encryption Error");
    		}
    		settings.edit().putString("password", passwordEnc).commit();
    		String asterisk = "";
    		for (int i=2; i<passwordPlainText.length(); i++){
    			asterisk += "*";
    		}
    		passwordEditText.setHint(passwordPlainText.substring(0,1)+asterisk+passwordPlainText.substring(passwordPlainText.length()-1));
    		passwordEditText.setText("");
    	}
    	Toast.makeText(context, "Credentials updated", Toast.LENGTH_SHORT).show();
    }
    
    public void toggleButtonClick(View view){
    	if(toggleButton.isChecked()){
    		settings.edit().putString("enabled", "true").commit();
			WifiManager wfManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
		    WifiInfo wifiinfo = wfManager.getConnectionInfo();  
		    if(wifiinfo.getSSID().equals(Data.getWifiNetwork())){
		    	new Login(context).execute();
		    }

    	} else {
    		settings.edit().putString("enabled", "false").commit();
    	}
    }    
}


