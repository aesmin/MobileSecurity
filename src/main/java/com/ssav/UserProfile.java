package com.ssav;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gcm.GCMRegistrar;
import com.ssav.DB.DBAdapter;
import com.ssav.Virusprotection.ReportActivity;
import com.ssav.antitheft.Utils;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Minaz on 3/1/14.
 */
public class UserProfile extends Activity {
    EditText mEtPwd;
    CheckBox mCbShowPwd;
    DBAdapter myDb;

    // UI elements
    EditText txtName;
    EditText txtEmail;
    EditText txtMobile;
    EditText txtPwd;
    TextView txtActivationKey;

    // register button
    Button btnSubmit;
    String fontPath = "fonts/ROCKB.TTF";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userfieldactivity);
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
        openDB();
        Intent i= getIntent();
        String isTrial = i.getStringExtra("data");
        TextView txtUser = (TextView)findViewById(R.id.txtUser);
        txtUser.setTypeface(tf);
        TextView txtMobNo = (TextView)findViewById(R.id.txtMobNo);
        txtMobNo.setTypeface(tf);
        TextView txtEmail1 = (TextView)findViewById(R.id.txtEmail);
        txtEmail1.setTypeface(tf);
        TextView txtPassword = (TextView)findViewById(R.id.txtPassword);
        txtPassword.setTypeface(tf);
        TextView showpassword = (TextView)findViewById(R.id.showpassword);
        showpassword.setTypeface(tf);
        TextView txtActivationKey = (TextView)findViewById(R.id.txtActivationKey);
        txtActivationKey.setTypeface(tf);

        txtName = (EditText) findViewById(R.id.txtUserVal);
        txtEmail = (EditText) findViewById(R.id.txtEmailVal);
        txtMobile = (EditText) findViewById(R.id.txtMobNoVal);
        txtPwd = (EditText) findViewById(R.id.txtPasswordVal);
        btnSubmit = (Button) findViewById(R.id.bt_submit);
        btnSubmit.setTypeface(tf);
        Cursor cursor = myDb.getAllRows();
        if(cursor.moveToFirst())
        {
            if(cursor.getString(DBAdapter.COL_USERNAME) != null)
            {
                txtName.setText(cursor.getString(DBAdapter.COL_USERNAME));
                //txtName.setEnabled(false);
            }

            if(cursor.getString(DBAdapter.COL_EMAILADD) != null)
            {
                txtEmail.setText(cursor.getString(DBAdapter.COL_EMAILADD));
                //txtEmail.setEnabled(false);
            }

            if(cursor.getString(DBAdapter.COL_MOBILENO) != null)
            {
                txtMobile.setText(cursor.getString(DBAdapter.COL_MOBILENO));
                //txtMobile.setEnabled(false);
            }
        }


        //Log.w("Minaz", "" + isTrial);
        if(isTrial != null && isTrial.equals("1"))
        {
            txtActivationKey = (TextView) findViewById(R.id.txtActivationKey);
            txtActivationKey.setVisibility(View.INVISIBLE);
            EditText txtActivationKeyVal = (EditText )findViewById(R.id.txtActivationKeyVal);
            txtActivationKeyVal.setVisibility(View.INVISIBLE);

        }
        // get the password EditText
        mEtPwd = (EditText) findViewById(R.id.txtPasswordVal);
        // get the show/hide password Checkbox
        mCbShowPwd = (CheckBox) findViewById(R.id.cbShowPwd);

        // add onCheckedListener on checkbox
        // when user clicks on this checkbox, this is the handler.
        mCbShowPwd.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // checkbox status is changed from uncheck to checked.
                if (!isChecked) {
                    // show password
                    mEtPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    // hide password
                    mEtPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });


        final Controller aController = (Controller) getApplicationContext();


        if (GCMRegistrar.isRegisteredOnServer(this) && cursor.getString(DBAdapter.COL_ACTIVATIONDT) != null) {

            // Skips registration.
            Toast.makeText(getApplicationContext(), "Already registered with GCM Server", Toast.LENGTH_LONG).show();

            Intent intent = new Intent(this, appActivity.class);
            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            UserProfile.this.finish();
        }else
        {

           // setContentView(R.layout.activity_register);


            // Check if Internet Connection present
            if (!aController.isConnectingToInternet()) {

                // Internet Connection is not present
                aController.showAlertDialog(UserProfile.this,
                        "Internet Connection Error",
                        "Please connect to working Internet connection", false);

                // stop executing code by return
                return;
            }

            // Check if GCM configuration is set
            if (Config.YOUR_SERVER_URL == null || Config.GOOGLE_SENDER_ID == null || Config.YOUR_SERVER_URL.length() == 0
                    || Config.GOOGLE_SENDER_ID.length() == 0) {

                // GCM sernder id / server url is missing
                aController.showAlertDialog(UserProfile.this, "Configuration Error!",
                        "Please set your Server URL and GCM Sender ID", false);

                // stop executing code by return
                return;
            }



            // Click event on register button
            btnSubmit.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // Get data from EditText
                    String name = txtName.getText().toString();
                    String email = txtEmail.getText().toString();

                    String mobile = txtMobile.getText().toString();
                    String pwd = txtPwd.getText().toString();

                    // Check if user filled the form
                    if(name.trim().length() > 0 && email.trim().length() > 0){


                        boolean result = myDb.updateUserInfo(1,name,email,mobile);
                        Date dt = new Date();

                        Calendar cal = Calendar.getInstance();
                        cal.setTime(dt);
                        cal.add(Calendar.DATE, 30);

                        result = myDb.updateLicType(1,"1", cal.getTime());

                        // Launch Main Activity
                        Intent i = new Intent(getApplicationContext(), GCMMainActivity.class);

                        // Registering user on our server
                        // Sending registraiton details to GCMMainActivity
                        i.putExtra("name", name);
                        i.putExtra("email", email);
                        i.putExtra("mobile", mobile);
                        i.putExtra("pwd", pwd);
                        startActivity(i);
                        finish();

                    }else{

                        // user doen't filled that data
                        aController.showAlertDialog(UserProfile.this, "Registration Error!", "Please enter your details", false);
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


    private void openDB() {
        myDb = new DBAdapter(this);
        myDb.open();
    }


}
