package com.ssav.antitheft;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ssav.AppRegisterActivity;
import com.ssav.LicenseInfo;
import com.ssav.R;
import com.ssav.Virusprotection.ReportActivity;

public class LoginActivity extends Activity {
    private static final String TAG = "SSMS";
    
    private static boolean mPasswordSet;
    private static boolean mPasswordWanted;
    private String mCurrentPassword;
    EditText mPassword;
    Typeface tf;

    private BroadcastReceiver receiver;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set View to login.xml
        setContentView(R.layout.login);

        tf = Typeface.createFromAsset(getAssets(), "Roboto-Light.ttf");
        TextView txtView1 = (TextView) findViewById(R.id.textView1);
        txtView1.setTypeface(tf);
        
        final SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        
        mCurrentPassword = preferences.getString(
                ARegisterActivity.PREFERENCES_CURRENT_PASSWORD,
                this.getResources().getString(
                        R.string.config_default_login_password));

        mPasswordSet = preferences.getBoolean(
                ARegisterActivity.PREFERENCES_AEGIS_PASSWORD_SET,
                this.getResources().getBoolean(
                        R.bool.config_default_password_set));
        
        mPasswordWanted = preferences.getBoolean(
                ARegisterActivity.PREFERENCES_PASSWORD_WANTED,
                this.getResources().getBoolean(
                        R.bool.config_default_password_wanted));


    } public boolean onCreateOptionsMenu(Menu menu) {
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
            case R.id.support:
                Utils.createWebViewDialog("file:///android_asset/support.html", this);
                return true;

            case R.id.purchase:
                Utils.createWebViewDialog("file:///android_asset/purchase.html", this);
                return true;
        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!mPasswordWanted) {
            Intent SSMSIntent = new Intent(LoginActivity.this,
                    AegisActivity.class);
            SSMSIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            SSMSIntent
                    .addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            startActivity(SSMSIntent);
            finish();
        }
        
        if (!mPasswordSet && mPasswordWanted) {
            mPasswordSet = true;
            Intent registerIntent = new Intent(LoginActivity.this,
                    ARegisterActivity.class);
            registerIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            registerIntent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            registerIntent.putExtra("fromLogin", true);
            startActivity(registerIntent);
            finish();
        }

        mPassword = (EditText) findViewById(R.id.login_password);
        Button loginScreen = (Button) findViewById(R.id.btnLogin);
        mPassword.setTypeface(tf);
        loginScreen.setTypeface(tf);
        loginScreen.getBackground().setAlpha(255);

        // Listening to Login Screen button
        loginScreen.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (mPassword == null) {
                    Toast.makeText(getApplicationContext(),
                            getResources().getString(R.string.login_password_toast_password_enter), Toast.LENGTH_LONG)
                            .show();
                }

                String mPasswordText = mPassword.getText().toString();

                if (mPasswordText.equals(mCurrentPassword)) {
                    Intent SSMSIntent = new Intent(LoginActivity.this,
                            AegisActivity.class);
                    SSMSIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    SSMSIntent
                            .addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                    startActivity(SSMSIntent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(),
                            getResources().getString(R.string.login_password_toast_password_fail), Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}
