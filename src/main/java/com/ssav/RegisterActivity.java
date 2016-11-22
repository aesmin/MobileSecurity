package com.ssav;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gcm.GCMRegistrar;
import com.ssav.Virusprotection.ReportActivity;
import com.ssav.antitheft.Utils;

public class RegisterActivity extends Activity {
	
	// UI elements
	EditText txtName; 
	EditText txtEmail;
	
	// register button
	ImageButton btnRegister;

	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		//Get Global Controller Class object (see application tag in AndroidManifest.xml)
		final Controller aController = (Controller) getApplicationContext();


        if (GCMRegistrar.isRegisteredOnServer(this)) {

            // Skips registration.
            Toast.makeText(getApplicationContext(), "Already registered with GCM Server", Toast.LENGTH_LONG).show();

            Intent intent = new Intent(this, appActivity.class);
            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            this.finish();

        }else
        {

            setContentView(R.layout.activity_register);


        // Check if Internet Connection present
		if (!aController.isConnectingToInternet()) {
			
			// Internet Connection is not present
			aController.showAlertDialog(RegisterActivity.this,
					"Internet Connection Error",
					"Please connect to working Internet connection", false);
			
			// stop executing code by return
			return;
		}

		// Check if GCM configuration is set
		if (Config.YOUR_SERVER_URL == null || Config.GOOGLE_SENDER_ID == null || Config.YOUR_SERVER_URL.length() == 0
				|| Config.GOOGLE_SENDER_ID.length() == 0) {
			
			// GCM sernder id / server url is missing
			aController.showAlertDialog(RegisterActivity.this, "Configuration Error!",
					"Please set your Server URL and GCM Sender ID", false);
			
			// stop executing code by return
			 return;
		}
		
		txtName = (EditText) findViewById(R.id.txtName);
		txtEmail = (EditText) findViewById(R.id.txtEmail);
		btnRegister = (ImageButton) findViewById(R.id.btnRegister);
		
		// Click event on register button
		btnRegister.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {  
				// Get data from EditText 
				String name = txtName.getText().toString(); 
				String email = txtEmail.getText().toString();
				
				// Check if user filled the form
				if(name.trim().length() > 0 && email.trim().length() > 0){
					
					// Launch Main Activity
					Intent i = new Intent(getApplicationContext(), GCMMainActivity.class);
					
					// Registering user on our server					
					// Sending registraiton details to GCMMainActivity
					i.putExtra("name", name);
					i.putExtra("email", email);
					startActivity(i);
					finish();
					
				}else{
					
					// user doen't filled that data
					aController.showAlertDialog(RegisterActivity.this, "Registration Error!", "Please enter your details", false);
				}
			}
		});
	}
}
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.about_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
			case R.id.about_us:
				Utils.createWebViewDialog("file:///android_asset/aboutus.html", this);
				return true;
			case R.id.reports:
				Intent intent = new Intent(this, ReportActivity.class);
				startActivity(intent);
				return true;
			case R.id.license_info:
				Intent i = new Intent(this, LicenseInfo.class);
				startActivity(i);
				return true;
			case R.id.register:
				Intent register = new Intent(this, AppRegisterActivity.class);
				startActivity(register);
				return true;
			case R.id.support:
				Utils.createWebViewDialog("file:///android_asset/support.html", this);
				return true;

			case R.id.purchase:
				Utils.createWebViewDialog("file:///android_asset/purchase.html", this);
				return true;
		}
		return false;
	}



}
